package com.example.services;

import com.example.model.FaceInfo;
import com.example.model.MultiFace;
import com.example.model.People;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RsResult { //存返回结果的对象
    private String error_msg;

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }




    //判断百度接口是否成功，如果成功，返回SUCCESS;
    public boolean isSuccess(String rs) {
        boolean flag = false;
        FaceInfo faceInfo = new FaceInfo(rs);
        String msg = faceInfo.getError_msg();
        if(msg.equals( "SUCCESS"))
            flag = true;
        return flag;
    }

    //获取单张搜索成功时的信息
    public ArrayList<People> searchInfo(String rs){
        FaceInfo faceInfo = new FaceInfo(rs);
        FaceInfo.ResultBean result = faceInfo.getResult();
        List<FaceInfo.ResultBean.UserListBean> user_list = result.getUser_list();

        ArrayList<People> list = new ArrayList<>();
        for (FaceInfo.ResultBean.UserListBean bean : user_list){
            People p = new People();
            p.setUser_id(bean.getUser_id());
            p.setName(bean.getUser_info());
            list.add(p);
        }
        return  list;
    }

    //获取多人搜索成功时的信息
    public ArrayList<People> multiSearchInfo(String rs) throws JSONException {
        MultiFace multiFace = new MultiFace(rs);
        MultiFace.ResultBean result = multiFace.getResult();
        List<MultiFace.ResultBean.FaceListBean> face_list = result.getFace_list();

        ArrayList<People> list = new ArrayList<>();
        for(MultiFace.ResultBean.FaceListBean fbean : face_list){
            List<MultiFace.ResultBean.FaceListBean.UserListBean> user_list = fbean.getUser_list();
            People p = new People();
            p.setLeft(fbean.getLocation().getLeft());
            p.setTop(fbean.getLocation().getTop());
            p.setWidth(fbean.getLocation().getWidth());
            p.setHeight(fbean.getLocation().getHeight());
            p.setRoration(fbean.getLocation().getRotation());
            for(MultiFace.ResultBean.FaceListBean.UserListBean bean : user_list){
                p.setUser_id(bean.getUser_id());
                p.setName(bean.getUser_info());
                list.add(p);
            }

        }
        return list;
    }

    //获取人脸年龄和评分信息
    public String detectInfo(String rs){
        String info = "没有查询到";
        //解析json
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(rs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject result = jsonObject.optJSONObject("result");
        JSONArray face_list = result.optJSONArray("face_list");
        for(int i = 0;i < face_list.length();i++){
            JSONObject jsonObject1 = face_list.optJSONObject(i);
            if(jsonObject1 != null){
                Integer age = jsonObject1.optInt("age");
                Double beauty = jsonObject1.optDouble("beauty");
                info = "预测年龄：" + age.toString() + "；"+"颜值："+beauty.toString();
            }
        }
        return info;
    }
}
