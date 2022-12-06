package com.example.computermanage.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.computermanage.model.GetSLNhap;
import com.example.computermanage.model.GetSLXuat;
import com.example.computermanage.model.GetTop;
import com.example.computermanage.model.HoaDonChiTiet;
import com.example.computermanage.model.TienTonNhap;
import com.example.computermanage.model.TienTonXuat;
import com.example.computermanage.sql.CreateSQL;

import java.util.ArrayList;

public class DAOHoaDonCT {
    CreateSQL createSQL;

    public DAOHoaDonCT(Context context) {
        createSQL = new CreateSQL(context);
    }

    public long insertHoaDonCT(HoaDonChiTiet hoaDonChiTiet) {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HoaDonChiTiet.COL_MSHD, hoaDonChiTiet.getMshd());
        values.put(HoaDonChiTiet.COL_MSSP, hoaDonChiTiet.getMssp());
        values.put(HoaDonChiTiet.COL_DONGIA, hoaDonChiTiet.getDongia());
        values.put(HoaDonChiTiet.COL_BAOHANH, hoaDonChiTiet.getBaohanh());
        values.put(HoaDonChiTiet.COL_GIAMGIA, hoaDonChiTiet.getGiamgia());
        values.put(HoaDonChiTiet.COL_SOLUONG, hoaDonChiTiet.getSoluong());
        long kq = database.insert(HoaDonChiTiet.TABLE_HOADONCT, null, values);
        return kq;
    }

    public int updateHoaDonCT(HoaDonChiTiet hoaDonChiTiet) {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HoaDonChiTiet.COL_MSHD, hoaDonChiTiet.getMshd());
        values.put(HoaDonChiTiet.COL_MSSP, hoaDonChiTiet.getMssp());
        values.put(HoaDonChiTiet.COL_DONGIA, hoaDonChiTiet.getDongia());
        values.put(HoaDonChiTiet.COL_BAOHANH, hoaDonChiTiet.getBaohanh());
        values.put(HoaDonChiTiet.COL_GIAMGIA, hoaDonChiTiet.getGiamgia());
        values.put(HoaDonChiTiet.COL_SOLUONG, hoaDonChiTiet.getSoluong());
        int kq = database.update(HoaDonChiTiet.TABLE_HOADONCT, values, "mshdct =? ", new String[]{String.valueOf(hoaDonChiTiet.getMshdct())});
        return kq;
    }
    public ArrayList<HoaDonChiTiet> getAllDK(String sql, String... a) {
        SQLiteDatabase database = createSQL.getReadableDatabase();
        ArrayList<HoaDonChiTiet> listHoadonCT = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql, a);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int mshdct = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HoaDonChiTiet.COL_MSHDCT)));
            String mshd = cursor.getString(cursor.getColumnIndex(HoaDonChiTiet.COL_MSHD));
            String mssp = cursor.getString(cursor.getColumnIndex(HoaDonChiTiet.COL_MSSP));
            int soluong = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HoaDonChiTiet.COL_SOLUONG)));
            double dongia = Double.parseDouble((cursor.getString(cursor.getColumnIndex(HoaDonChiTiet.COL_DONGIA))));
            int baohanh = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HoaDonChiTiet.COL_BAOHANH)));
            int giamgia = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HoaDonChiTiet.COL_GIAMGIA)));

            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet(mshdct, mshd, mssp, soluong, dongia, baohanh, giamgia);
            listHoadonCT.add(hoaDonChiTiet);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return listHoadonCT;
    }

    public ArrayList<HoaDonChiTiet> getAll() {
        String sql = "SELECT * FROM " + HoaDonChiTiet.TABLE_HOADONCT;
        return getAllDK(sql);
    }
    public ArrayList<HoaDonChiTiet> getListMahd(String id) {
        String sql = "SELECT * FROM hoadonchitiet WHERE mshd =? ";
        return getAllDK(sql);
    }
    public HoaDonChiTiet getID(String id) {
        String sql = "SELECT * FROM hoadonchitiet WHERE mshd =? ";
        ArrayList<HoaDonChiTiet> list = getAllDK(sql, id);
        return list.get(0);
    }
    public HoaDonChiTiet getMaHD(String maHD) {
        String sql = "SELECT * FROM hoadonchitiet WHERE mshd =?";
        ArrayList<HoaDonChiTiet> list = getAllDK(sql, maHD);
        return list.get(0);
    }
    public int checkCTHD(String maHD) {
        ArrayList<HoaDonChiTiet> list = new ArrayList<>();
        String sql = "SELECT * FROM hoadonchitiet WHERE mshd = ?";
        list = getAllDK(sql, maHD);
        return list.size() == 0 ? -1 : 1;
    }

    public double doanhthu() {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        double thanhtien = 0;
        String sql = " SELECT sum(soluong*dongia) as thanhtien FROM hoadon INNER JOIN hoadonchitiet ON hoadon.mshd = hoadonchitiet.mshd WHERE hoadon.phanloaiHD =1";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            thanhtien = Double.parseDouble(cursor.getString(cursor.getColumnIndex("thanhtien")));

        }
        cursor.close();
        return thanhtien;

    }

    //
    public double getTongNhap() {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        double tongnhap = 0;
        String sql = " SELECT sum(soluong*dongia) as tongnhap FROM hoadon INNER JOIN hoadonchitiet ON hoadon.mshd = hoadonchitiet.mshd WHERE hoadon.phanloaiHD = 0 ";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            tongnhap = Double.parseDouble(cursor.getString(cursor.getColumnIndex("tongnhap")));

        }
        cursor.close();
        return tongnhap;

    }

    public double getTongXuat() {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        double tongxuat = 0;
        String sql = " SELECT sum(soluong*dongia) as tongxuat FROM hoadon INNER JOIN hoadonchitiet ON hoadon.mshd = hoadonchitiet.mshd WHERE hoadon.phanloaiHD = 1";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            tongxuat = Double.parseDouble(cursor.getString(cursor.getColumnIndex("tongxuat")));

        }
        cursor.close();
        return tongxuat;
    }
    public int getSLNhap() {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        int slNhap = 0;
        String sql = " SELECT sum(soluong) as slNhap FROM hoadon INNER JOIN hoadonchitiet ON hoadon.mshd = hoadonchitiet.mshd WHERE hoadon.phanloaiHD = 0";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            slNhap = cursor.getInt(cursor.getColumnIndex("slNhap"));

        }
        cursor.close();
        return slNhap;
    }
    public int getSLXuat() {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        int slXuat = 0;
        String sql = "SELECT sum(soluong) as slXuat FROM hoadon INNER JOIN hoadonchitiet ON hoadon.mshd = hoadonchitiet.mshd WHERE hoadon.phanloaiHD = 1";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            slXuat = cursor.getInt(cursor.getColumnIndex("slXuat"));

        }
        cursor.close();
        return slXuat;
    }
    public ArrayList<GetTop> getTop(){
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ArrayList<GetTop> listTop = new ArrayList<>();
        String sqlTop = "SELECT sanpham.tensp , sum(soluong) as slsp \n" +
                "FROM sanpham INNER JOIN hoadonchitiet ON sanpham.mssp = hoadonchitiet.mssp \n" +
                "INNER JOIN hoadon ON hoadonchitiet.mshd = hoadon.mshd  WHERE hoadon.phanloaiHD = 1\n" +
                "GROUP BY sanpham.tensp ORDER BY slsp DESC";

        Cursor cursor = database.rawQuery(sqlTop,null);
        while (cursor.moveToNext()){
            String tensp = cursor.getString(cursor.getColumnIndex("tensp"));
            int sl = cursor.getInt(cursor.getColumnIndex("slsp"));
            GetTop getTop = new GetTop(tensp,sl);
            listTop.add(getTop);
        }
        return listTop;
    }
    public ArrayList<GetSLNhap> slNhap() {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ArrayList<GetSLNhap> list = new ArrayList<>();
        String sql = " SELECT hoadonchitiet.mssp ,sum(soluong) as slnhap FROM hoadon INNER JOIN hoadonchitiet ON hoadon.mshd = hoadonchitiet.mshd\n" +
                "WHERE hoadon.phanloaiHD = 0 GROUP BY hoadonchitiet.mssp";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()){
            String mssp = cursor.getString(cursor.getColumnIndex("mssp"));
            int sl = Integer.parseInt(cursor.getString(cursor.getColumnIndex("slnhap")));
            GetSLNhap getSLNhap = new GetSLNhap(mssp,sl);
            list.add(getSLNhap);
        }
        cursor.close();
        return list;
    }
    public ArrayList<GetSLXuat> slXuat() {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ArrayList<GetSLXuat> list = new ArrayList<>();
        String sql = " SELECT hoadonchitiet.mssp, sum(soluong) as slxuat FROM hoadon INNER JOIN hoadonchitiet ON hoadon.mshd = hoadonchitiet.mshd\n" +
                "WHERE hoadon.phanloaiHD = 1 GROUP BY hoadonchitiet.mssp";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()){
            String mssp = cursor.getString(cursor.getColumnIndex("mssp"));
            int sl = Integer.parseInt(cursor.getString(cursor.getColumnIndex("slxuat")));
            GetSLXuat getSLXuat = new GetSLXuat(mssp,sl);
            list.add(getSLXuat);
        }
        cursor.close();
        return list;
    }
    public double tienTon() {
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ArrayList<TienTonNhap> listNhap = new ArrayList<>();
        // tinh tien nhap
        String sql = "SELECT hoadonchitiet.mssp ,hoadonchitiet.dongia as dgnhap ,soluong as slnhap FROM hoadon INNER JOIN hoadonchitiet ON hoadon.mshd = hoadonchitiet.mshd\n" +
                "WHERE hoadon.phanloaiHD = 0";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()){
            String massp = cursor.getString(cursor.getColumnIndex("mssp"));
            double dongia = Double.parseDouble(cursor.getString(cursor.getColumnIndex("dgnhap")));
            int sl = Integer.parseInt(cursor.getString(cursor.getColumnIndex("slnhap")));
            TienTonNhap tienTonNhap = new TienTonNhap(massp , dongia, sl);
            listNhap.add(tienTonNhap);
        }

        ArrayList<TienTonXuat> listXuat = new ArrayList<>();
        // tinh tien xuat
        sql = "SELECT hoadonchitiet.mssp ,hoadonchitiet.dongia as dgxuat ,soluong as slxuat FROM hoadon INNER JOIN hoadonchitiet ON hoadon.mshd = hoadonchitiet.mshd\n" +
                "WHERE hoadon.phanloaiHD = 1";
        cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()){
            String massp = cursor.getString(cursor.getColumnIndex("mssp"));
            double dongia = Double.parseDouble(cursor.getString(cursor.getColumnIndex("dgxuat")));
            int sl = Integer.parseInt(cursor.getString(cursor.getColumnIndex("slxuat")));
            TienTonXuat tienTonXuat = new TienTonXuat(massp , dongia, sl);
            listXuat.add(tienTonXuat);
        }

        double tongTienNhap = 0;

        for(int i = 0 ; i < listNhap.size()  ;i++){
            for(int j = 0 ; j < listXuat.size()  ;j++){
                if(listNhap.get(i).mssp.equals(listXuat.get(j).mssp)){
                    listNhap.get(i).soluongnhap -= listXuat.get(j).soluongxuat;
                }
            }
        }
        for(int i = 0 ; i < listNhap.size()  ;i++){
            tongTienNhap += listNhap.get(i).dongianhap * listNhap.get(i).soluongnhap;
        }


        cursor.close();
        return tongTienNhap;
    }
}
