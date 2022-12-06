package com.example.computermanage.model;

public class Hang {
    private String mshang;
    private int mslsp;
    private String tenhang;
    private byte[] hinhanh;

    public static final String TABLE_HANG = "hang";
    public static final String COL_MSHANG = "mshang";
    public static final String COL_TENHANG = "tenhang";
    public static final String COL_MSLSP = "mslsp";
    public static final String COL_HINHANH = "hinhanh";

    public Hang() {
    }

    public Hang(String mshang, int mslsp, String tenhang, byte[] hinhanh) {
        this.mshang = mshang;
        this.mslsp = mslsp;
        this.tenhang = tenhang;
        this.hinhanh = hinhanh;
    }

    public String getMshang() {
        return mshang;
    }

    public void setMshang(String mshang) {
        this.mshang = mshang;
    }

    public int getMslsp() {
        return mslsp;
    }

    public void setMslsp(int mslsp) {
        this.mslsp = mslsp;
    }

    public String getTenhang() {
        return tenhang;
    }

    public void setTenhang(String tenhang) {
        this.tenhang = tenhang;
    }

    public byte[] getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(byte[] hinhanh) {
        this.hinhanh = hinhanh;
    }

    @Override
    public String toString() {
        return getTenhang();
    }
}
