package com.example.qlchitieu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlchitieu.MainActivity;
import com.example.qlchitieu.R;
import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.customs.ItemListBLAdapter;
import com.example.qlchitieu.models.KhoanNo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BorrowListFragment extends Fragment {
    SharedPreferences sharedPreferences;
    private TextView tv_borrow;
    private RecyclerView list_borrow;
    MainActivity activity;
    Api api = ApiUtils.getAPIService();
    int matk;
    private KhoanNo khoanNo;
    private ItemListBLAdapter itemListBLAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_borrow_list, container, false);
        activity = (MainActivity) getActivity();
        tv_borrow = view.findViewById(R.id.tv_sum_borrow);
        list_borrow = view.findViewById(R.id.list_borrow);
        list_borrow.setHasFixedSize(true);
        sharedPreferences = activity.getSharedPreferences("saveData", Context.MODE_PRIVATE);
        matk = sharedPreferences.getInt("ma_taikhoan", -1);
        todo();



        return view;
    }
    private void todo(){
        api.getBorrow(matk).enqueue(new Callback<KhoanNo>() {
            @Override
            public void onResponse(Call<KhoanNo> call, Response<KhoanNo> response) {
                if(response.code() == 200){
                    khoanNo = response.body();
                    tv_borrow.setText(String.format("%,.2f", khoanNo.getTongno()));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                    list_borrow.setLayoutManager(linearLayoutManager);
                    itemListBLAdapter = new ItemListBLAdapter(activity.getApplicationContext(),khoanNo.getKhoanno(),"n");
                    list_borrow.setAdapter(itemListBLAdapter);
                }
                if(response.code() == 404){
                    tv_borrow.setText("0");
                    if(khoanNo != null && khoanNo.getKhoanno().size() > 0){
                        itemListBLAdapter.clearApplications();
                    }
                }
            }

            @Override
            public void onFailure(Call<KhoanNo> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        todo();
    }
}
