package com.yinaf.dragon.Content.Activity.family_set;

import android.content.Context;

import com.google.gson.Gson;
import com.yinaf.dragon.Content.Activity.family_set.model.JsonBean;
import com.yinaf.dragon.Content.Activity.family_set.model.MemberSeleteModel;
import com.yinaf.dragon.Content.Activity.family_set.model.MemberSetModel;
import com.yinaf.dragon.Content.Activity.family_set.util.GetJsonDataUtil;
import com.yinaf.dragon.Content.Bean.MySettingModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存 家人设置页中的常量
 */
public class ConstantSet {

    public static ArrayList<JsonBean> options1Items = new ArrayList<>();
    public static ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    public static ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private ConstantSet() {
    }

    /**
     * 获取服药提醒的时间设置  0-23 小时  0-59  分钟
     *
     * @return
     */
    public static List<List<String>> getDrugTime() {
        List<List<String>> times = new ArrayList<>();
        List<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hours.add("0" + i);
            } else {
                hours.add(i + "");
            }

        }
        times.add(hours);
        List<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minutes.add("0" + i);
            } else {
                minutes.add(i + "");
            }
        }
        times.add(minutes);
        return times;
    }

    /**
     * 根据请求到的数据,生成成员资料的列表项进行填写
     *
     * @param obj
     * @return 0 列表 id, 1 资料选择项 2 资料填写项
     */
    public static List<String[]> getMemberList(MemberSetModel.ObjBean obj) {
        ArrayList<String[]> list = new ArrayList<>();

        list.add(new String[]{"realName", "成员昵称", obj.getRealName()});
        list.add(new String[]{"trueName", "真实姓名", obj.getTrueName()});
        list.add(new String[]{"birthday", "出生日期", obj.getBirthday()});
        list.add(new String[]{"identity", "身份证号", obj.getIdentity()});
        list.add(new String[]{"address", "家庭地址", obj.getAddress()});
        if (obj.getSex() == 1) {
            list.add(new String[]{"sex", "性别", "男"});
        } else if (obj.getSex() == 2) {
            list.add(new String[]{"sex", "性别", "女"});
        } else {
            list.add(new String[]{"sex", "性别", ""});
        }

        list.add(new String[]{"blood", "血型", obj.getBlood()});
        list.add(new String[]{"nation", "民族", obj.getNation()});
        list.add(new String[]{"height", "身高", obj.getHeight()});
        list.add(new String[]{"weight", "体重", obj.getWeight()});
        list.add(new String[]{"phone", "手机号码", obj.getPhone()});
        list.add(new String[]{"rela", "与用户的关系", obj.getRela()});
        list.add(new String[]{"marriage", "婚姻状况", obj.getMarriage()});
        list.add(new String[]{"job", "工作情况", obj.getJob()});
        list.add(new String[]{"unit", "工作单位", obj.getUnit()});
        list.add(new String[]{"occupation", "职业", obj.getOccupation()});
        list.add(new String[]{"income", "收入情况", obj.getIncome()});
        list.add(new String[]{"education", "文化程度", obj.getEducation()});
        list.add(new String[]{"liveState", "居住情况", obj.getLiveState()});
        list.add(new String[]{"distance", "与子女距离", obj.getDistance()});
        list.add(new String[]{"householdRegister", "户籍地", obj.getHouseholdRegister()});
        list.add(new String[]{"domicile", "所在地", obj.getDomicile()});


        return list;
    }

    /**
     * 根据请求到的数据,生成用户基本资料的列表项进行填写
     *
     * @param obj
     * @return 0 列表 id, 1 资料选择项 2 资料填写项
     */
    public static List<String[]> getUserList(MySettingModel.ObjBean obj) {
        ArrayList<String[]> list = new ArrayList<>();

        list.add(new String[]{"realName", "成员昵称", obj.getRealName()});

        if (obj.getSex() == 1) {
            list.add(new String[]{"sex", "性别", "男"});
        } else if (obj.getSex() == 2) {
            list.add(new String[]{"sex", "性别", "女"});
        } else {
            list.add(new String[]{"sex", "性别", ""});
        }
        list.add(new String[]{"birthday", "出生日期", obj.getBirthday()});

        list.add(new String[]{"blood", "血型", obj.getBlood()});
        list.add(new String[]{"mail", "邮箱", obj.getMail()});
        list.add(new String[]{"phone", "手机号码", obj.getPhone()});
        list.add(new String[]{"occupation", "职业", obj.getOccupation()});
        list.add(new String[]{"householdRegister", "户籍地", obj.getHouseholdRegister()});
        list.add(new String[]{"domicile", "所在地", obj.getDomicile()});


        return list;
    }
    /**
     * 根据请求到的数据,生成成员资料的列表项进行填写
     *
     */
    public static MemberSetModel.ObjBean setMemberList(MemberSetModel model,List<String[]> datas) {
        MemberSetModel.ObjBean obj = model.getObj();
        obj.setRealName(datas.get(0)[2]);
        obj.setTrueName(datas.get(1)[2]);
        obj.setBirthday(datas.get(2)[2]);
        obj.setIdentity(datas.get(3)[2]);
        obj.setAddress(datas.get(4)[2]);
        if (datas.get(5)[2].equals("男")) {
            obj.setSex(1);

        } else if (datas.get(5)[2].equals("女")) {
            obj.setSex(2);
        } else {
            obj.setSex(0);
        }

        obj.setBlood(datas.get(6)[2]);
        obj.setNation(datas.get(7)[2]);
        obj.setHeight(datas.get(8)[2]);
        obj.setWeight(datas.get(9)[2]);
        obj.setPhone(datas.get(10)[2]);
        obj.setRela(datas.get(11)[2]);
        obj.setMarriage(datas.get(12)[2]);
        obj.setJob(datas.get(13)[2]);
        obj.setUnit(datas.get(14)[2]);
        obj.setOccupation(datas.get(15)[2]);
        obj.setIncome(datas.get(16)[2]);
        obj.setEducation(datas.get(17)[2]);
        obj.setLiveState(datas.get(18)[2]);
        obj.setDistance(datas.get(19)[2]);
        obj.setHouseholdRegister(datas.get(20)[2]);
        obj.setDomicile(datas.get(21)[2]);



        return obj;
    }
/**
     * 根据请求到的数据,生成用户基本资料的列表项进行填写
     *
     */
    public static MySettingModel.ObjBean setUserList(MySettingModel model,List<String[]> datas) {
        MySettingModel.ObjBean obj = model.getObj();
        obj.setRealName(datas.get(0)[2]);

        if (datas.get(1)[2].equals("男")) {
            obj.setSex(1);

        } else if (datas.get(1)[2].equals("女")) {
            obj.setSex(2);
        } else {
            obj.setSex(0);
        }
        obj.setBirthday(datas.get(2)[2]);
        obj.setBlood(datas.get(3)[2]);
        obj.setMail(datas.get(4)[2]);
        obj.setPhone(datas.get(5)[2]);
        obj.setOccupation(datas.get(6)[2]);
        obj.setHouseholdRegister(datas.get(7)[2]);
        obj.setDomicile(datas.get(8)[2]);



        return obj;
    }


    /**
     * 成员关系
     */
    public static List<String> getRelationList() {
        List<String> list = new ArrayList<>();
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
        return list;
    }

    /**
     * 城市选择器数据初始化
     */
    public static boolean initJsonData(Context context) {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(context, "city.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean;
        try {
            jsonBean = parseData(JsonData);//用Gson 转成实体
        } catch (Exception e) {
            return false;
        }


        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        return true;
    }


    private static ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public static List<MemberSeleteModel> getSexItems(List<MemberSeleteModel> items) {
        items.add(new MemberSeleteModel("男",false));
        items.add(new MemberSeleteModel("女",false));
        return items;
    }

    public static List<MemberSeleteModel> getBloodItems(List<MemberSeleteModel> items) {
        items.add(new MemberSeleteModel("A型",false));
        items.add(new MemberSeleteModel("B型",false));
        items.add(new MemberSeleteModel("AB型",false));
        items.add(new MemberSeleteModel("O型",false));
        items.add(new MemberSeleteModel("其他",false));
        return items;
    }

    public static List<MemberSeleteModel> getNationItems(List<MemberSeleteModel> items) {
        String name = "汉族、蒙古族、满族、朝鲜族、赫哲族、达斡尔族、鄂温克族、鄂伦春族、回族" +
                "、东乡族、土族、撒拉族、保安族、裕固族、维吾尔族、哈萨克族、柯尔克孜族" +
                "、锡伯族、塔吉克族、乌孜别克族、俄罗斯族、塔塔尔族、藏族、门巴族、珞巴族、" +
                "羌族、彝族、白族、哈尼族、傣族、僳僳族、佤族、拉祜族、纳西族、景颇族、" +
                "布朗族、阿昌族、普米族、怒族、德昂族、独龙族、基诺族、苗族、布依族、侗族、" +
                "水族、仡佬族、壮族、瑶族、仫佬族、毛南族、京族、土家族、黎族、畲族、高山族";
        for (String item : name.split("、")) {
            items.add(new MemberSeleteModel(item,false));
        }

        return items;
    }

    public static List<MemberSeleteModel> getMarriageItems(List<MemberSeleteModel> items) {
        items.add(new MemberSeleteModel("未婚",false));
        items.add(new MemberSeleteModel("已婚",false));
        items.add(new MemberSeleteModel("丧偶",false));
        items.add(new MemberSeleteModel("离异",false));
        return items;
    }

    public static List<MemberSeleteModel> getJobItems(List<MemberSeleteModel> items) {
        items.add(new MemberSeleteModel("在职",false));
        items.add(new MemberSeleteModel("务农",false));
        items.add(new MemberSeleteModel("退休",false));
        items.add(new MemberSeleteModel("低保户",false));
        items.add(new MemberSeleteModel("五保户",false));
        items.add(new MemberSeleteModel("孤寡老人",false));
        items.add(new MemberSeleteModel("军属",false));
        items.add(new MemberSeleteModel("待业",false));
        items.add(new MemberSeleteModel("其他",false));
        return items;
    }

    public static List<MemberSeleteModel> getOccupationItems(List<MemberSeleteModel> items) {
        items.add(new MemberSeleteModel("计算机/互联网/通信",false));
        items.add(new MemberSeleteModel("生产/工艺/制造",false));
        items.add(new MemberSeleteModel("医疗/护理/制药",false));
        items.add(new MemberSeleteModel("金融/银行/投资/保险",false));
        items.add(new MemberSeleteModel("商业/服务业/个体经营",false));
        items.add(new MemberSeleteModel( "文化/广告/传媒",false));
        items.add(new MemberSeleteModel("娱乐/艺术/表演",false));
        items.add(new MemberSeleteModel("律师/法务",false));
        items.add(new MemberSeleteModel("教育/培训",false));
        items.add(new MemberSeleteModel("公务员/行政/事业单位",false));
        items.add(new MemberSeleteModel("学生",false));
        items.add(new MemberSeleteModel("其他",false));
        return items;
    }

    public static List<MemberSeleteModel> getEducationItems(List<MemberSeleteModel> items) {
        items.add(new MemberSeleteModel("小学",false));
        items.add(new MemberSeleteModel("初中",false));
        items.add(new MemberSeleteModel("高中",false));
        items.add(new MemberSeleteModel("大专",false));
        items.add(new MemberSeleteModel("本科",false));
        items.add(new MemberSeleteModel("硕士",false));
        items.add(new MemberSeleteModel("博士",false));
        items.add(new MemberSeleteModel("博士后",false));
        items.add(new MemberSeleteModel("其他",false));
        return items;
    }

    public static List<MemberSeleteModel> getLiveStateItems(List<MemberSeleteModel> items) {
        items.add(new MemberSeleteModel("与子女同居",false));
        items.add(new MemberSeleteModel("独居",false));
        items.add(new MemberSeleteModel("疗养机构",false));
        items.add(new MemberSeleteModel("其他",false));
        return items;
    }

    public static List<MemberSeleteModel> getDistanceItems(List<MemberSeleteModel> items) {
        items.add(new MemberSeleteModel("同城",false));
        items.add(new MemberSeleteModel("同省",false));
        items.add(new MemberSeleteModel("同国",false));
        items.add(new MemberSeleteModel("异国",false));
        items.add(new MemberSeleteModel("其他",false));
        return items;
    }
}
