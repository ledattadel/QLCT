package com.example.qlchitieu.models;

public class VayNo1 {
    private int ma_phatsinh;
    private String ngay;
    private String noidung;
    private String loaiphatsinh;
    private double sotien;
    private String mota;
    private int ma_taikhoan;
    private String ngaytra;
    private String hoten_vayno;
    private String diachi_vayno;
    private String sdt_vayno;
    private Integer datra;

    public VayNo1(int ma_phatsinh, String ngay, String noidung, String loaiphatsinh, double sotien, String mota, int ma_taikhoan, String ngaytra, String hoten_vayno, String diachi_vayno, String sdt_vayno, Integer datra) {
        this.ma_phatsinh = ma_phatsinh;
        this.ngay = ngay;
        this.noidung = noidung;
        this.loaiphatsinh = loaiphatsinh;
        this.sotien = sotien;
        this.mota = mota;
        this.ma_taikhoan = ma_taikhoan;
        this.ngaytra = ngaytra;
        this.hoten_vayno = hoten_vayno;
        this.diachi_vayno = diachi_vayno;
        this.sdt_vayno = sdt_vayno;
        this.datra = datra;
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

    public String getNgaytra() {
        return ngaytra;
    }

    public void setNgaytra(String ngaytra) {
        this.ngaytra = ngaytra;
    }

    public String getHoten_vayno() {
        return hoten_vayno;
    }

    public void setHoten_vayno(String hoten_vayno) {
        this.hoten_vayno = hoten_vayno;
    }

    public String getDiachi_vayno() {
        return diachi_vayno;
    }

    public void setDiachi_vayno(String diachi_vayno) {
        this.diachi_vayno = diachi_vayno;
    }

    public String getSdt_vayno() {
        return sdt_vayno;
    }

    public void setSdt_vayno(String sdt_vayno) {
        this.sdt_vayno = sdt_vayno;
    }

    public Integer getDatra() {
        return datra;
    }

    public void setDatra(Integer datra) {
        this.datra = datra;
    }
}
