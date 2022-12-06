package com.poly.computermanager.model;

public class GetTop {
    public String tensp;
    public int soluong;

    public GetTop() {
    }

    public GetTop(String tensp, int soluong) {
        this.tensp = tensp;
        this.soluong = soluong;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
