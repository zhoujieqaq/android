package com.example.day10_26;

import com.bumptech.glide.Glide;

import com.example.model.People;
import com.example.services.App;
import com.example.services.RsResult;
import com.example.tools.NetInterceptor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tools.NetInterceptor;

import net.coobird.thumbnailator.Thumbnails;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SelectPhotoActivity extends AppCompatActivity {
    private Uri uri;
    private ImageView photo;
    private String uploadFileName;
    private InputStream inputStream;
    private byte[] fileBuf;
    private ListView lv;
    private ArrayList<People> pList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        photo = findViewById(R.id.photo);
    }

    //按钮点击选择事件
    public void select(View view) {
        Button uploadButton = findViewById(R.id.upload);
        EditText nameEditText = findViewById(R.id.name);
        EditText idEditText = findViewById(R.id.id);
        nameEditText.setVisibility(View.VISIBLE);
        idEditText.setVisibility(View.VISIBLE);
        uploadButton.setVisibility(View.VISIBLE);
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        //进行sdcard的读写请求
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            openGallery(); //打开相册，进行选择
        }
    }


    //按钮点击多人人脸识别

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(this, "读相册的操作被拒绝", Toast.LENGTH_LONG).show();
                }
        }
    }

    //打开相册,进行照片的选择
    private void openGallery() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                handleSelect(data);
        }
    }

    //选择后照片的读取工作
    private void handleSelect(Intent intent) {


        Cursor cursor = null;
        uri = intent.getData();
        cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            uploadFileName = cursor.getString(columnIndex);
        }
        //使用glide的图片读取
//        try {
//            Glide.with(this).load(uri)
//                    .fitCenter()
//                    .into(photo);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //没有使用glide的图片读取
        try {
            inputStream = getContentResolver().openInputStream(uri);
            fileBuf=convertToBytes(inputStream);
            Bitmap bitmap = BitmapFactory.decodeByteArray(fileBuf, 0, fileBuf.length);
            photo.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
    }
    //单个人脸检索
    public void detect(View view){
        new Thread(){
            @Override
            public void run(){
                String base64 = App.toBase64(fileBuf);
                String rs = App.searchFaceWithBase64(base64,"test5");
                System.out.println(rs);
                RsResult rsResult = new RsResult();
                pList = rsResult.searchInfo(rs);
                //跳转界面
                Intent intent = new Intent(SelectPhotoActivity.this,MainActivity.class);
                intent.putExtra("pList",pList);
                startActivity(intent);
            }

        }.start();
    }

    //多张人脸检索
    public void multiSearch(View view){
        new Thread(){
            @Override
            public void run(){
                String base64 = App.toBase64(fileBuf);
                String rs = App.multiSearchFaceWithBase64(base64,"test5");
                System.out.println(rs);
                RsResult rsResult = new RsResult();
                try {
                    pList = rsResult.multiSearchInfo(rs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //跳转到MainActivity界面
//                Intent intent = new Intent(SelectPhotoActivity.this,MainActivity.class);
//                intent.putExtra("pList",pList);
//                startActivity(intent);

                //跳转到ResultActivity界面
                Intent intent = new Intent(SelectPhotoActivity.this,ResultActivity.class);
                //到图片传给结果界面
                intent.putExtra("uri",uri.toString());
                //把人物信息传给结果界面
                intent.putExtra("pList",pList);
                startActivity(intent);

            }
        }.start();
    }


    //图片上传的处理
    public void upload(View view) {
        new Thread() {
            @Override
            public void run() {
                //获取编辑框中的内容
                EditText txtName = (EditText)findViewById(R.id.name);
                String name = txtName.getText().toString();
                EditText user_id = (EditText)findViewById(R.id.id);
                String id = user_id.getText().toString();
                String base64 = App.toBase64(fileBuf);
                String rs=App.addFaceWithBase64(base64,"test5",id,name);
                System.out.println(rs);
                RsResult rsResult = new RsResult();
                if(rsResult.isSuccess(rs)) {
                    Looper.prepare();
                    Toast.makeText(SelectPhotoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }.start();

    }

    private byte[] convertToBytes(InputStream inputStream) throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        inputStream.close();
        return  out.toByteArray();
    }
}