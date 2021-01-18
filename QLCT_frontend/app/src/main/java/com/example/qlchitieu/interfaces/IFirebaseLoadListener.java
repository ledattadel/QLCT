package com.example.qlchitieu.interfaces;

import com.example.qlchitieu.models.DanhSach;

import java.util.List;

public interface IFirebaseLoadListener {
    void onFirebaseLoadSuccess(List<DanhSach> list);
    void onFirebaseLoadFailed(String message);
}
