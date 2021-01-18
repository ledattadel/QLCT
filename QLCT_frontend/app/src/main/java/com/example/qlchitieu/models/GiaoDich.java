package com.example.qlchitieu.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GiaoDich {
    @SerializedName("ngayphatsinh")
    @Expose
    private String ngayphatsinh;
    @SerializedName("phatsinh")
    @Expose
    private List<PhatSinh> phatsinh = null;

    public String getNgayphatsinh() {
        return ngayphatsinh;
    }

    public void setNgayphatsinh(String ngayphatsinh) {
        this.ngayphatsinh = ngayphatsinh;
    }

    public List<PhatSinh> getPhatsinh() {
        return phatsinh;
    }

    public void setPhatsinh(List<PhatSinh> phatsinh) {
        this.phatsinh = phatsinh;
    }
}
