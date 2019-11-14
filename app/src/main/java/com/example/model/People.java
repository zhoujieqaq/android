package com.example.model;

import android.os.Parcel;
import android.os.Parcelable;

//实现Parcelable接口，可以用intent传递对象
public class People implements Parcelable {
    private String user_id;
    private String name;
    private double left;
    private double top;
    private int width;
    private int height;
    private int rotation;


    //必须实现这个接口，它的作用是从percel中读取数据，顺序必须按照声明顺序
    public static final Creator<People> CREATOR = new Creator<People>() {

        @Override
        public People createFromParcel(Parcel in) {
            People p = new People();
            p.user_id = in.readString();
            p.name = in.readString();
            p.left = in.readDouble();
            p.top = in.readDouble();
            p.width = in.readInt();
            p.height = in.readInt();
            p.rotation = in.readInt();
            return p;
        }

        @Override
        public People[] newArray(int size) {
            return new People[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        //序列化对象，必须按照声明顺序
        parcel.writeString(user_id);
        parcel.writeString(name);
        parcel.writeDouble(left);
        parcel.writeDouble(top);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeInt(rotation);

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRoration() {
        return rotation;
    }

    public void setRoration(int roration) {
        this.rotation = roration;
    }

    @Override
    public String toString() {
        return "姓名:"+name;
    }
}
