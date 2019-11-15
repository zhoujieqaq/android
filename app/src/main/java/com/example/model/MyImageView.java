package com.example.model;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;

//自定义imageView
public class MyImageView extends AppCompatImageView {

    private ArrayList<People> pList;

    public ArrayList<People> getpList() {
        return pList;
    }

    public void setpList(ArrayList<People> pList) {
        this.pList = pList;
    }

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //根据位置绘制信息
        for (People p : pList) {
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setTextSize(50);
            canvas.rotate(p.getRoration());
            canvas.drawText(p.toString(), (float) p.getLeft(), (float) p.getTop(), paint);
        }
    }


}
