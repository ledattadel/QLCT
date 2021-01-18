package com.example.qlchitieu.models;

public class ThuTra {
    private String ngay;
    private String noidung;
    private String loaiphatsinh;
    private double sotien;
    private String mota;
    private int ma_taikhoan;
    private int ma_ps;

    public ThuTra(String ngay, String noidung, String loaiphatsinh, double sotien, String mota, int ma_taikhoan, int ma_ps) {
        this.ngay = ngay;
        this.noidung = noidung;
        this.loaiphatsinh = loaiphatsinh;
        this.sotien = sotien;
        this.mota = mota;
        this.ma_taikhoan = ma_taikhoan;
        this.ma_ps = ma_ps;
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

    public int getMa_ps() {
        return ma_ps;
    }

    public void setMa_ps(int ma_ps) {
        this.ma_ps = ma_ps;
    }
}
