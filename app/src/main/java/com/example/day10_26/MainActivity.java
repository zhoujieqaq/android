package com.example.day10_26;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.example.model.People;
import com.example.services.App;
import com.example.services.RsResult;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingNavigationView mFloatingNavigationView;
    private Uri uri;
    private ImageView photo;
    private String uploadFileName;
    private InputStream inputStream;
    private byte[] fileBuf;
    private ArrayList<People> pList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        photo = findViewById(R.id.photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFloatingNavigationView = (FloatingNavigationView) findViewById(R.id.floating_navigation_view);
        mFloatingNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFloatingNavigationView.open();
            }
        });
        mFloatingNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Snackbar.make((View) mFloatingNavigationView.getParent(), item.getTitle() + " Selected!", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent();
                //页面跳转
                if (item.getTitle().equals("人脸检测")) {
                    intent.setClass(MainActivity.this, MainActivity.class);
                } else if (item.getTitle().equals("人脸上传")) {
                    intent.setClass(MainActivity.this, UploadActivity.class);
                } else if (item.getTitle().equals("颜值检测")) {
                    intent.setClass(MainActivity.this, DetectScoreActivity.class);
                } else {
                    intent.setClass(MainActivity.this, MainActivity.class);
                }
                startActivity(intent);
                mFloatingNavigationView.close();
                return true;
            }
        });

    }

    //按钮点击选择事件
    public void select(View view) {
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
    //多张人脸检索
    public void multiSearch(View view) {
        new Thread() {
            @Override
            public void run() {
                String base64 = null;
                try {
                    base64 = App.toBase64(fileBuf);
                }catch (NullPointerException e){
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                String rs = App.multiSearchFaceWithBase64(base64, "test5");
                System.out.println(rs);
                RsResult rsResult = new RsResult();
                if(!rsResult.isSuccess(rs)){
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, "查找失败，请选择一张库中人脸", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }
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
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                //到图片传给结果界面
                intent.putExtra("uri", uri.toString());
                //把人物信息传给结果界面
                intent.putExtra("pList", pList);
                startActivity(intent);

            }
        }.start();
    }

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
        try {
            inputStream = getContentResolver().openInputStream(uri);
            fileBuf = convertToBytes(inputStream);
            Bitmap bitmap = BitmapFactory.decodeByteArray(fileBuf, 0, fileBuf.length);
            photo.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
    }


    //转为对象数组
    private byte[] convertToBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        inputStream.close();
        return out.toByteArray();
    }


}