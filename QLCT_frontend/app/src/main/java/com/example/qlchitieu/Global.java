package com.example.qlchitieu;

import android.app.Application;

public class Global extends Application {
    private int ma_nguoidung;
    private int ma_taikhoan;


    public int getMa_nguoidung() {
        return ma_nguoidung;
    }

    public void setMa_nguoidung(int ma_nguoidung) {
        this.ma_nguoidung = ma_nguoidung;
    }

    public int getMa_taikhoan() {
        return ma_taikhoan;
    }

    public void setMa_taikhoan(int ma_taikhoan) {
        this.ma_taikhoan = ma_taikhoan;
    }
}
