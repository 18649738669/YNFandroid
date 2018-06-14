package com.yinaf.dragon.Content.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yinaf.dragon.Content.Activity.family_set.widget.CustomDatePicker;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.CustomUi.CustomRadioGroup;
import com.yinaf.dragon.Content.CustomUi.CustomViewPager;
import com.yinaf.dragon.Content.Db.DatabaseHelper;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.AddMemberAPI;
import com.yinaf.dragon.Content.Net.ImageUploadAPI;
import com.yinaf.dragon.Content.PopupWindow.PhotoPopup;
import com.yinaf.dragon.Content.Receiver.RefreshMemberReceiver;
import com.yinaf.dragon.Content.Utils.ActivityCollector;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.App;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.FileUtils;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.StringUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by long on 2018/4/21.
 * 功能：新增家人页面
 */

public class AddFamilyAct extends BasicAct implements ImageUploadAPI.ImageUploadListener,
        AddMemberAPI.AddMemberListener,TakePhoto.TakeResultListener,InvokeListener {

    @BindView(R.id.tool_bar_btn_back)
    RelativeLayout toolBarBtnBack;
    @BindView(R.id.tool_bar_title)
    TextView toolBarTitle;
    @BindView(R.id.add_family_vp)
    CustomViewPager addFamilyVp;
    @BindView(R.id.add_family_tv_previous_step)
    TextView addFamilyTvPreviousStep;
    @BindView(R.id.add_family_tv_next_step)
    TextView addFamilyTvNextStep;

    CustomRadioGroup addFamilyRg;//与用户关系选择容器
    ImageButton addFamilyIvMale;//男 按钮
    ImageButton addFamilyIvFemale;//女 按钮
    TextView addFamilyTvBirthday;//日期选择器的显示
    TextView addFamilyTvHeight;//显示身高
    SeekBar addFamilySbHeight;//身高进度条
    TextView addFamilyTvWeight;//显示体重
    SeekBar addFamilySbWeight;//体重进度条
    ImageButton addFamilyIvBloodA;//A型血按钮
    ImageButton addFamilyIvBloodB;//B型血按钮
    ImageButton addFamilyIvBloodAb;//Ab型血按钮
    ImageButton addFamilyIvBloodO;//O型血按钮
    ImageButton addFamilyIvBloodOther;//其他型血按钮
    RoundedImageView addFamilyRoundedImg;//圆形头像
    EditText addFamilyEtRealName;//昵称
    CustomDatePicker customDatePicker;


    List<View> pagerViewList = new ArrayList<View>(); //所需展示的页面集合
    int vp_index = 0;//当前加载的页面下标

    DatabaseHelper dbHelper;
    Member member;
    int sex = 0;//性别
    String birthday = "";//出生日期
    int height = 30;//身高
    int weight = 40;//体重
    String blood = "";//血型
    String realName = "";//昵称
    public static final int TAKEPHOTO = 1;// 拍照
    public static final int PHOTOCROP = 2; // 缩放
    public static final int PHOTORESULT = 3;// 结果
    //拍照时setDataType()方法中的type
    public static final String IMAGE_UNSPECIFIED = "image/*";

    private ImageLoader imageLoader;
    String bitName = "";
    LoadingDialog loadingDialog;

    InvokeParam invokeParam;
    private CropOptions cropOptions;  //裁剪参数
    private CompressConfig compressConfig;  //压缩参数
    TakePhoto takePhoto;

    /**
     * 需要的权限
     */
    private static final String[] INITIAL_PERMS={
            Manifest.permission.CAMERA,
    };
    private static final int INITIAL_REQUEST=1337;




    public AddFamilyAct() {
        super(R.layout.act_add_family, R.string.title_activity_add_family, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_blue);

    }

    public static void startActivity(Context context,int isOne) {
        Intent intent = new Intent(context, AddFamilyAct.class);
        intent.putExtra("isOne",isOne);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        loadingDialog = LoadingDialog.showDialog(this);
        imageLoader = App.getImageLoader();
        toolBarTitle.setText(R.string.add_family_title_rela);
        member = new Member();
        dbHelper = new DatabaseHelper(this, SPHelper.getString(Builds.SP_USER, "userName"));

        if (vp_index == 0){
            addFamilyTvPreviousStep.setVisibility(View.GONE);
        }

        //初始化ViewPager
        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater = getLayoutInflater();
        //获取与用户关系页面
        View vp_rela = inflater.inflate(R.layout.vp_add_family_rela, null);
        addFamilyRg = vp_rela.findViewById(R.id.add_family_rg);
        //获取性别页面
        View vp_sex = inflater.inflate(R.layout.vp_add_family_sex,null);
        addFamilyIvMale = vp_sex.findViewById(R.id.add_family_iv_male);
        addFamilyIvFemale = vp_sex.findViewById(R.id.add_family_iv_female);
        //获取出生日期页面
        View vp_birthday = inflater.inflate(R.layout.vp_add_family_birthday,null);
        addFamilyTvBirthday = vp_birthday.findViewById(R.id.add_family_tv_birthday);
        //获取身高页面
        View vp_height = inflater.inflate(R.layout.vp_add_family_height,null);
        addFamilyTvHeight = vp_height.findViewById(R.id.add_family_tv_height);
        addFamilySbHeight = vp_height.findViewById(R.id.add_family_sb_height);
        //获取体重页面
        View vp_weight = inflater.inflate(R.layout.vp_add_family_weight,null);
        addFamilyTvWeight = vp_weight.findViewById(R.id.add_family_tv_weight);
        addFamilySbWeight = vp_weight.findViewById(R.id.add_family_sb_weight);
        //获取血型页面
        View vp_blood = inflater.inflate(R.layout.vp_add_family_blood,null);
        addFamilyIvBloodA = vp_blood.findViewById(R.id.add_family_iv_blood_a);
        addFamilyIvBloodB = vp_blood.findViewById(R.id.add_family_iv_blood_b);
        addFamilyIvBloodAb = vp_blood.findViewById(R.id.add_family_iv_blood_ab);
        addFamilyIvBloodO = vp_blood.findViewById(R.id.add_family_iv_blood_o);
        addFamilyIvBloodOther = vp_blood.findViewById(R.id.add_family_iv_blood_other);
        //获取昵称页面
        View vp_real_name = inflater.inflate(R.layout.vp_add_family_real_name,null);
        addFamilyRoundedImg = vp_real_name.findViewById(R.id.add_family_rounded_img);
        addFamilyEtRealName = vp_real_name.findViewById(R.id.add_family_et_real_name);



        initViewRela();
        initViewSex();

        pagerViewList.add(vp_rela);
        pagerViewList.add(vp_sex);
        pagerViewList.add(vp_birthday);
        pagerViewList.add(vp_height);
        pagerViewList.add(vp_weight);
        pagerViewList.add(vp_blood);
        pagerViewList.add(vp_real_name);
        addFamilyVp.setAdapter(pagerAdapter);
        addFamilyVp.setCurrentItem(vp_index);




    }

    /**
     * 初始化输入昵称页面
     */
    private void initViewRealName(){

        addFamilyEtRealName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                realName = addFamilyEtRealName.getText().toString();
                return false;
            }
        });

        addFamilyRoundedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean permission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED ;

                if (!permission ) {

                    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                        requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
                    }else{
                        TipUtils.showTip("请先在手机的设置中心，设置允许访问相机、相册的权限");
                    }
                }else {
                    //获取TakePhoto实例
                    takePhoto = getTakePhoto();
                    //设置裁剪参数
                    cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(false).create();
                    //设置压缩参数
                    compressConfig=new CompressConfig.Builder().setMaxSize(50*1024).setMaxPixel(800).create();
                    takePhoto.onEnableCompress(compressConfig,true);  //设置为需要压缩
                    PhotoPopup.show(new PhotoPopup.PhotoListener() {
                        @Override
                        public void getCameraPhoto() {
                            boolean isSDCardExit = Environment.MEDIA_MOUNTED.equals(Environment
                                    .getExternalStorageState());
                            if (isSDCardExit) {
                                File file = new File(Builds.IMAGE_PATH,bitName = StringUtils.getImageNameFromDate() + ".jpg");
                                if (!file.getParentFile().exists()) {
                                    file.getParentFile().mkdirs();
                                }
                                Uri imageUri = Uri.fromFile(file);
                                takePhoto.onPickFromCapture(imageUri);
                            } else {
                                TipUtils.showTip("请插入sd卡");
                            }
                        }

                        @Override
                        public void getGalleyPhoto() {
                            takePhoto.onPickFromGallery();
                        }
                    }, AddFamilyAct.this, addFamilyRoundedImg);
                }
            }
        });

    }


    /**
     * 上传头像
     * @param file
     */
    private void postHeadPortrait(File file,String imageName){
        loadingDialog.show();
        OkHttpUtils
                .post()
                .addFile("file", imageName, file)//
                .url(Builds.HOST + "/file/fileUpload")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Log.e("postHeadPortrait    ",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!JSONUtils.getBoolean(jsonObject,"success")){
                                loadingDialog.dismiss();
                            }
                            if (JSONUtils.getInt(jsonObject,"code") == 5107){
                                ActivityCollector.finishAll();
                                LoginAct.startNewLoginAct();
                                TipUtils.showTip(JSONUtils.getString(jsonObject,"msg"));
                            }else {
                                JSONObject object = JSONUtils.getJSONObject(jsonObject, "obj");
                                String url = JSONUtils.getString(object, "url");
                                new ImageUploadAPI(AddFamilyAct.this, url);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);

    }


    /**
     * 初始化选择血型页面
     */
    private void initViewBlood(){
        addFamilyIvBloodA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blood = "A型";
                addFamilyIvBloodA.setImageResource(R.drawable.choose_yes);
                addFamilyIvBloodB.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodAb.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodO.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodOther.setImageResource(R.drawable.circle_no);
            }
        });
        addFamilyIvBloodB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blood = "B型";
                addFamilyIvBloodA.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodB.setImageResource(R.drawable.choose_yes);
                addFamilyIvBloodAb.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodO.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodOther.setImageResource(R.drawable.circle_no);
            }
        });
        addFamilyIvBloodAb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blood = "Ab型";
                addFamilyIvBloodA.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodB.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodAb.setImageResource(R.drawable.choose_yes);
                addFamilyIvBloodO.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodOther.setImageResource(R.drawable.circle_no);
            }
        });
        addFamilyIvBloodO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blood = "O型";
                addFamilyIvBloodA.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodB.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodAb.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodO.setImageResource(R.drawable.choose_yes);
                addFamilyIvBloodOther.setImageResource(R.drawable.circle_no);
            }
        });
        addFamilyIvBloodOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blood = "其他";
                addFamilyIvBloodA.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodB.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodAb.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodO.setImageResource(R.drawable.choose_no);
                addFamilyIvBloodOther.setImageResource(R.drawable.circle_yes);
            }
        });
    }

    /**
     * 初始化身高页面
     */
    private void initViewHeight(){
        addFamilySbHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //拖动进度条
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                addFamilyTvHeight.setText((progress + 140)+"CM");
                height = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addFamilySbHeight.setProgress(height);
    }

    /**
     * 初始化体重页面
     */
    private void initViewWeight(){
        addFamilySbWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //拖动进度条
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                addFamilyTvWeight.setText((progress + 10)+"KG");
                weight = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addFamilySbWeight.setProgress(weight);
    }

    /**
     * 初始化出生日期页面
     */
    private void initViewBirthday() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (birthday.equals("")){
            addFamilyTvBirthday.setText(format.format(date));
        }
        initDatePicker();
        addFamilyTvBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDatePicker.show(addFamilyTvBirthday.getText().toString());
            }
        });

    }

    /**
     * 开始提醒和结束提醒的时间选择器初始化
     */
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        addFamilyTvBirthday.setText(now.split(" ")[0]);

        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间

                addFamilyTvBirthday.setText(time.split(" ")[0]);
            }
        }, "1900-01-01 00:00", "2100-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(true); // 不允许循环滚动


    }

    /**
     * 初始化性别页面
     */
    private void initViewSex() {
        //选中性别男
        addFamilyIvMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = 1;
                addFamilyIvMale.setImageResource(R.drawable.choose_yes);
                addFamilyIvFemale.setImageResource(R.drawable.choose_no);
            }
        });
        //选中性别女
        addFamilyIvFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = 2;
                addFamilyIvMale.setImageResource(R.drawable.choose_no);
                addFamilyIvFemale.setImageResource(R.drawable.choose_yes);
            }
        });

    }

    /**
     * 初始化与用户关系页面
     */
    private void initViewRela() {

        List<String> list = new ArrayList<String>();
        list.add("父亲");
        list.add("母亲");
        list.add("丈夫");
        list.add("妻子");
        list.add("爷爷");
        list.add("奶奶");
        list.add("外公");
        list.add("外婆");
        list.add("儿子");
        list.add("女儿");
        list.add("亲戚");
        list.add("朋友");
        list.add("其他");
        list.add("本人");

        addFamilyRg.setColumn(3);//设置列数
        addFamilyRg.setValues(list);//设置记录列表
        addFamilyRg.setView(this);//设置视图

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        getTakePhoto().onCreate(savedInstanceState);
    }

    @OnClick({R.id.tool_bar_btn_back, R.id.add_family_tv_previous_step, R.id.add_family_tv_next_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_btn_back:
                if (getIntent().getIntExtra("isOne",0) == 0){
                    finish();
                }else {
                    HomeAct.startActivity(this);
                }
                break;
            case R.id.add_family_tv_previous_step://上一步
                if (vp_index > 0 ){
                    vp_index -= 1;
                }
                if (vp_index != 0){
                    toolBarBtnBack.setVisibility(View.GONE);
                    addFamilyTvPreviousStep.setVisibility(View.VISIBLE);
                }else {
                    toolBarBtnBack.setVisibility(View.VISIBLE);
                    addFamilyTvPreviousStep.setVisibility(View.GONE);
                }
                if (vp_index != 6){
                    addFamilyTvNextStep.setText("下一步");
                }else {
                    addFamilyTvNextStep.setText("完成");
                }
                addFamilyVp.setCurrentItem(vp_index);

                switch (vp_index){
                    case 0:
                        toolBarTitle.setText(R.string.add_family_title_rela);
//                        initViewRela();
                        break;
                    case 1:
                        toolBarTitle.setText(R.string.add_family_title_sex);
                        initViewSex();

                        break;
                    case 2:
                        toolBarTitle.setText(R.string.add_family_title_birthday);
                        initViewBirthday();
                        break;
                    case 3:
                        toolBarTitle.setText(R.string.add_family_title_height);
                        initViewHeight();
                        break;
                    case 4:
                        toolBarTitle.setText(R.string.add_family_title_weight);
                        initViewWeight();
                        break;
                    case 5:
                        toolBarTitle.setText(R.string.add_family_title_blood);
                        initViewBlood();
                        break;
                    case 6:
                        toolBarTitle.setText(R.string.add_family_title_real_name);
                        initViewRealName();
                        break;
                }
                break;
            case R.id.add_family_tv_next_step://下一步
                member.setRela(addFamilyRg.getCurrentValue());//获取当前被选择的按钮值
                member.setSex(sex);
                member.setBirthday(addFamilyTvBirthday.getText().toString());
                member.setHeight((height + 140)+"");
                member.setWeight((weight + 10)+"");
                member.setBlood(blood);
                member.setRealName(addFamilyEtRealName.getText().toString());
                if (vp_index == 0 && member.getRela().equals("")){
                    TipUtils.showTip("请选择与用户的关系！");
                    return;
                }
                if (vp_index == 1 && member.getSex() == 0){
                    TipUtils.showTip("请选择性别！");
                    return;
                }
                if (vp_index == 3 && member.getHeight().equals("")){
                    TipUtils.showTip("请选择身高！");
                    return;
                }
                if (vp_index == 4 && member.getWeight().equals("")){
                    TipUtils.showTip("请选择体重！");
                    return;
                }
                if (vp_index == 5 && member.getBlood().equals("")){
                    TipUtils.showTip("请选择血型！");
                    return;
                }
                if (vp_index == 6 && (addFamilyEtRealName.getText() == null || addFamilyEtRealName.getText().toString().equals(""))){
                    TipUtils.showTip("请输入昵称！");
                    return;
                }
                if (vp_index < 6){
                    vp_index += 1;
                }
                if (vp_index != 0){
                    toolBarBtnBack.setVisibility(View.GONE);
                    addFamilyTvPreviousStep.setVisibility(View.VISIBLE);
                }else {
                    toolBarBtnBack.setVisibility(View.VISIBLE);
                    addFamilyTvPreviousStep.setVisibility(View.GONE);
                }
                if (addFamilyTvNextStep.getText().equals("完成") && member.getSex() != 0 && member.getBirthday() != ""
                        && member.getImage()!= "" && member.getBlood()!= "" && member.getRela() != "" && member.getHeight() != ""
                        && member.getWeight() != "" && member.getRealName() != ""){
                    member.setRealName(addFamilyEtRealName.getText().toString());
                    loadingDialog.show();
                    new AddMemberAPI(this,member.getSex(),member.getBirthday(),member.getImage(),member.getBlood(),
                            member.getRela(),member.getHeight(),member.getWeight(),member.getRealName());
                }
                if (vp_index < 6){
                    addFamilyTvNextStep.setText("下一步");
                }else {
                    addFamilyTvNextStep.setText("完成");
                }

                if (pagerViewList.size() > vp_index) {
                    addFamilyVp.setCurrentItem(vp_index);
                }
                switch (vp_index){
                    case 0:
                        toolBarTitle.setText(R.string.add_family_title_rela);
                        initViewRela();

                        break;
                    case 1:
                        toolBarTitle.setText(R.string.add_family_title_sex);
                        initViewSex();
                        break;
                    case 2:
                        toolBarTitle.setText(R.string.add_family_title_birthday);
                        initViewBirthday();
                        break;
                    case 3:
                        toolBarTitle.setText(R.string.add_family_title_height);
                        initViewHeight();
                        break;
                    case 4:
                        toolBarTitle.setText(R.string.add_family_title_weight);
                        initViewWeight();
                        break;
                    case 5:
                        toolBarTitle.setText(R.string.add_family_title_blood);
                        initViewBlood();
                        break;
                    case 6:
                        toolBarTitle.setText(R.string.add_family_title_real_name);
                        initViewRealName();
                        break;
                }
                break;
        }
    }

    /**
     * viewpager 的适配器
     */
    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        //获取当前窗体界面数
        public int getCount() {
            return pagerViewList.size();
        }

        @Override
        //判断是否由对象生成界面
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(pagerViewList.get(position));
            return pagerViewList.get(position);

        }
        @Override
        //使从ViewGroup中移出当前View
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pagerViewList.get(position));
        }
    };

    /**
     * 上传临时云后转正式云接口
     * @param content
     */
    @Override
    public void imageUploadSuccess(JSONArray content) {

        loadingDialog.dismiss();
        try {
            for (int i = 0 ; i < content.length() ; i++){
                JSONObject jsonObject = content.getJSONObject(i);
                String url = JSONUtils.getString(jsonObject,"url");
                member.setImage(url);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void imageUploadError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 新增成员接口
     * @param content
     */
    @Override
    public void addMemberSuccess(JSONArray content) {

        loadingDialog.dismiss();
        TipUtils.showTip("新增成员成功！");
        RefreshMemberReceiver.send(this);
        if (getIntent().getIntExtra("isOne",0) == 0){
            finish();
        }else {
            HomeAct.startActivity(this);
        }

    }

    @Override
    public void addMemberError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
    }

    @Override
    public void takeSuccess(TResult result) {

        Log.e("拍照",result.getImage().getCompressPath());
        String photoPath = result.getImage().getCompressPath();
        addFamilyRoundedImg.setImageURI(Uri.fromFile(new File(photoPath)));
        loadingDialog.show();
        postHeadPortrait(new File(photoPath),new File(photoPath).getName());


    }

    @Override
    public void takeFail(TResult result, String msg) {
        TipUtils.showTip(msg);
    }

    @Override
    public void takeCancel() {

    }



    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }

}
