package com.yinaf.dragon.Content.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yinaf.dragon.Content.Adapter.ChatListAdapter;
import com.yinaf.dragon.Content.Bean.ChatListBean;
import com.yinaf.dragon.Content.Bean.ChatMessage;
import com.yinaf.dragon.Content.Bean.MediaPlayerManager;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.Db.DatabaseHelper;
import com.yinaf.dragon.Content.Net.GetMemberMessagesAPI;
import com.yinaf.dragon.Content.Receiver.RefreshVoiceReceiver;
import com.yinaf.dragon.Content.Utils.ActivityCollector;
import com.yinaf.dragon.Content.Utils.SoundMeter;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.DateTimeUtil;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.yinaf.dragon.Tool.APP.App.context;
import static com.yinaf.dragon.Tool.Utils.DateTimeUtil.getStampTime;

/**
 * Created by long on 2018/05/10.
 * 功能：语音聊天页面
 * 点击录制声音后  保存声音到本地  然后将文件路径 存到对象中去
 * 点击条目 再去从对象中拿到声音  如果有文件 就播 没有就拿url播
 * 将发送失败的语音 保存在本地数据库中  再次进入把语音加在后面
 */

public class ChatMainAct extends BasicAct implements ChatListAdapter.ItemListener,
        GetMemberMessagesAPI.GetMemberMessagesListener,RefreshVoiceReceiver.RefreshVoiceListener{

    @BindView(R.id.tool_bar_title)
    TextView toolBarTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.send_button)
    Button send_button;
    @BindView(R.id.chat_popup)
    LinearLayout chat_popup;
    @BindView(R.id.cancel_button)
    Button cancel_button;
    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.volume)
    ImageView volume;
    @BindView(R.id.voice_text)
    TextView voice_text;
    @BindView(R.id.point_text)
    TextView point_text;
    @BindView(R.id.up_img)
    ImageView up_img;
    @BindView(R.id.chat_swipe)
    SwipeRefreshLayout refreshLayout;

    /**
     * 需要的权限
     */
    private static final String[] INITIAL_PERMS={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };
    private static final int INITIAL_REQUEST=1337;

    private ChatListBean chatBean;

    private static final String WATCH_BEAN = "WATCH_BEAN";
    private static final String COMMIT_SEND = "COMMIT_SEND";
    private static final String FAIL_SEND = "FAIL_SEND";
    private static final int[] drawableAmp = {R.drawable.amp1, R.drawable.amp1, R.drawable.amp2, R.drawable.amp2, R.drawable.amp3,
            R.drawable.amp3, R.drawable.amp4, R.drawable.amp4, R.drawable.amp5, R.drawable.amp5, R.drawable.amp6, R.drawable.amp6, R.drawable.amp6};

    private static final int POLL_INTERVAL = 300;
    private static final int MAX_LENGTH = 20 * 1000;//最大长度

    private static final String CANCEL = "cancel";
    private static final String SEND = "send";

    public static final String VOICE_NAME = ".amr";

    public String voice_name;

    private static String audioPath = Builds.VOICE_PATH;

    private AudioManager audioManager;

    private ChatListAdapter adapter;

    private SoundMeter mSensor;
    private Handler mHandler = new Handler();

    private LinearLayoutManager linerLayoutManager;

    private boolean isMove;//是否移动
    private boolean isDowning;//是否按下
    private boolean isUp;//是否弹起

    private MediaPlayerManager playerManager;

    private List<ChatMessage> messageList = new ArrayList<>();//所有数据

    private long startTimeM, stopTimeM = 0, click_time;

    private CountDownTimer timer;

    private int maxTime = MAX_LENGTH;//最大语音长度

    RefreshVoiceReceiver refreshVoiceReceiver;//刷新语音广播

    DatabaseHelper dbHelper;



    public ChatMainAct() {
        super(R.layout.act_chat_main_layout, R.string.title_activity_chat_main_list, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context, ChatListBean chatBean) {
        Intent intent = new Intent(context, ChatMainAct.class);
        intent.putExtra("WATCH_BEAN", chatBean);
        context.startActivity(intent);
    }


    @Override
    public void initView() {
        chatBean =getIntent().getParcelableExtra(WATCH_BEAN);
        toolBarTitle.setText(chatBean.getName());
        dbHelper = new DatabaseHelper(this,SPHelper.getString(Builds.SP_USER, "userName"));
        refreshVoiceReceiver = new RefreshVoiceReceiver(this);
        RefreshVoiceReceiver.register(this,refreshVoiceReceiver);
        initListView();

        //默认关闭扬声器
        openOrCloseSpeaker(false);
        initFile();
        initButtonClick();
        playerManager = new MediaPlayerManager(this, voice_text);
        playerManager.setHandler(mHandler);
        setRefreshLayout();
        new GetMemberMessagesAPI(ChatMainAct.this,0,chatBean.getMemberId());

    }

    /**
     * 初始化按钮点击事件
     */
    private void initButtonClick() {
        send_button.setOnTouchListener(new SoundTounchLinstener());
        send_button.setLongClickable(true);
        send_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isDowning)
                    return false;
                startTimeM = System.currentTimeMillis();
                voice_name = audioPath + startTimeM + VOICE_NAME;
                isDowning = true;
                long click_time = startTimeM - stopTimeM;
                recordStartListener(click_time, voice_name);
                return false;
            }
        });
    }

    /**
     * 初始化文件
     */
    private void initFile() {
        // 文件或目录不存在时,创建目录和文件.
        File file = new File(audioPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 初始化列表
     */
    private void initListView() {
        adapter = new ChatListAdapter(this, messageList, this);
        linerLayoutManager = new LinearLayoutManager(this);
        linerLayoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        linerLayoutManager.setReverseLayout(true);//列表翻转
        recyclerView.setLayoutManager(linerLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        linerLayoutManager.scrollToPositionWithOffset(0, 0);
        mSensor = new SoundMeter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    //下拉加载更多
    public void setRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.common_blue);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetMemberMessagesAPI(ChatMainAct.this,messageList.get(messageList.size()-1).getM_id(),chatBean.getMemberId());
            }
        });
    }

    /**
     * 打开或关闭扬声器
     */
    private void openOrCloseSpeaker(boolean isSpeakerphoneOn) {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        audioManager.setSpeakerphoneOn(isSpeakerphoneOn);
        if (!isSpeakerphoneOn) {
            // 设置成听筒模式
            audioManager.setMode(AudioManager.MODE_IN_CALL);
//             设置为通话状态
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }
    }

    /**
     * 列表的item点击事件
     *
     * @param type
     * @param bean
     * @param v
     * @param position
     */
    @Override
    public void itemCLick(int type, ChatMessage bean, View v, int position) {
        if (bean.getVoice_length() > 0)
            playerManager.startPlay(v, type, bean, position);
    }
    //点击失败按钮  重新发送
    @Override
    public void rePost(ChatMessage bean, int position) {
        if (bean.getVoice_url() == null) {
            TipUtils.showTip(getResources().getString( R.string.cannot_find_file));
        }
        File file = new File(bean.getVoice_url());
        sendMessage(file,bean.getVoice_length()+"");

    }
    //点击头像跳转详情界面
    @Override
    public void headInfoClick(int type) {

    }

    @OnClick(R.id.voice_text)
    public void onViewClicked() {

        if (audioManager.isSpeakerphoneOn()) {
            openOrCloseSpeaker(false);
            voice_text.setText(R.string.receiver_model);
            voice_text.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.drawable.receiver_title_img), null, null);
        } else {
            openOrCloseSpeaker(true);
            voice_text.setText(R.string.speaker_model);
            voice_text.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context, R.drawable.speaker_title_img), null, null);
        }

    }


    /**
     * 发送消息到成员
     * @param file
     */
    private void sendMessage(File file, String length){

        OkHttpUtils
                .post()
                .addParams("length", length)
                .addParams("memberId", chatBean.getMemberId() + "")
                .addParams("sessionId", SPHelper.getString(Builds.SP_USER,"sessionId"))
                .addFile("file", file.getName(), file)
                .url(Builds.HOST + "/mobile/user/sendMessage")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Log.e("sendMessage    ",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (JSONUtils.getInt(jsonObject,"code") == 5107){
                                ActivityCollector.finishAll();
                                LoginAct.startNewLoginAct();
                                TipUtils.showTip(JSONUtils.getString(jsonObject,"msg"));

                            }else if (jsonObject.getBoolean("success")) {
                                messageList.get(0).setM_id(JSONUtils.getInt(jsonObject, "obj"));
                                messageList.get(0).setIs_send(ChatListAdapter.SUCCESS);
                                //这样刷新adapter中的list 并且不会出现卡顿
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                }, 400);
                                linerLayoutManager.scrollToPosition(0);
                            }else {
                                TipUtils.showTip(jsonObject.getString("msg"));
                                messageList.get(0).setIs_send(ChatListAdapter.FAIL);
                                //这样刷新adapter中的list 并且不会出现卡顿
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                }, 400);
                                linerLayoutManager.scrollToPosition(0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    /**
     * 查询聊天记录
     * @param content
     */
    @Override
    public void getMemberMessagesSuccess(JSONArray content) throws JSONException {
        for (int i = 0; i < content.length(); i++){
            JSONObject jsonObject = content.getJSONObject(i);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setM_id(JSONUtils.getInt(jsonObject,"m_id"));
            chatMessage.setVoice_length(JSONUtils.getInt(jsonObject,"voice_length"));
            chatMessage.setVoice_url(JSONUtils.getString(jsonObject,"voice_url"));
            chatMessage.setIs_send(200);
            chatMessage.setSend_type(JSONUtils.getInt(jsonObject,"send_type"));
            chatMessage.setImg(JSONUtils.getString(jsonObject,"img"));
            messageList.add(chatMessage);
        }
        if (refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
        if (content.length() == 0) {
            TipUtils.showTip(getResources().getString(R.string.no_more_chatmessage));
            return;
        }
        adapter.notifyItemRangeInserted(messageList.size(), content.length());
        linerLayoutManager.scrollToPosition(0);
    }

    @Override
    public void getMemberMessagesError(long code, String msg) {
        TipUtils.showTip(msg);
    }

    /**
     * 接收到语音消息的广播推送
     */
    @Override
    public void receiverRefreshVoice(String data) {

        try {
            JSONObject jsonObject = new JSONObject(data);
            ChatMessage messagesBean = new ChatMessage();
            messagesBean.setVoice_length(Integer.parseInt(JSONUtils.getString(jsonObject,"voiceDate")));
            String time = getStampTime(DateTimeUtil.getNowTime("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
            messagesBean.setCreate_time_double(Double.valueOf(time));
            Member member = dbHelper.selectMemberByMemberId(Integer.parseInt(JSONUtils.getString(jsonObject,"memberId")));
            messagesBean.setMemberId(member.getMemberId());
            messagesBean.setWatchId(member.getWatchId());
            messagesBean.setSend_type(0);
            messagesBean.setRead_status(1);
            messagesBean.setVoice_url(JSONUtils.getString(jsonObject,"content"));
            messagesBean.setImg(member.getImage());
            messagesBean.setIs_send(ChatListAdapter.SUCCESS);
            messagesBean.setM_id(Integer.parseInt(JSONUtils.getString(jsonObject,"voiceId")));
            messageList.add(0, messagesBean);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyItemRangeInserted(0, 1);

        //这样刷新adapter中的list 并且不会出现卡顿
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 400);

        linerLayoutManager.scrollToPosition(0);

    }

    /**
     * 按钮监听
     */
    private class SoundTounchLinstener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean permission = ContextCompat.checkSelfPermission(getApplication(), android.Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED ;

            if (!permission ) {

                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                    requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
                }else{
                    TipUtils.showTip("请先在手机的设置中心，设置允许访问麦克风的权限");
                }
            }else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isDowning = false;
                        send_button.setBackgroundResource(R.drawable.chat_send_click_button);
                        send_button.setText(R.string.up_click_isover);

                        isMove = false;

                        chat_popup.setVisibility(View.VISIBLE);
                        imageView1.setVisibility(View.VISIBLE);
                        volume.setVisibility(View.VISIBLE);
                        point_text.setVisibility(View.VISIBLE);
                        cancel_button.setVisibility(View.GONE);
                        up_img.setVisibility(View.GONE);

                        playerManager.stopPlay();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!isDowning && isUp)
                            return false;

                        if (event.getY() < -300) {
                            imageView1.setVisibility(View.GONE);
                            volume.setVisibility(View.GONE);
                            point_text.setVisibility(View.GONE);
                            cancel_button.setVisibility(View.VISIBLE);
                            up_img.setVisibility(View.VISIBLE);

                            send_button.setText(R.string.up_is_over_too);

                            isMove = true;
                        } else {
                            imageView1.setVisibility(View.VISIBLE);
                            volume.setVisibility(View.VISIBLE);
                            point_text.setVisibility(View.VISIBLE);
                            cancel_button.setVisibility(View.GONE);
                            up_img.setVisibility(View.GONE);
                            send_button.setText(R.string.up_click_isover);
                            isMove = false;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isUp) {
                            isUp = false;
                            return false;
                        }
                        send_button.setBackgroundResource(R.drawable.chat_send_button);
                        send_button.setText(R.string.send_voice);
                        chat_popup.setVisibility(View.GONE);

                        if (!isDowning) {
                            return false;
                        }

                        recordStopListener();

                        if (click_time < 1000) {
                            TipUtils.showTip(getResources().getString(R.string.click_so_little));
                            File file = new File(voice_name);
                            file.delete();
                            return false;
                        }
                        isDowning = false;

                        break;
                }
            }
            return false;
        }
    }

    /**
     * 发送信息
     */
    public void sendMessageNet(String path, int lenthTime) {
        File file = new File(path);
        sendMessage(file,lenthTime+"");
    }

    /**
     * 开启录音
     */
    public void recordStartListener(final long click_time, final String filename) {
        if (click_time < 1000) {
            CountDownTimer cdt = new CountDownTimer(1000 - click_time, 1000 - click_time) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    if (isDowning) {
                        startRecord(filename);
                        timeDown();
                    }
                }
            };
            cdt.start();
        } else if (click_time > 1000) {
            if (isDowning) {
                startRecord(filename);
                timeDown();
            }
        }
    }


    /**
     * 结束录音
     */
    public void recordStopListener() {
        timer.cancel();
        maxTime = MAX_LENGTH;

        stopTimeM = System.currentTimeMillis();
        long endTimeM = System.currentTimeMillis();
        click_time = endTimeM - startTimeM;

        if (click_time < 1000) {
            CountDownTimer cdt = new CountDownTimer(1000 - click_time, 1000 - click_time) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    stopTimeM = System.currentTimeMillis();
                    stopRecord();
                }
            };
            cdt.start();
        } else if (click_time > 1000) {
            stopTimeM = System.currentTimeMillis();
            stopRecord();
            if (!isMove)
                sendMessageLocal(click_time);
            else {
                File file = new File(voice_name);
                file.delete();
            }
        }
    }

    /**
     * 计时器
     */
    public void timeDown() {
        timer = new CountDownTimer(maxTime, maxTime) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                send_button.setBackgroundResource(R.drawable.chat_send_button);
                send_button.setText(R.string.send_voice);
                chat_popup.setVisibility(View.GONE);
                send_button.setSelected(false);
                isUp = true;
                isDowning = false;
                recordStopListener();
            }
        }.start();
    }


    private Runnable mPollTask = new Runnable() {
        public void run() {
            volume.setImageResource(drawableAmp[(int) mSensor.getAmplitude()]);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };

    private void startRecord(String name) {
        mSensor.start(name);
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stopRecord() {
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        volume.setImageResource(drawableAmp[0]);
    }

    /**
     * 新增语音消息
     * @param click_time
     */
    public void sendMessageLocal(long click_time) {
        ChatMessage messagesBean = new ChatMessage();

        double timeLength = click_time < 1000 ? 1 : (click_time / 1000);
//        timeLength = Math.rint(timeLength);
        messagesBean.setVoice_length((int) timeLength);

        String time = getStampTime(DateTimeUtil.getNowTime("yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
        messagesBean.setCreate_time_double(Double.valueOf(time));

        messagesBean.setWatchId(Integer.parseInt(chatBean.getWatch_id()));
        messagesBean.setSend_type(1);
        messagesBean.setVoice_url(voice_name);
        messagesBean.setImg(SPHelper.getString(Builds.SP_USER,"image"));
        messagesBean.setIs_send(ChatListAdapter.UPDATING);

        messageList.add(0, messagesBean);

        adapter.notifyItemRangeInserted(0, 1);

        //这样刷新adapter中的list 并且不会出现卡顿
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 400);

        linerLayoutManager.scrollToPosition(0);

        sendMessageNet(voice_name, (int)timeLength);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                audioManager.adjustStreamVolume(
                        AudioManager.MODE_IN_COMMUNICATION,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                audioManager.adjustStreamVolume(
                        AudioManager.MODE_IN_COMMUNICATION,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefreshVoiceReceiver.unregister(this,refreshVoiceReceiver);
    }
}
