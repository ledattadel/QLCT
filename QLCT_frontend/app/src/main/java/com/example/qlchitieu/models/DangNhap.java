package com.example.qlchitieu.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DangNhap {
    @SerializedName("ma_nguoidung")
    @Expose
    private int maNguoidung;
    @SerializedName("ma_taikhoan")
    @Expose
    private int maTaikhoan;
    @SerializedName("message")
    @Expose
    private String message;

    public int getMaNguoidung() {
        return maNguoidung;
    }

    public void setMaNguoidung(Integer maNguoidung) {
        this.maNguoidung = maNguoidung;
    }

    public int getMaTaikhoan() {
        return maTaikhoan;
    }

    public void setMaTaikhoan(Integer maTaikhoan) {
        this.maTaikhoan = maTaikhoan;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "DangNhap{" +
                "maNguoidung=" + maNguoidung +
                ", maTaikhoan=" + maTaikhoan +
                ", message='" + message + '\'' +
                '}';
    }
}
