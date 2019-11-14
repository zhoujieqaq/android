package com.example.services;

import android.util.Base64;

import com.example.tools.Base64Util;

import okhttp3.*;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class App {
    static String detectUrl="https://aip.baidubce.com/rest/2.0/face/v3/detect?access_token=";
    static String addUrl="https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add?access_token=";
    static String searchUrl="https://aip.baidubce.com/rest/2.0/face/v3/search?access_token=";
    static String multiSearchUrl="https://aip.baidubce.com/rest/2.0/face/v3/multi-search?access_token=";

    static String access_token="24.fad0c046fc8425755cec8ff819ca957a.2592000.1574950903.282335-17647875";
    static {
        try {
            detectUrl+=access_token;
            addUrl+=access_token;
            searchUrl+=access_token;
            multiSearchUrl+=access_token;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String toBase64(byte[] buf){
        String s = null;

            s = Base64Util.encode(buf);

        return  s;
    }


    public static String detectFaceWithUrl(String faceUrl){
        String rs="";
        OkHttpClient client = new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("image_type","URL")
                .add("image",faceUrl)
                .build();
        Request request=new Request.Builder()
                .url(detectUrl)
                .header("Content-Type","application/json")
                .post(body)
                .build();

        try {
            Response resp= client.newCall(request).execute();
            rs = resp.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }


    public static String detectFaceWithBase64(String Base64){
        String rs="";
        OkHttpClient client = new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("image_type","BASE64")
                .add("image",Base64)
                .add("face_field","age,beauty")
                .build();
        Request request=new Request.Builder()
                .url(detectUrl)
                .header("Content-Type","application/json")
                .post(body)
                .build();

        try {
            Response resp= client.newCall(request).execute();
            rs = resp.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static String addFaceWithBase64(String Base64,String groupId,String userId,String userInfo){
        String rs="";
        OkHttpClient client = new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("image_type","BASE64")
                .add("group_id",groupId)
                .add("image",Base64)
                .add("user_id",userId)
                .add("user_info",userInfo)
                .build();
        Request request=new Request.Builder()
                .url(addUrl)
                .header("Content-Type","application/json")
                .post(body)
                .build();

        try {
            Response resp= client.newCall(request).execute();
            rs = resp.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }


    public static String searchFaceWithBase64(String Base64,String groupIdList){
        String rs="";
        OkHttpClient client = new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("image_type","BASE64")
                .add("image",Base64)
                .add("group_id_list", groupIdList)
                .build();
        Request request=new Request.Builder()
                .url(searchUrl)
                .header("Content-Type","application/json")
                .post(body)
                .build();

        try {
            Response resp= client.newCall(request).execute();
            rs = resp.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static String multiSearchFaceWithBase64(String Base64,String groupIdList){
        String rs="";
        OkHttpClient client = new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("image_type","BASE64")
                .add("image",Base64)
                .add("group_id_list", groupIdList)
                .add("max_face_num","10")
                .build();
        Request request=new Request.Builder()
                .url(multiSearchUrl)
                .header("Content-Type","application/json")
                .post(body)
                .build();

        try {
            Response resp= client.newCall(request).execute();
            rs = resp.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }



    public static void main(String[] args) throws Exception{
        //String s=toBase64(Thread.currentThread().getContextClassLoader().getResourceAsStream("cage1.jpeg"));
        System.out.println();

    }
}
