package com.example.qlchitieu.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThongTin_NguoiDung {
    @SerializedName("ma_nguoidung")
    @Expose
    private Integer maNguoidung;
    @SerializedName("hoten")
    @Expose
    private String hoten;
    @SerializedName("gioitinh")
    @Expose
    private String gioitinh;
    @SerializedName("sodienthoai")
    @Expose
    private String sodienthoai;
    @SerializedName("diachi")
    @Expose
    private String diachi;
    @SerializedName("nghenghiep")
    @Expose
    private String nghenghiep;
    @SerializedName("ma_taikhoan")
    @Expose
    private Integer maTaikhoan;

    public Integer getMaNguoidung() {
        return maNguoidung;
    }

    public void setMaNguoidung(Integer maNguoidung) {
        this.maNguoidung = maNguoidung;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getNghenghiep() {
        return nghenghiep;
    }

    public void setNghenghiep(String nghenghiep) {
        this.nghenghiep = nghenghiep;
    }

    public Integer getMaTaikhoan() {
        return maTaikhoan;
    }

    public void setMaTaikhoan(Integer maTaikhoan) {
        this.maTaikhoan = maTaikhoan;
    }

    @Override
    public String toString() {
        return "ThongTin_NguoiDung{" +
                "maNguoidung=" + maNguoidung +
                ", hoten='" + hoten + '\'' +
                ", gioitinh='" + gioitinh + '\'' +
                ", sodienthoai='" + sodienthoai + '\'' +
                ", diachi=" + diachi +
                ", nghenghiep=" + nghenghiep +
                ", maTaikhoan=" + maTaikhoan +
                '}';
    }
}
