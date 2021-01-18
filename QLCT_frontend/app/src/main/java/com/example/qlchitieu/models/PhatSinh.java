package com.example.qlchitieu.models;

public class PhatSinh {

    private String ngay;
    private String noidung;
    private String loaiphatsinh;
    private double sotien;
    private String mota;
    private int ma_taikhoan;

    public PhatSinh(String ngay, String noidung, String loaiphatsinh, double sotien, String mota, int ma_taikhoan) {
        this.ngay = ngay;
        this.noidung = noidung;
        this.loaiphatsinh = loaiphatsinh;
        this.sotien = sotien;
        this.mota = mota;
        this.ma_taikhoan = ma_taikhoan;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getLoaiphatsinh() {
        return loaiphatsinh;
    }

    public void setLoaiphatsinh(String loaiphatsinh) {
        this.loaiphatsinh = loaiphatsinh;
    }

    public double getSotien() {
        return sotien;
    }

    public void setSotien(double sotien) {
        this.sotien = sotien;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getMa_taikhoan() {
        return ma_taikhoan;
    }

    public void setMa_taikhoan(int ma_taikhoan) {
        this.ma_taikhoan = ma_taikhoan;
    }
}
