package com.yinaf.dragon.Content.Activity.family_set;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.google.gson.Gson;
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
import com.yinaf.dragon.Content.Activity.LoginAct;
import com.yinaf.dragon.Content.Activity.family_set.adapter.MemberSetAdapter;
import com.yinaf.dragon.Content.Activity.family_set.model.MemberSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.PoliceSetModel;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.Db.DatabaseHelper;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetMemberAPI;
import com.yinaf.dragon.Content.Net.ImageUploadAPI;
import com.yinaf.dragon.Content.Net.MemberSetAPI;
import com.yinaf.dragon.Content.Net.MemberUpdateSetAPI;
import com.yinaf.dragon.Content.Net.PhotoUploadAPI;
import com.yinaf.dragon.Content.Net.PoliceSetAPI;
import com.yinaf.dragon.Content.Net.WatchesSetAPI;
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
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.StringUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 成员资料设置
 */
public class MemberSetAct extends BasicAct implements MemberSetAPI.MemberSetListener
        , PhotoUploadAPI.ImageUploadListener, MemberUpdateSetAPI.MemberUpdateSetListener,
        TakePhoto.TakeResultListener,InvokeListener ,GetMemberAPI.GetMemberAPIListener{

    /**
     * 返回按钮
     */
    @BindView(R.id.img_back)
    ImageView imgBack;
    /**
     * 头像
     */
    @BindView(R.id.family_rounded_img)
    RoundedImageView familyRoundedImg;
    /**
     * 编号
     */
    @BindView(R.id.tv_number)
    TextView tvNumber;
    /**
     * 资料显示项
     */
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    /**
     * 图像加载框架
     */
    private ImageLoader imageLoader;
    /**
     * 资料列表项配适器
     */
    private MemberSetAdapter memberSetAdapter;
    /**
     * 照片存储位置
     */
    private String bitName = "";
    private int memberId;
    public static final int TAKEPHOTO = 1;// 拍照
    public static final int PHOTOCROP = 2; // 缩放
    public static final int PHOTORESULT = 3;// 结果
    //拍照时setDataType()方法中的type
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private LoadingDialog loadingDialog;
    private MemberSetModel model = new MemberSetModel();
    InvokeParam invokeParam;
    private CropOptions cropOptions;  //裁剪参数
    private CompressConfig compressConfig;  //压缩参数
    TakePhoto takePhoto;
    DatabaseHelper dbHelper;


    /**
     * 需要的权限
     */
    private static final String[] INITIAL_PERMS={
            Manifest.permission.CAMERA,
    };
    private static final int INITIAL_REQUEST=1337;


    public MemberSetAct() {
        super(R.layout.member_set_act, R.string.family_member_msg_set, 0
                , true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_blue);

    }

    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);
        //图像加载控件初始化
        imageLoader = App.getImageLoader();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //获取当前成员资料信息
        memberId = getIntent().getIntExtra("memberId", 0);

        if (!NetworkUtils.isNetworkConnect(getApplicationContext())) {
            String string = SPHelper.getString(Builds.SP_USER, memberId + "Member");
            if (string != null && !TextUtils.isEmpty(string)) {
                model = JSONUtils.parseJson(string, MemberSetModel.class);
                //基本资料数据初始化
                memberSetAdapter = new MemberSetAdapter(getApplicationContext(),
                        0, ConstantSet.getMemberList(model.getObj()));
                recyclerView.setAdapter(memberSetAdapter);

            } else {
                TipUtils.showTip("请开启网络!");
            }
        } else {

            loadingDialog.show();
            new MemberSetAPI(this, memberId);
        }
        dbHelper = new DatabaseHelper(this,SPHelper.getString(Builds.SP_USER, "userName"));

    }

    @OnClick({R.id.img_back,R.id.family_rounded_img})
    public void onClick(View vie) {
        switch (vie.getId()) {
            case R.id.img_back:
                RefreshMemberReceiver.send(this);
                finish();
                break;
            case R.id.family_rounded_img:
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
                    //        设置压缩参数
                    compressConfig=new CompressConfig.Builder().setMaxSize(50*1024).setMaxPixel(800).create();
                    takePhoto.onEnableCompress(compressConfig,false);  //设置为需要压缩
                    //头像选择
                    shouSeletePop();
                }
                break;
        }
    }

    private void shouSeletePop() {
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
        }, this, familyRoundedImg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == 101) {
            int position = data.getIntExtra("position", 0);
            String content = data.getStringExtra("content");
            String[] strings = memberSetAdapter.getDatas().get(position);
            strings[2] = content;
            memberSetAdapter.notifyItemChanged(position);

            SPHelper.save(Builds.SP_USER,memberId+"Member",
                    new Gson().toJson(ConstantSet.setMemberList(model,memberSetAdapter.getDatas())));

            loadingDialog.show();
            new GetMemberAPI(this);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);

    }


    /**
     * 上传头像
     *
     * @param file
     */
    private void postHeadPortrait(File file, String imageName) {
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

                        Log.e("postHeadPortrait    ", response);

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
                                new PhotoUploadAPI(MemberSetAct.this, url);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    public void memberSetSuccess(MemberSetModel drugSetModel) {
        model = drugSetModel;
        loadingDialog.dismiss();
        if (drugSetModel != null && drugSetModel.getObj() != null) {
            MemberSetModel.ObjBean obj = drugSetModel.getObj();
            //头像
            if (!TextUtils.isEmpty(obj.getImage())) {
                imageLoader.displayImage(obj.getImage(), familyRoundedImg);
            }
            //编号
            tvNumber.setText("编号: " + obj.getMemberNum());
            //资料生成列表项
            if (memberSetAdapter==null){
                memberSetAdapter =
                        new MemberSetAdapter(getApplicationContext(), 0, ConstantSet.getMemberList(obj));
                recyclerView.setAdapter(memberSetAdapter);
            }else {
                memberSetAdapter.getDatas().clear();
                memberSetAdapter.getDatas().addAll(ConstantSet.getMemberList(obj));
            }

            memberSetAdapter.notifyDataSetChanged();
            memberSetAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    selectMemberMsg(position);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }
    }

    /**
     * 选择成员信息进行填写
     *
     * @param position
     */
    private void selectMemberMsg(int position) {
        String[] strings = memberSetAdapter.getDatas().get(position);
        Intent intent;
        switch (position) {
            case 0://昵称填写
            case 1://真实姓名
            case 3://真实姓名
            case 4://家庭地址
            case 10://电话
            case 14://工作单位
            case 8://身高
            case 9://体重
            case 16://收入


                intent = new Intent(getApplicationContext(), MemberTextWriteAct.class);
                intent.putExtra("title", strings[1]);
                intent.putExtra("content", strings[2]);
                intent.putExtra("params", strings[0]);
                intent.putExtra("position", position);
                intent.putExtra("memberId", memberId);
                startActivityForResult(intent, 101);
                break;
            case 2://日期出生
            case 20://户籍地
            case 21://所在地
                intent = new Intent(getApplicationContext(), MemberListAct.class);

                intent.putExtra("title", strings[1]);
                intent.putExtra("content", strings[2]);
                intent.putExtra("params", strings[0]);
                intent.putExtra("position", position);
                intent.putExtra("memberId", memberId);
                startActivityForResult(intent, 101);
                break;
            case 5://性别
            case 6://血型
            case 7://民族
            case 12://婚姻
            case 13://工作情况
            case 15://职业
            case 17://文化
            case 18://居住
            case 19://子女距离
                intent = new Intent(getApplicationContext(), MemberSelectAct.class);

                intent.putExtra("title", strings[1]);
                intent.putExtra("content", strings[2]);
                intent.putExtra("params", strings[0]);
                intent.putExtra("position", position);
                intent.putExtra("memberId", memberId);
                startActivityForResult(intent, 101);
                break;
            case 11:
                intent = new Intent(getApplicationContext(), RelationSelectAct.class);
                intent.putExtra("type",1);
                intent.putExtra("title", strings[1]);
                intent.putExtra("content", strings[2]);
                intent.putExtra("params", strings[0]);
                intent.putExtra("position", position);
                intent.putExtra("memberId", memberId);
                startActivityForResult(intent, 101);
                break;

        }
    }


    @Override
    public void memberSetError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void imageUploadSuccess(String content) {

        loadingDialog.dismiss();
        new MemberUpdateSetAPI(this,memberId,"image",content);
        Member member = dbHelper.selectMemberByMemberId(memberId);
        member.setImage(content);
        dbHelper.updateMember(member);
        RefreshMemberReceiver.send(this);

    }

    @Override
    public void imageUploadError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void memberUpdateSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void memberUpdateSetSuccess() {

        TipUtils.showTip("设置成功");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getTakePhoto().onCreate(savedInstanceState);
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
        familyRoundedImg.setImageURI(Uri.fromFile(new File(photoPath)));
        loadingDialog.show();
        postHeadPortrait(new File(photoPath),new File(photoPath).getName());


    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.e("拍照1",result.toString());
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

    /**
     * 更新成员数据库内容
     * @param content
     */
    @Override
    public void getMemberSuccess(JSONArray content) {
        dbHelper.deleteMemberData();
        try {
            for (int i = 0 ; i < content.length() ; i++){
                JSONObject jsonObject = content.getJSONObject(i);
                if (dbHelper.isMemberExists(JSONUtils.getInt(jsonObject,"memberId"))){
                    //本地已存在此成员信息,则更新成员信息
                    Member member = dbHelper.selectMemberByMemberId(JSONUtils.getInt(jsonObject,"memberId"));
                    if (jsonObject.getString("watchId").equals("")){
                        member.setWatchId(-1);
                    }else {
                        member.setWatchId(JSONUtils.getInt(jsonObject,"watchId"));
                    }
                    member.setIsLead(JSONUtils.getInt(jsonObject,"isLead"));
                    member.setImage(JSONUtils.getString(jsonObject,"image"));
                    member.setRealName(JSONUtils.getString(jsonObject,"realName"));
                    member.setMemberNum(JSONUtils.getString(jsonObject,"memberNum"));
                    member.setRela(JSONUtils.getString(jsonObject,"rela"));
                    dbHelper.updateMember(member);

                }else {
                    //本地不存在此成员信息，则插入一条成员信息
                    Member member = new Member();
                    member.setMemberId(JSONUtils.getInt(jsonObject,"memberId"));
                    member.setWatchId(JSONUtils.getInt(jsonObject,"watchId"));
                    member.setIsLead(JSONUtils.getInt(jsonObject,"isLead"));
                    member.setImage(JSONUtils.getString(jsonObject,"image"));
                    member.setRealName(JSONUtils.getString(jsonObject,"realName"));
                    member.setMemberNum(JSONUtils.getString(jsonObject,"memberNum"));
                    member.setRela(JSONUtils.getString(jsonObject,"rela"));
                    dbHelper.insertNewMember(member);
                }
            }
            loadingDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMemberError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}


