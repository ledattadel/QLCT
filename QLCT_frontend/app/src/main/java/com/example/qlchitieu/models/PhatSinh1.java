package com.example.qlchitieu.models;

public class PhatSinh1 {
    private int ma_phatsinh;
    private String ngay;
    private String noidung;
    private String loaiphatsinh;
    private String sotien;
    private String mota;
    private int ma_taikhoan;

    public PhatSinh1(int ma_phatsinh, String ngay, String noidung, String loaiphatsinh, String sotien, String mota, int ma_taikhoan) {
        this.ma_phatsinh = ma_phatsinh;
        this.ngay = ngay;
        this.noidung = noidung;
        this.loaiphatsinh = loaiphatsinh;
        this.sotien = sotien;
        this.mota = mota;
        this.ma_taikhoan = ma_taikhoan;
    }

    public int getMa_phatsinh() {
        return ma_phatsinh;
    }

    public void setMa_phatsinh(int ma_phatsinh) {
        this.ma_phatsinh = ma_phatsinh;
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

    public String getSotien() {
        return sotien;
    }

    public void setSotien(String sotien) {
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
