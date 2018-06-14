package com.yinaf.dragon.Content.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yinaf.dragon.Content.Bean.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long on 2018-4-17.
 * 功能：sqlite数据库
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    /**
     * 成员表
     * memberId 成员主键 watchId 腕表ID memberNum 成员编号 address 地址
     * rela 与用户关系 sex 性别 birthday 生日 blood 血型 createTime 创建时间
     * distance 与子女距离  domicile 所在地 education 学历 householdRegister 户籍地
     * height 身高 identity 身份证号 image 头像 income 收入情况 job 工作情况
     * liveState 居住情况 marriage 婚姻状况 nation 民族 occupation职业 phone 手机
     * realName 昵称 telephone 固定电话 trueName 真实姓名 unit 工作单位 updateTime 修改时间
     * weight 体重 isLead 是否主副
     */
    private static final String CREATE_MEMBER = "create table member (" +
            "memberId integer primary key," +
            "watchId integer ," +
            "memberNum text ," +
            "address text ," +
            "rela text ," +
            "sex integer ," +
            "birthday text ," +
            "blood text ," +
            "createTime text ," +
            "distance text ," +
            "domicile text ," +
            "education text ," +
            "householdRegister text ," +
            "height text ," +
            "identity text ," +
            "image text ," +
            "income text ," +
            "job text ," +
            "liveState text ," +
            "marriage text ," +
            "nation text ," +
            "occupation text ," +
            "phone text ," +
            "realName text ," +
            "telephone text ," +
            "trueName text ," +
            "unit text ," +
            "updateTime text ," +
            "weight text ," +
            "isLead integer)";



    public DatabaseHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    /**
     * 创建数据库
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_MEMBER);  //成员表

    }

    /**
     * 更新数据库
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion){
            case 2:
                break;
            default: break;
        }

    }

    /**
     * --------------------------------成员表操作----------------------------------------------------
     */

    /**
     * 新增一个成员数据
     */
    public void insertNewMember(Member newMember){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("memberId", newMember.getMemberId());
        values.put("watchId", newMember.getWatchId());
        values.put("memberNum", newMember.getMemberNum());
        values.put("address", newMember.getAddress());
        values.put("rela", newMember.getRela());
        values.put("sex", newMember.getSex());
        values.put("birthday", newMember.getBirthday());
        values.put("blood", newMember.getBlood());
        values.put("createTime", newMember.getCreateTime());
        values.put("distance", newMember.getDistance());
        values.put("domicile", newMember.getDomicile());
        values.put("education", newMember.getEducation());
        values.put("householdRegister", newMember.getHouseholdRegister());
        values.put("height", newMember.getHeight());
        values.put("identity", newMember.getIdentity());
        values.put("image", newMember.getImage());
        values.put("income", newMember.getIncome());
        values.put("job", newMember.getJob());
        values.put("liveState", newMember.getLiveState());
        values.put("marriage", newMember.getMarriage());
        values.put("nation", newMember.getNation());
        values.put("occupation", newMember.getOccupation());
        values.put("phone", newMember.getPhone());
        values.put("realName", newMember.getRealName());
        values.put("telephone", newMember.getTelephone());
        values.put("trueName", newMember.getTrueName());
        values.put("unit", newMember.getUnit());
        values.put("updateTime", newMember.getUpdateTime());
        values.put("weight", newMember.getWeight());
        values.put("isLead", newMember.getIsLead());
        db.insert("member", null, values);

    }

    /**
     * 搜索数据库中所有的Member，按照createTime升序排列
     */
    public List<Member> selectAllMember() {
        List<Member> memberList = new ArrayList<Member>();
        Member member = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("member", null, null, null, null, null, "createTime desc");
        if (cursor.moveToFirst()) {
            do {
                member = new Member();
                member.setMemberId(cursor.getInt(cursor.getColumnIndex("memberId")));
                member.setWatchId(cursor.getInt(cursor.getColumnIndex("watchId")));
                member.setMemberNum(cursor.getString(cursor.getColumnIndex("memberNum")));
                member.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                member.setRela(cursor.getString(cursor.getColumnIndex("rela")));
                member.setSex(cursor.getInt(cursor.getColumnIndex("sex")));
                member.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
                member.setBlood(cursor.getString(cursor.getColumnIndex("blood")));
                member.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
                member.setDistance(cursor.getString(cursor.getColumnIndex("distance")));
                member.setDomicile(cursor.getString(cursor.getColumnIndex("domicile")));
                member.setEducation(cursor.getString(cursor.getColumnIndex("education")));
                member.setHouseholdRegister(cursor.getString(cursor.getColumnIndex("householdRegister")));
                member.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                member.setIdentity(cursor.getString(cursor.getColumnIndex("identity")));
                member.setImage(cursor.getString(cursor.getColumnIndex("image")));
                member.setIncome(cursor.getString(cursor.getColumnIndex("income")));
                member.setJob(cursor.getString(cursor.getColumnIndex("job")));
                member.setLiveState(cursor.getString(cursor.getColumnIndex("liveState")));
                member.setMarriage(cursor.getString(cursor.getColumnIndex("marriage")));
                member.setNation(cursor.getString(cursor.getColumnIndex("nation")));
                member.setOccupation(cursor.getString(cursor.getColumnIndex("occupation")));
                member.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                member.setRealName(cursor.getString(cursor.getColumnIndex("realName")));
                member.setTelephone(cursor.getString(cursor.getColumnIndex("telephone")));
                member.setTrueName(cursor.getString(cursor.getColumnIndex("trueName")));
                member.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
                member.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
                member.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
                member.setIsLead(cursor.getInt(cursor.getColumnIndex("isLead")));

                memberList.add(member);

            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return memberList;
    }

    /**
     * 根据成员主键搜索数据库中一条成员信息
     *
     * @param memberId 成员主键
     * @return
     */
    public Member selectMemberByMemberId(int memberId) {
        Member member = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("member", null, "memberId = ?", new String[]{memberId+""}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                member = new Member();
                member.setMemberId(cursor.getInt(cursor.getColumnIndex("memberId")));
                member.setWatchId(cursor.getInt(cursor.getColumnIndex("watchId")));
                member.setMemberNum(cursor.getString(cursor.getColumnIndex("memberNum")));
                member.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                member.setRela(cursor.getString(cursor.getColumnIndex("rela")));
                member.setSex(cursor.getInt(cursor.getColumnIndex("sex")));
                member.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
                member.setBlood(cursor.getString(cursor.getColumnIndex("blood")));
                member.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
                member.setDistance(cursor.getString(cursor.getColumnIndex("distance")));
                member.setDomicile(cursor.getString(cursor.getColumnIndex("domicile")));
                member.setEducation(cursor.getString(cursor.getColumnIndex("education")));
                member.setHouseholdRegister(cursor.getString(cursor.getColumnIndex("householdRegister")));
                member.setHeight(cursor.getString(cursor.getColumnIndex("height")));
                member.setIdentity(cursor.getString(cursor.getColumnIndex("identity")));
                member.setImage(cursor.getString(cursor.getColumnIndex("image")));
                member.setIncome(cursor.getString(cursor.getColumnIndex("income")));
                member.setJob(cursor.getString(cursor.getColumnIndex("job")));
                member.setLiveState(cursor.getString(cursor.getColumnIndex("liveState")));
                member.setMarriage(cursor.getString(cursor.getColumnIndex("marriage")));
                member.setNation(cursor.getString(cursor.getColumnIndex("nation")));
                member.setOccupation(cursor.getString(cursor.getColumnIndex("occupation")));
                member.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                member.setRealName(cursor.getString(cursor.getColumnIndex("realName")));
                member.setTelephone(cursor.getString(cursor.getColumnIndex("telephone")));
                member.setTrueName(cursor.getString(cursor.getColumnIndex("trueName")));
                member.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
                member.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
                member.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
                member.setIsLead(cursor.getInt(cursor.getColumnIndex("isLead")));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        return member;
    }

    /**
     * 更新成员的内容
     *
     * @param member 成员
     */
    public void updateMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("memberId", member.getMemberId());
        values.put("watchId", member.getWatchId());
        values.put("memberNum", member.getMemberNum());
        values.put("address", member.getAddress());
        values.put("rela", member.getRela());
        values.put("sex", member.getSex());
        values.put("birthday", member.getBirthday());
        values.put("blood", member.getBlood());
        values.put("createTime", member.getCreateTime());
        values.put("distance", member.getDistance());
        values.put("domicile", member.getDomicile());
        values.put("education", member.getEducation());
        values.put("householdRegister", member.getHouseholdRegister());
        values.put("height", member.getHeight());
        values.put("identity", member.getIdentity());
        values.put("image", member.getImage());
        values.put("income", member.getIncome());
        values.put("job", member.getJob());
        values.put("liveState", member.getLiveState());
        values.put("marriage", member.getMarriage());
        values.put("nation", member.getNation());
        values.put("occupation", member.getOccupation());
        values.put("phone", member.getPhone());
        values.put("realName", member.getRealName());
        values.put("telephone", member.getTelephone());
        values.put("trueName", member.getTrueName());
        values.put("unit", member.getUnit());
        values.put("updateTime", member.getUpdateTime());
        values.put("weight", member.getWeight());
        values.put("isLead", member.getIsLead());

        db.update("member", values, "memberId = ?", new String[]{member.getMemberId()+""});
    }

    /**
     * 判断数据库中某一个成员是否存在
     *
     * @param memberId uuid
     * @return
     */
    public boolean isMemberExists(int memberId) {
        boolean isExist = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("member", null, "memberId = ?", new String[]{memberId+""}, null, null, null);
        if (cursor.getCount() >= 1) {
            isExist = true;
        }
        if (cursor != null) {
            cursor.close();
        }

        return isExist;
    }

    /**
     * 根据memberId删除成员
     *
     * @param memberId
     */
    public void deleteMemberByMemberId(int memberId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("member", "memberId = ?", new String[]{memberId + ""});
    }

    /**
     * 清空成员表数据
     * 把自增长更新为零
     */
    public void deleteMemberData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+"member");
//        db.execSQL("update sqlite_sequence SET memberId = 0 where name ='"+"member"+"';");
//        db.delete("member", "memberId = ?", new String[]{memberId + ""});
    }



    }
