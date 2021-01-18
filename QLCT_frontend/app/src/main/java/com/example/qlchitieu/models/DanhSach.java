package com.example.qlchitieu.models;

import java.util.List;

public class DanhSach {
    private String ngayphatsinh;
    private List<PhatSinh1> phatsinh;

    public DanhSach(String ngayphatsinh, List<PhatSinh1> phatsinh) {
        this.ngayphatsinh = ngayphatsinh;
        this.phatsinh = phatsinh;
    }

    public String getNgayphatsinh() {
        return ngayphatsinh;
    }

    public void setNgayphatsinh(String ngayphatsinh) {
        this.ngayphatsinh = ngayphatsinh;
    }

    public List<PhatSinh1> getPhatsinh() {
        return phatsinh;
    }

    public void setPhatsinh(List<PhatSinh1> phatsinh) {
        this.phatsinh = phatsinh;
    }
}
