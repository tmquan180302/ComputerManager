package com.poly.computermanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.poly.computermanager.model.Hang;
import com.poly.computermanager.sql.CreateSQL;

import java.util.ArrayList;

public class DAOHang {
    CreateSQL createSQL;
    public DAOHang(Context context){
        createSQL = new CreateSQL(context);
    }
    public long insertHang(Hang hang){
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Hang.COL_MSHANG,hang.getMshang());
        values.put(Hang.COL_TENHANG,hang.getTenhang());
        values.put(Hang.COL_MSLSP,hang.getMslsp());
        values.put(Hang.COL_HINHANH,hang.getHinhanh());
        long kq = database.insert(Hang.TABLE_HANG,null,values);
        return kq;
    }
    public int updateHang(Hang hang){
        SQLiteDatabase database = createSQL.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Hang.COL_MSHANG,hang.getMshang());
        values.put(Hang.COL_MSLSP,hang.getMslsp());
        values.put(Hang.COL_TENHANG,hang.getTenhang());
        values.put(Hang.COL_HINHANH,hang.getHinhanh());
        int kq = database.update(Hang.TABLE_HANG,values,"mshang =? ",new String[]{String.valueOf(hang.getMshang())});
        return kq;
    }
    public int deleteHang(String ma){
        SQLiteDatabase database = createSQL.getWritableDatabase();
        int kq = database.delete(Hang.TABLE_HANG,"mshang =? ",new String[]{ma});
        return kq;
    }
    public ArrayList<Hang> getAllDK(String sql ,String... a){
        SQLiteDatabase database = createSQL.getReadableDatabase();
        ArrayList<Hang> listHang = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql,a);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String mshang = cursor.getString(cursor.getColumnIndex(Hang.COL_MSHANG));
            int mslsp = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Hang.COL_MSLSP)));
            String tenhang = cursor.getString(cursor.getColumnIndex(Hang.COL_TENHANG));
            byte[] hinhanh = cursor.getBlob(cursor.getColumnIndex(Hang.COL_HINHANH));
            Hang hang = new Hang(mshang,mslsp,tenhang,hinhanh);
            listHang.add(hang);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return listHang;
    }
    public ArrayList<Hang> getAll(){
        String sql = "SELECT * FROM " + Hang.TABLE_HANG;
        return getAllDK(sql);
    }
    public Hang getID(String id){
        String sql = "SELECT * FROM hang WHERE mshang =? ";
        ArrayList<Hang> list = getAllDK(sql,id);
        return list.get(0);
    }
    public ArrayList<Hang> getAllSXTen() {
        String sql = "SELECT * FROM hang ORDER BY tenhang ASC";
        ArrayList<Hang> list = getAllDK(sql);
        return list;
    }

    public ArrayList<Hang> getAllSXMa() {
        String sql = "SELECT * FROM hang ORDER BY mshang ASC";
        ArrayList<Hang> list = getAllDK(sql);
        return list;
    }

    public ArrayList<Hang> checkGetIDHang(String id){
        String sql=" SELECT * FROM hang INNER JOIN loaisanpham on loaisanpham.mslsp =hang.mslsp WHERE hang.mshang=? ";
        ArrayList<Hang> list=getAllDK(sql, String.valueOf(id));
        return list;

    }
}
