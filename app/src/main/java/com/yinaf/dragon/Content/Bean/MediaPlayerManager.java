package com.yinaf.dragon.Content.Bean;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.yinaf.dragon.Content.Adapter.ChatListAdapter;
import com.yinaf.dragon.Content.Utils.VoicePlayingUtil;
import com.yinaf.dragon.R;

import java.io.IOException;

import static com.yinaf.dragon.Content.Adapter.ChatListAdapter.WATCH_CLICK;
import static com.yinaf.dragon.Content.Utils.VoicePlayingUtil.voicePlayInstance;


/**
 * Created by long on 18/5/10.
 *
 */

public class MediaPlayerManager {
    private MediaPlayer player;
    private boolean isPlaying;//是否正在播放
    private Handler mHandler;
    private VoicePlayingUtil voiceUtil;
    private Context context;
    private int playPosition=-1;//正在播的条目
    private TextView voiceTv;

    public MediaPlayerManager(Context context,TextView voiceTv) {
        this.context=context;
        this.voiceTv=voiceTv;
    }

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    /**
     * 通过url或文件播放声音
     * <p>
     * 如果正在播放  停止后mp置为空 重新new
     * 因为mp是native上面的对象  native上面为空了  本地还不为空
     * <p>
     * 参考 http://lovelease.iteye.com/blog/2105616
     */
    public void startPlay(View v, int type, final ChatMessage bean, int position) {
        RelativeLayout relativeLayout = (RelativeLayout) v;
        voiceUtil = voicePlayInstance(mHandler);

        if (player == null) {
            player = new MediaPlayer();
        } else {
            player.reset();
        }

        if (isPlaying) {
            player.stop();
            player.release();
            player = null;
            voiceTv.setVisibility(View.GONE);
            voiceUtil.stopPlay();
            isPlaying = false;
            if (position != playPosition)
                startPlay(v, type, bean, position);
            return;
        }
        playPosition = position;

        if (type == ChatListAdapter.USER_CLICK) {
            voiceUtil.setModelType(ChatListAdapter.USER_CLICK);
        } else {
            voiceUtil.setModelType(WATCH_CLICK);
        }

        voiceUtil.setImageView((ImageView) relativeLayout.getChildAt(0));
        voiceUtil.setLength(bean.getVoice_length());

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
                player = null;
                isPlaying = false;
                voiceUtil.stopPlay();
                voiceTv.setVisibility(View.GONE);
            }
        });
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                voiceTv.setVisibility(View.VISIBLE);
                voiceUtil.voicePlay();
                isPlaying = true;
            }
        });
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //直接播放文件
        if(bean.getVoice_url()!=null){
            try {
                player.setDataSource(bean.getVoice_url());
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, R.string.url_is_error,Toast.LENGTH_LONG).show();
                voiceTv.setVisibility(View.GONE);
                voiceUtil.stopPlay();
            }
        }else {
            AudioFileManager.getInstance().getSoundFile(context, bean.getVoice_url(),
                    new AudioFileManager.GetFileListener(){
                @Override
                public void onSussess(String filePath) {
                    // 直接播放缓存文件
                    try {
                        bean.setVoice_url(filePath);
                        player.setDataSource(filePath); // 设置数据源
                        player.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context,R.string.url_is_error,Toast.LENGTH_LONG).show();
                        voiceTv.setVisibility(View.GONE);
                        voiceUtil.stopPlay();
                    }
                }

                @Override
                public void onFail(String reason) {
                    try {
                        player.setDataSource(bean.getVoice_url());
                        player.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, R.string.url_is_error,Toast.LENGTH_LONG).show();
                        voiceTv.setVisibility(View.GONE);
                        voiceUtil.stopPlay();
                    }
                }
            });
        }

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                player.reset();
                isPlaying = false;
                return false;
            }
        });
    }

    public void stopPlay(){
        if (player == null)
            return;
        if (player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
            voiceTv.setVisibility(View.GONE);
            voiceUtil.stopPlay();
        }
    }
}
