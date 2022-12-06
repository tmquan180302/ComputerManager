package com.poly.computermanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.poly.computermanager.model.HoaDon;
import com.poly.computermanager.sql.CreateSQL;

import java.util.ArrayList;

public class DAOHoaDon {
    CreateSQL createSQL;
    public DAOHoaDon(Context context){
        createSQL = new CreateSQL(context);
    }
    public long insertHoaDon(HoaDon hoaDon){
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HoaDon.COL_MSHD,hoaDon.getMshd());
        values.put(HoaDon.COL_MSNV,hoaDon.getMsnv());
        values.put(HoaDon.COL_MSKH,hoaDon.getMskh());
        values.put(HoaDon.COL_PhanLoai,hoaDon.getPhanloaiHD());
        values.put(HoaDon.COL_NGAYMUA,hoaDon.getNgaymua());
        values.put(HoaDon.COL_TRANGTHAI,hoaDon.getTrangthai());
        long kq = database.insert(HoaDon.TABLE_HOADON,null,values);
        return kq;
    }
    public int updateHoaDon(HoaDon hoaDon){
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HoaDon.COL_MSHD,hoaDon.getMshd());
        values.put(HoaDon.COL_MSKH,hoaDon.getMskh());
        values.put(HoaDon.COL_MSNV,hoaDon.getMsnv());
        values.put(HoaDon.COL_PhanLoai,hoaDon.getPhanloaiHD());
        values.put(HoaDon.COL_NGAYMUA,hoaDon.getNgaymua());
        values.put(HoaDon.COL_TRANGTHAI,hoaDon.getTrangthai());
        int kq = database.update(HoaDon.TABLE_HOADON,values,"mshd =? ",new String[]{hoaDon.getMshd()});
        return kq;
    }
    public int deleteHoaDon(String mahd){
        SQLiteDatabase database = createSQL.getWritableDatabase();
        int kq = database.delete(HoaDon.TABLE_HOADON,"mshd =? ",new String[]{mahd});
        return kq;
    }
    public ArrayList<HoaDon> getAllDK(String sql , String... a){
        SQLiteDatabase database = createSQL.getReadableDatabase();
        ArrayList<HoaDon> listHoadon = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql,a);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String mshd = cursor.getString(cursor.getColumnIndex(HoaDon.COL_MSHD));
            String mskh = cursor.getString(cursor.getColumnIndex(HoaDon.COL_MSKH));
            String msnv = cursor.getString(cursor.getColumnIndex(HoaDon.COL_MSNV));
            int phanloai= Integer.parseInt(cursor.getString(cursor.getColumnIndex(HoaDon.COL_PhanLoai)));
            String ngaymua = cursor.getString(cursor.getColumnIndex(HoaDon.COL_NGAYMUA));
            int trangthai = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HoaDon.COL_TRANGTHAI)));
            HoaDon hoaDon = new HoaDon(mshd,msnv,mskh,phanloai,ngaymua,trangthai);
            listHoadon.add(hoaDon);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return listHoadon;
    }
    public ArrayList<HoaDon> getAll(){
        String sql = "SELECT * FROM " + HoaDon.TABLE_HOADON;
        return getAllDK(sql);
    }
    public int getSoHDNhap() {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        int soHDNhap=0;
        String sql = " SELECT count(mshd) as soHDNhap FROM hoadon WHERE phanloaiHD = 0";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            soHDNhap = cursor.getInt(cursor.getColumnIndex("soHDNhap"));

        }
        cursor.close();
        return soHDNhap;
    }
    public int getSoHDXuat() {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        int soHDXuat = 0;
        String sql = " SELECT count(mshd) as soHDXuat FROM hoadon WHERE phanloaiHD = 1";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            soHDXuat = cursor.getInt(cursor.getColumnIndex("soHDXuat"));

        }
        cursor.close();
        return soHDXuat;
    }
    public ArrayList<HoaDon> getDayXuat(){
        String sql=" SELECT * FROM hoadon WHERE ngaymua >= DATETIME('now','-1 day') AND phanloaiHD =1 ";
        return getAllDK(sql);
    }
    public ArrayList<HoaDon> getWeekXuat(){
        String sql=" SELECT * FROM hoadon WHERE ngaymua >= DATETIME('now','-7 day') AND phanloaiHD =1 ";
        return getAllDK(sql);
    }
    public ArrayList<HoaDon> getMonthXuat(){
        String sql=" SELECT * FROM hoadon WHERE ngaymua >= DATETIME('now','-30 day') AND phanloaiHD =1 ";
        return getAllDK(sql);
    }
    public ArrayList<HoaDon> getDayNhap(){
        String sql=" SELECT * FROM hoadon WHERE ngaymua >= DATETIME('now','-1 day') AND phanloaiHD =0 ";
        return getAllDK(sql);
    }
    public ArrayList<HoaDon> getWeekNhap(){
        String sql=" SELECT * FROM hoadon WHERE ngaymua >= DATETIME('now','-7 day') AND phanloaiHD =0 ";
        return getAllDK(sql);
    }
    public ArrayList<HoaDon> getMonthNhap(){
        String sql=" SELECT * FROM hoadon WHERE ngaymua >= DATETIME('now','-30 day') AND phanloaiHD =0 ";
        return getAllDK(sql);
    }
    public HoaDon getID(String id){
        String sql = "SELECT * FROM hoadon WHERE mshd =? ";
        ArrayList<HoaDon> list = getAllDK(sql,id);
        return list.get(0);
    }
    public int checkMaHD(String maHD) {
        String sql = "SELECT * FROM hoadon WHERE mshd = ? ";
        ArrayList<HoaDon> list = getAllDK(sql, maHD);
        return list.size() == 0 ? -1 : 1;
    }
    public ArrayList<HoaDon> getAllNhap() {
        String sql = "SELECT * FROM hoadon WHERE phanloaiHD = 0 ";
        ArrayList<HoaDon> list = getAllDK(sql);
        return list;
    }

    public ArrayList<HoaDon> getAllXuat() {
        String sql = "SELECT * FROM hoadon WHERE phanloaiHD = 1 " ;
        ArrayList<HoaDon> list = getAllDK(sql);
        return list;
    }
}
