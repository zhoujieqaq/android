package com.example.day10_26;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test4() throws Exception{
        InputStream inputStream=new FileInputStream("C:\\Users\\28330\\Desktop\\image\\adcd.jpg");
        OutputStream outputStream=new FileOutputStream("C:\\Users\\28330\\Desktop\\image\\adcd1.jpg");

        //文件读入缓存并编码
        byte[] buf=new byte[inputStream.available()];
        inputStream.read(buf);
        //编码
        String s=new String(Base64.getEncoder().encode(buf));

        //解码，并写入文件
        byte[] buf1= Base64.getDecoder().decode(s);
        outputStream.write(buf1);

        outputStream.close();
        inputStream.close();

    }

    @Test
    public void test5() throws Exception{
        String path="C:\\Users\\28330\\Desktop\\image\\abc.jpg";
        OkHttpClient client=new OkHttpClient();

        //上传文件域的请求体部分
        RequestBody formBody=  RequestBody
                .create(new File(path), MediaType.parse("image/jpeg"));

        //整个上传的请求体部分（普通表单+文件上传域）
        RequestBody requestBody=new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "Square Logo")
                //filename:avatar,originname:abc.jpg
                .addFormDataPart("avatar", "abc.jpg",formBody)
                .build();

        Request request = new Request.Builder()
                .url("http://localhost:8000/upload")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }
}