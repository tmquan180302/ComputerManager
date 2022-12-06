package com.poly.computermanager.model;

import java.io.Serializable;

public class SanPham implements Serializable {
    private String mssp;
    private String mshang;
    private String tensp;
    private double giatien;
    private int tinhtrang;
    private int trangthai;
    private byte[] hinhanh;
    private String mota;

    public static final String TABLE_SANPHAM = "sanpham";
    public static final String COL_MSHang = "mshang";
    public static final String COL_MSSP = "mssp";
    public static final String COL_TENSP = "tensp";
    public static final String COL_GIATIEN = "giatien";
    public static final String COL_TINHTRANG = "tinhtrang";
    public static final String COL_TRANGTHAI = "trangthai";
    public static final String COL_HINHANH = "hinhanh";
    public static final String COL_MOTA = "mota";

    public SanPham() {
    }

    public SanPham(String mssp, String mshang, String tensp, double giatien, int tinhtrang, int trangthai, byte[] hinhanh, String mota) {
        this.mssp = mssp;
        this.mshang = mshang;
        this.tensp = tensp;
        this.giatien = giatien;
        this.tinhtrang = tinhtrang;
        this.trangthai = trangthai;
        this.hinhanh = hinhanh;
        this.mota = mota;
    }

    public String getMssp() {
        return mssp;
    }

    public void setMssp(String mssp) {
        this.mssp = mssp;
    }

    public String getMshang() {
        return mshang;
    }

    public void setMshang(String mshang) {
        this.mshang = mshang;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public double getGiatien() {
        return giatien;
    }

    public void setGiatien(double giatien) {
        this.giatien = giatien;
    }

    public int getTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(int tinhtrang) {
        this.tinhtrang = tinhtrang;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public byte[] getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(byte[] hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    @Override
    public String toString() {
        return getTensp();
    }
}
