package com.example.qlchitieu.models;

public class NguoiDung {
    private String tennguoidung;
    private String matkhau;

    public NguoiDung(String tennguoidung, String matkhau) {
        this.tennguoidung = tennguoidung;
        this.matkhau = matkhau;
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
}
