package com.yinaf.dragon.Content.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.yinaf.dragon.Content.Activity.family_set.ConstantSet;
import com.yinaf.dragon.Content.Activity.family_set.MemberListAct;
import com.yinaf.dragon.Content.Activity.family_set.MemberSelectAct;
import com.yinaf.dragon.Content.Activity.family_set.MemberTextWriteAct;
import com.yinaf.dragon.Content.Activity.family_set.adapter.MemberSetAdapter;
import com.yinaf.dragon.Content.Bean.MySettingModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.MySettingAPI;
import com.yinaf.dragon.Content.Net.MySettingUpdateSetAPI;
import com.yinaf.dragon.Content.Net.PhotoUploadAPI;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 功能：用户的基本资料设置
 */
public class MySettingAct extends BasicAct implements  PhotoUploadAPI.ImageUploadListener
        , MySettingAPI.MySettingListener, MySettingUpdateSetAPI.MySettingUpdateSetListener,
        TakePhoto.TakeResultListener,InvokeListener {

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
    public static final int TAKEPHOTO = 1;// 拍照
    public static final int PHOTOCROP = 2; // 缩放
    public static final int PHOTORESULT = 3;// 结果
    //拍照时setDataType()方法中的type
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private LoadingDialog loadingDialog;
    private MySettingModel model = new MySettingModel();

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

    public MySettingAct() {
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

        if (!NetworkUtils.isNetworkConnect(getApplicationContext())) {
            String string = SPHelper.getString(Builds.SP_USER, "Setting");
            if (string != null && !TextUtils.isEmpty(string)) {
                model = JSONUtils.parseJson(string, MySettingModel.class);
                //基本资料数据初始化
                memberSetAdapter = new MemberSetAdapter(getApplicationContext(),
                        0, ConstantSet. getUserList(model.getObj()));
                recyclerView.setAdapter(memberSetAdapter);

            } else {
                TipUtils.showTip("请开启网络!");
            }
        } else {

            loadingDialog.show();
            new MySettingAPI(this);
        }

    }

    @OnClick({R.id.img_back,R.id.family_rounded_img})
    public void onClick(View vie) {
        switch (vie.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.family_rounded_img:
                boolean permission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED ;

                if (!permission ) {

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
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
            if (position == 0){
                SPHelper.save(Builds.SP_USER,"realName",content);
                RefreshMemberReceiver.send(this);

            }
            String[] strings = memberSetAdapter.getDatas().get(position);
            strings[2] = content;
            memberSetAdapter.notifyItemChanged(position);

            SPHelper.save(Builds.SP_USER,"Setting",
                    new Gson().toJson(ConstantSet.setUserList(model,memberSetAdapter.getDatas())));

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
                                new PhotoUploadAPI(MySettingAct.this, url);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
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
            case 5://电话
            case 4://邮箱


                intent = new Intent(getApplicationContext(), MemberTextWriteAct.class);
                intent.putExtra("title", strings[1]);
                intent.putExtra("content", strings[2]);
                intent.putExtra("params", strings[0]);
                intent.putExtra("position", position);
                intent.putExtra("memberId", -1);
                startActivityForResult(intent, 101);
                break;
            case 2://日期出生
            case 7://户籍地
            case 8://所在地
                intent = new Intent(getApplicationContext(), MemberListAct.class);

                intent.putExtra("title", strings[1]);
                intent.putExtra("content", strings[2]);
                intent.putExtra("params", strings[0]);
                intent.putExtra("position", position);
                intent.putExtra("memberId", -1);
                startActivityForResult(intent, 101);
                break;
            case 1://性别
            case 3://血型
            case 6://职业
                intent = new Intent(getApplicationContext(), MemberSelectAct.class);

                intent.putExtra("title", strings[1]);
                intent.putExtra("content", strings[2]);
                intent.putExtra("params", strings[0]);
                intent.putExtra("position", position);
                intent.putExtra("memberId", -1);
                startActivityForResult(intent, 101);
                break;


        }
    }


    @Override
    public void memberSetSuccess(MySettingModel drugSetModel) {
        model = drugSetModel;
        loadingDialog.dismiss();
        if (drugSetModel != null && drugSetModel.getObj() != null) {
            MySettingModel.ObjBean obj = drugSetModel.getObj();
            //头像
            if (!TextUtils.isEmpty(obj.getImage())) {
                imageLoader.displayImage(obj.getImage(), familyRoundedImg);
            }
            //编号
            tvNumber.setText("账号: " + obj. getPhone());
            //资料生成列表项
            if (memberSetAdapter==null){
                memberSetAdapter =
                        new MemberSetAdapter(getApplicationContext(), 0, ConstantSet.getUserList(obj));
                recyclerView.setAdapter(memberSetAdapter);
            }else {
                memberSetAdapter.getDatas().clear();
                memberSetAdapter.getDatas().addAll(ConstantSet.getUserList(obj));
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

    @Override
    public void memberSetError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void imageUploadSuccess(String content) {

        SPHelper.save(Builds.SP_USER,"image",content);
        loadingDialog.dismiss();

        new MySettingUpdateSetAPI(this,"image",content);

    }

    @Override
    public void imageUploadError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void mySettingUpdateSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void mySettingUpdateSetSuccess() {

        RefreshMemberReceiver.send(this);
        TipUtils.showTip("设置成功");
        loadingDialog.dismiss();

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

}
