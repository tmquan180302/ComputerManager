package com.poly.computermanager.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CreateSQL extends SQLiteOpenHelper {
    public CreateSQL(@Nullable Context context) {
        super(context, "computermanage.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create table nhanvien
        String CREATE_TABLE_NHANVIEN = "CREATE TABLE nhanvien(" +
                "msnv TEXT UNIQUE PRIMARY KEY NOT NULL," +
                "password TEXT  ," +
                "hoten TEXT  ," +
                "sdt TEXT  ," +
                "email TEXT," +
                "hinhanh BLOB)";
        db.execSQL(CREATE_TABLE_NHANVIEN);
        //create table khachhang
        String CREATE_TABLE_KHACHHANG = "CREATE TABLE khachhang(" +
                "mskh TEXT UNIQUE PRIMARY KEY NOT NULL," +
                "hoten TEXT  ," +
                "sdt TEXT  ," +
                "gioitinh TEXT  ," +
                "diachi TEXT  )";
        db.execSQL(CREATE_TABLE_KHACHHANG);
        //create table loaisanpham
        String CREATE_TABLE_LOAISANPHAM = "CREATE TABLE loaisanpham(" +
                "mslsp INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "tenlsp TEXT  ," +
                "hinhanh BLOB)";
        db.execSQL(CREATE_TABLE_LOAISANPHAM);
        //create table hang
        String CREATE_TABLE_HANG = "CREATE TABLE hang(" +
                "mshang TEXT PRIMARY KEY  NOT NULL," +
                "mslsp INTEGER NOT NULL REFERENCES loaisanpham(mslsp) ON DELETE CASCADE ON UPDATE CASCADE,"+
                "tenhang TEXT   , hinhanh BLOB )";
        db.execSQL(CREATE_TABLE_HANG);

        String CREATE_TABLE_SANPHAM = "CREATE TABLE sanpham(" +
                "mssp TEXT PRIMARY KEY NOT NULL," +
                "mshang TEXT NOT NULL REFERENCES hang(mshang) ON DELETE CASCADE ON UPDATE CASCADE," +
                "tensp TEXT  ," +
                "giatien DECIMAL(20,0) ," +
                "tinhtrang INTEGER ," +
                "trangthai INTEGER ," +
                "hinhanh BLOB," +
                "mota TEXT)";
        db.execSQL(CREATE_TABLE_SANPHAM);
        //create table sanphamchitiet
        String CREATE_TABLE_SPCHITIET = "CREATE TABLE sanphamchitiet(" +
                "msspct INTEGER PRIMARY KEY AUTOINCREMENT," +
                "mssp TEXT NOT NULL REFERENCES sanpham(mssp) ON DELETE CASCADE ON UPDATE CASCADE ," +
                "cpu TEXT," +
                "ram TEXT," +
                "ocung TEXT," +
                "hedieuhanh TEXT," +
                "manhinh TEXT," +
                "cardmh TEXT," +
                "pin TEXT," +
                "trongluong FLOAT)";
        db.execSQL(CREATE_TABLE_SPCHITIET);
        //create table hoadon
        String CREATE_TABLE_HOADON = "CREATE TABLE hoadon(" +
                "mshd TEXT UNIQUE PRIMARY KEY NOT NULL," +
                "msnv TEXT NOT NULL REFERENCES nhanvien(msnv) ON DELETE CASCADE ON UPDATE CASCADE," +
                "mskh TEXT REFERENCES khachhang(mskh) ON DELETE CASCADE ON UPDATE CASCADE," +
                "phanloaiHD INTEGER  ," +
                "ngaymua DATE  ," +
                "trangthai INTEGER)";
        db.execSQL(CREATE_TABLE_HOADON);
        //create table hoadonchitiet
        String CREATE_TABLE_HDCHITIET = "CREATE TABLE hoadonchitiet(" +
                "mshdct INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
                "mshd TEXT  NOT NULL REFERENCES hoadon(mshd) ON DELETE CASCADE ON UPDATE CASCADE," +
                "mssp TEXT  NOT NULL REFERENCES sanpham(mssp) ON DELETE CASCADE ON UPDATE CASCADE," +
                "soluong INTEGER ," +
                "dongia DOUBLE ," +
                "baohanh INTEGER," +
                "giamgia INTEGER)";
        db.execSQL(CREATE_TABLE_HDCHITIET);

    }
    //thaidoi

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
