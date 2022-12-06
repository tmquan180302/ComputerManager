package com.example.computermanage.model;

public class HoaDon {
    private String mshd;
    private String msnv;
    private String mskh;
    private int phanloaiHD;
    private String ngaymua;
    private int trangthai;

    public static final String TABLE_HOADON = "hoadon";
    public static final String COL_MSHD = "mshd";
    public static final String COL_MSNV = "msnv";
    public static final String COL_PhanLoai = "phanloaiHD";
    public static final String COL_MSKH = "mskh";
    public static final String COL_NGAYMUA = "ngaymua";
    public static final String COL_TRANGTHAI = "trangthai";


    public HoaDon() {
    }

    public HoaDon(String mshd, String msnv, String mskh, int phanloaiHD, String ngaymua, int trangthai) {
        this.mshd = mshd;
        this.msnv = msnv;
        this.mskh = mskh;
        this.phanloaiHD = phanloaiHD;
        this.ngaymua = ngaymua;
        this.trangthai = trangthai;
    }

    public String getMshd() {
        return mshd;
    }

    public void setMshd(String mshd) {
        this.mshd = mshd;
    }

    public String getMsnv() {
        return msnv;
    }

    public void setMsnv(String msnv) {
        this.msnv = msnv;
    }

    public String getMskh() {
        return mskh;
    }

    public void setMskh(String mskh) {
        this.mskh = mskh;
    }

    public int getPhanloaiHD() {
        return phanloaiHD;
    }

    public void setPhanloaiHD(int phanloaiHD) {
        this.phanloaiHD = phanloaiHD;
    }

    public String getNgaymua() {
        return ngaymua;
    }

    public void setNgaymua(String ngaymua) {
        this.ngaymua = ngaymua;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }
}
