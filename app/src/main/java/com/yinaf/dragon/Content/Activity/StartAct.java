package com.yinaf.dragon.Content.Activity;


import android.os.Bundle;
import android.widget.ImageView;

import com.idescout.sql.SqlScoutServer;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.Db.DatabaseHelper;
import com.yinaf.dragon.Content.Net.GetMemberAPI;
import com.yinaf.dragon.Content.Net.LoginAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by long on 2018-4-11.
 * 功能：启动页面
 */


public class StartAct extends BasicAct implements LoginAPI.LoginAPIListener,GetMemberAPI.GetMemberAPIListener{

    @BindView(R.id.start_iv)
    ImageView startIv;

    private DatabaseHelper dbHelper;
    public String phone;//手机号
    public String pwd;//密码
    public String sessionId;//sessionId

    public StartAct() {
        super(R.layout.act_start, R.string.title_activity_start, false,TOOLBAR_TYPE_FULL_SCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SqlScoutServer.create(this, getPackageName());
    }

    @Override
    public void initView() {
        phone = SPHelper.getString(Builds.SP_USER, "userName");
        pwd = SPHelper.getString(Builds.SP_USER, "passWord");
        sessionId = SPHelper.getString(Builds.SP_USER, "sessionId");
        dbHelper = new DatabaseHelper(this,SPHelper.getString(Builds.SP_USER, "userName"));
        new LoginAPI(this,phone,sessionId,pwd);

    }


    @OnClick(R.id.start_iv)
    public void onViewClicked() {
        LoginAct.startActivity(this);
        finish();
    }

    /**
     * 登陆接口
     * @param content
     */
    @Override
    public void loginSuccess(JSONObject content) {

        String sessionId = JSONUtils.getString(content, "sessionId");
        String userName = JSONUtils.getString(content, "userName");
        String realName = JSONUtils.getString(content, "realName");
        String image = JSONUtils.getString(content, "image");
        String userId = JSONUtils.getString(content, "userId");
        SPHelper.save(Builds.SP_USER,"userId",userId);//用户ID
        SPHelper.save(Builds.SP_USER,"userName",userName);//用户名
        SPHelper.save(Builds.SP_USER,"sessionId",sessionId);//sessionId
        SPHelper.save(Builds.SP_USER,"realName",realName);//昵称
        SPHelper.save(Builds.SP_USER,"image",image);//头像图片地址
        SPHelper.save(Builds.SP_USER,"passWord",pwd);//密码
        dbHelper = new DatabaseHelper(this,SPHelper.getString(Builds.SP_USER, "userName"));
        new GetMemberAPI(this);
    }

    @Override
    public void loginError(long code, String msg) {
        LoginAct.startActivity(this);
        finish();
    }

    /**
     * 获取用户的所有成员
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
            HomeAct.startActivity(this);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMemberError(long code, String msg) {
        LoginAct.startActivity(this);
        finish();
    }
}
























