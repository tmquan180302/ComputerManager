package com.example.computermanage.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.computermanage.model.SanPham;
import com.example.computermanage.sql.CreateSQL;

import java.util.ArrayList;
import java.util.List;

public class DAOSanPham {
    CreateSQL createSQL;

    public DAOSanPham(Context context) {
        createSQL = new CreateSQL(context);
    }

    public long insertSanPham(SanPham sanPham) {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SanPham.COL_MSSP, sanPham.getMssp());
        values.put(SanPham.COL_MSHang, sanPham.getMshang());
        values.put(SanPham.COL_TENSP, sanPham.getTensp());
        values.put(SanPham.COL_GIATIEN, sanPham.getGiatien());
        values.put(SanPham.COL_TINHTRANG, sanPham.getTinhtrang());
        values.put(SanPham.COL_TRANGTHAI, sanPham.getTrangthai());
        values.put(SanPham.COL_HINHANH, sanPham.getHinhanh());
        values.put(SanPham.COL_MOTA, sanPham.getMota());
        long kq = database.insert(SanPham.TABLE_SANPHAM, null, values);
        return kq;
    }

    public int updateSanPham(SanPham sanPham) {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SanPham.COL_MSSP, sanPham.getMssp());
        values.put(SanPham.COL_MSHang, sanPham.getMshang());
        values.put(SanPham.COL_TENSP, sanPham.getTensp());
        values.put(SanPham.COL_GIATIEN, sanPham.getGiatien());
        values.put(SanPham.COL_TINHTRANG, sanPham.getTinhtrang());
        values.put(SanPham.COL_TRANGTHAI, sanPham.getTrangthai());
        values.put(SanPham.COL_HINHANH, sanPham.getHinhanh());
        values.put(SanPham.COL_MOTA, sanPham.getMota());
        int kq = database.update(SanPham.TABLE_SANPHAM,  values,"mssp =? ",new String[]{sanPham.getMssp()});
        return kq;
    }

    public int deleteSanPham(String id) {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        int kq = database.delete(SanPham.TABLE_SANPHAM, "mssp =? ", new String[]{id});
        return kq;
    }

    public ArrayList<SanPham> getAllDK(String sql, String... a) {
        SQLiteDatabase database = createSQL.getReadableDatabase();
        ArrayList<SanPham> list = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql, a);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String mssp = cursor.getString(cursor.getColumnIndex(SanPham.COL_MSSP));
            String mshang = cursor.getString(cursor.getColumnIndex(SanPham.COL_MSHang));
            String tensp = cursor.getString(cursor.getColumnIndex(SanPham.COL_TENSP));
            double giatien = Double.parseDouble(cursor.getString(cursor.getColumnIndex(SanPham.COL_GIATIEN)));
            int tinhtrang = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SanPham.COL_TINHTRANG)));
            int trangthai = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SanPham.COL_TRANGTHAI)));
            byte[] hinhanh = cursor.getBlob(cursor.getColumnIndex(SanPham.COL_HINHANH));
            String mota = cursor.getString(cursor.getColumnIndex(SanPham.COL_MOTA));
            SanPham sanPham = new SanPham(mssp, mshang, tensp, giatien, tinhtrang,trangthai, hinhanh,mota);
            list.add(sanPham);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }

    public ArrayList<SanPham> getAll() {
        String sql = "SELECT * FROM " + SanPham.TABLE_SANPHAM;
        return getAllDK(sql);
    }
    public ArrayList<SanPham> getAllSPBan() {
        String sql = "SELECT * FROM sanpham WHERE trangthai = 0";
        ArrayList<SanPham> list = getAllDK(sql);
        return list;
    }
    public ArrayList<SanPham> getAllDell() {
        String sql = "SELECT * FROM sanpham WHERE tensp LIKE '%dell%'";
        return getAllDK(sql);
    }
    public ArrayList<SanPham> getAllHP() {
        String sql = "SELECT * FROM sanpham WHERE tensp LIKE '%hp%'";
        return getAllDK(sql);
    }
    public ArrayList<SanPham> getAllThinkPad() {
        String sql = "SELECT * FROM sanpham WHERE tensp LIKE '%thinkpad%'";
        return getAllDK(sql);
    }
    public ArrayList<SanPham> getAllAsus() {
        String sql = "SELECT * FROM sanpham WHERE tensp LIKE '%asus%'";
        return getAllDK(sql);
    }
    public ArrayList<SanPham> getAllAcer() {
        String sql = "SELECT * FROM sanpham WHERE tensp LIKE '%acer%'";
        return getAllDK(sql);
    }
    public ArrayList<SanPham> getAllMac() {
        String sql = "SELECT * FROM sanpham WHERE tensp LIKE '%macbook%'";
        return getAllDK(sql);
    }
    public ArrayList<SanPham> getAllLenovo() {
        String sql = "SELECT * FROM sanpham WHERE tensp LIKE '%lenovo%'";
        return getAllDK(sql);
    }
    public ArrayList<SanPham> getAllMSI() {
        String sql = "SELECT * FROM sanpham WHERE tensp LIKE '%msi%'";
        return getAllDK(sql);
    }
    public ArrayList<SanPham> getAllSXTen() {
        String sql = "SELECT * FROM sanpham ORDER BY tensp ASC";
        ArrayList<SanPham> list = getAllDK(sql);
        return list;
    }

    public ArrayList<SanPham> getAllSXMa() {
        String sql = "SELECT * FROM sanpham ORDER BY mssp ASC";
        ArrayList<SanPham> list = getAllDK(sql);
        return list;
    }
    public ArrayList<SanPham> getAllSXGia() {
        String sql = "SELECT * FROM sanpham ORDER BY giatien ASC";
        ArrayList<SanPham> list = getAllDK(sql);
        return list;
    }

    public SanPham getID(String id) {
        String sql = "SELECT * FROM sanpham WHERE mssp =? ";
        ArrayList<SanPham> list = getAllDK(sql, id);
        return list.get(0);
    }
    public int checkMaSP(String maSP) {
        String sql = "SELECT * FROM sanpham WHERE mssp = ?";
        ArrayList<SanPham> list = getAllDK(sql, maSP);
        return list.size() == 0 ? -1 : 1;
    }
    public ArrayList<SanPham> checkGetIDSpham(String id){
        String sql=" SELECT * FROM sanpham INNER JOIN hoadonchitiet on hoadonchitiet.mssp =sanpham.mssp " +
                "INNER JOIN hoadon on hoadon.mshd=hoadonchitiet.mshd WHERE sanpham.mssp=? AND hoadon.phanloaiHD =1 OR hoadon.phanloaiHD =0 ";
        ArrayList<SanPham> list=getAllDK(sql, String.valueOf(id));
        return list;
    }

}
