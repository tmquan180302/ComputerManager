package com.example.computermanage.model;

public class LoaiSanPham {
    private int mslsp;
    private String tenlsp;
    private byte[] hinhanh;

    public static final String TABLE_LOAISANPHAM = "loaisanpham";
    public static final String COL_MSLSP = "mslsp";
    public static final String COL_TENLSP = "tenlsp";
    public static final String COL_HINHANH = "hinhanh";

    public LoaiSanPham() {
    }

    public LoaiSanPham(int mslsp, String tenlsp, byte[] hinhanh) {
        this.mslsp = mslsp;
        this.tenlsp = tenlsp;
        this.hinhanh = hinhanh;
    }

    public int getMslsp() {
        return mslsp;
    }

    public void setMslsp(int mslsp) {
        this.mslsp = mslsp;
    }

    public String getTenlsp() {
        return tenlsp;
    }

    public void setTenlsp(String tenlsp) {
        this.tenlsp = tenlsp;
    }

    public byte[] getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(byte[] hinhanh) {
        this.hinhanh = hinhanh;
    }

    @Override
    public String toString() {
        return getTenlsp();
    }
}
