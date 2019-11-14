package com.example.day10_26;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.example.model.MultiFace;
import com.example.model.MyImageView;
import com.example.model.People;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private MyImageView photo;
    private ArrayList<People> pList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        photo = findViewById(R.id.photo);
        // photo = findViewById(R.id.photo);
        // photo.setText("HELLO");
        //获取传递过来的图片
       // byte[] fileBuf = this.getIntent().getExtras().getByteArray("fileBuf");
        Uri uri = Uri.parse(this.getIntent().getExtras().getString("uri"));
        InputStream inputStream = null;
        byte [] fileBuf = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            fileBuf=convertToBytes(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(fileBuf, 0, fileBuf.length);
        //获取传递过来的plist值
        pList = this.getIntent().getExtras().getParcelableArrayList("pList");
        photo.setImageBitmap(bitmap);
        //从pList中获取每个人脸的信息,并置入自定义ImageView中显示
        photo.setpList(pList);
        //获取传递过来的plist值
        pList = this.getIntent().getExtras().getParcelableArrayList("pList");
        // 获取界面ListView组件
        ListView listView = (ListView) findViewById(R.id.lv);

        // 将数组包装成ArrayAdapter
        ArrayAdapter<People> adapter = new ArrayAdapter<People>(this,
                android.R.layout.simple_list_item_1, pList);

        // 为ListView设置Adapter
        listView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
