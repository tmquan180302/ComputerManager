package com.poly.computermanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.poly.computermanager.model.LoaiSanPham;
import com.poly.computermanager.sql.CreateSQL;

import java.util.ArrayList;

public class DAOLoaiSanPham {
    CreateSQL createSQL;
    public DAOLoaiSanPham(Context context){
        createSQL = new CreateSQL(context);
    }
    public long insertLoaiSanPham(LoaiSanPham loaiSanPham){
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LoaiSanPham.COL_TENLSP,loaiSanPham.getTenlsp());
        values.put(LoaiSanPham.COL_HINHANH,loaiSanPham.getHinhanh());
        long kq = database.insert(LoaiSanPham.TABLE_LOAISANPHAM,null,values);
        return kq;
    }
    public int updateLoaiSanPham(LoaiSanPham loaiSanPham){
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LoaiSanPham.COL_TENLSP,loaiSanPham.getTenlsp());
        values.put(LoaiSanPham.COL_HINHANH,loaiSanPham.getHinhanh());
        int kq = database.update(LoaiSanPham.TABLE_LOAISANPHAM,values,"mslsp = ? ",new String[]{String.valueOf(loaiSanPham.getMslsp())});
        return kq;
    }
    public int deleteLoaiSanPham(int id){
        SQLiteDatabase database = createSQL.getWritableDatabase();
        int kq = database.delete(LoaiSanPham.TABLE_LOAISANPHAM,"mslsp = ? ",new String[]{String.valueOf(id)});
        return kq;
    }
    public ArrayList<LoaiSanPham> getAllDK(String sql , String... a){
        SQLiteDatabase database = createSQL.getReadableDatabase();
        ArrayList<LoaiSanPham> listLSP = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql,a);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int mslsp = Integer.parseInt(cursor.getString(cursor.getColumnIndex(LoaiSanPham.COL_MSLSP)));
            String tenlsp = cursor.getString(cursor.getColumnIndex(LoaiSanPham.COL_TENLSP));
            byte[] hinhanh = cursor.getBlob(cursor.getColumnIndex(LoaiSanPham.COL_HINHANH));
            LoaiSanPham loaiSanPham = new LoaiSanPham(mslsp,tenlsp,hinhanh);
            listLSP.add(loaiSanPham);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return listLSP;
    }
    public ArrayList<LoaiSanPham> getAll(){
        String sql = "SELECT * FROM "+LoaiSanPham.TABLE_LOAISANPHAM;
        return getAllDK(sql);
    }
    public ArrayList<LoaiSanPham> getAllSXTen() {
        String sql = "SELECT * FROM loaisanpham ORDER BY tenlsp ASC";
        ArrayList<LoaiSanPham> list = getAllDK(sql);
        return list;
    }

    public ArrayList<LoaiSanPham> getAllSXMa() {
        String sql = "SELECT * FROM loaisanpham ORDER BY mslsp ASC";
        ArrayList<LoaiSanPham> list = getAllDK(sql);
        return list;
    }
    public LoaiSanPham getID(String id){
        String sql = "SELECT * FROM loaisanpham WHERE mslsp=? ";
        ArrayList<LoaiSanPham> list = getAllDK(sql,id);
        return list.get(0);
    }
    public ArrayList<LoaiSanPham> checkGetIDLoai(int id){
        String sql=" SELECT * FROM loaisanpham INNER JOIN hang on loaisanpham.mslsp = hang.mslsp WHERE loaisanpham.mslsp=? ";
        ArrayList<LoaiSanPham> list=getAllDK(sql, String.valueOf(id));
        return list;

    }
}
