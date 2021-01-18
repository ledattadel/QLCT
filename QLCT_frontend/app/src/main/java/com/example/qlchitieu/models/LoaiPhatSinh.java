package com.example.qlchitieu.models;

public class LoaiPhatSinh {
    private String loai;
    private int icon;

    public LoaiPhatSinh(String loai, int icon) {
        this.loai = loai;
        this.icon = icon;
    }

    public String getLoai() {
        return loai;
    }

    public int getIcon() {
        return icon;
    }
}
