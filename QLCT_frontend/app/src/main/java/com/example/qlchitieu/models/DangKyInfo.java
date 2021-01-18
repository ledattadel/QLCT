package com.example.qlchitieu.models;

public class DangKyInfo {
    private String tennguoidung;
    private String matkhau;
    private String hoten;
    private String sodienthoai;

    public DangKyInfo(String tennguoidung, String matkhau, String hoten, String sodienthoai) {
        this.tennguoidung = tennguoidung;
        this.matkhau = matkhau;
        this.hoten = hoten;
        this.sodienthoai = sodienthoai;
    }

    public String getTennguoidung() {
        return tennguoidung;
    }

    public void setTennguoidung(String tennguoidung) {
        this.tennguoidung = tennguoidung;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }
}
