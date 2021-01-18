package com.example.qlchitieu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.qlchitieu.MainActivity;
import com.example.qlchitieu.R;
import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.models.TaiKhoan;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionFragment extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView tv_balance;
    ImageView menu_filter;
    MainActivity activity;
    Api api = ApiUtils.getAPIService();
    int matk;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        activity = (MainActivity) getActivity();
        sharedPreferences = activity.getSharedPreferences("saveData", Context.MODE_PRIVATE);
        matk = sharedPreferences.getInt("ma_taikhoan", -1);
        tv_balance = view.findViewById(R.id.tv_balance);
        menu_filter = view.findViewById(R.id.menu_filter);

        api.getBalance(matk).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {
                if(response.code()==200){
                    TaiKhoan taiKhoan = response.body();
                    tv_balance.setText(taiKhoan.getSodu());
                }
                if(response.code() == 500){
                    CustomToast.makeText(activity,"Có lỗi khi lấy số dư", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan> call, Throwable t) {
            //    CustomToast.makeText(activity,"Có lỗi không xác định! Vui lòng thử lại sau", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
            }
        });
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_tran, new DayFragment())
                .commit();
        menu_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(activity, menu_filter);
                popup.getMenuInflater()
                        .inflate(R.menu.transaction_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.filter_day:
                                getChildFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_tran, new DayFragment())
                                        .commit();
                                return true;
                            case R.id.filter_month:
                                getChildFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_tran, new MonthFragment())
                                        .commit();
                                return true;
                            case R.id.filter_year:
                                getChildFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_tran, new YearFragment())
                                        .commit();
                                return true;
                            case R.id.filter_interval:
                                getChildFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_tran, new IntervalFragment())
                                        .commit();
                                return true;
                            case R.id.filter_all:
                                getChildFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_tran, new AllFragment())
                                        .commit();
                                return true;
                        }
                        return false;
                    }
                });

                popup.show();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        api.getBalance(matk).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {
                if(response.code()==200){
                    TaiKhoan taiKhoan = response.body();
                    tv_balance.setText(taiKhoan.getSodu());
                }
                if(response.code() == 500){
                    CustomToast.makeText(activity,"Có lỗi khi lấy số dư", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan> call, Throwable t) {
                CustomToast.makeText(activity,"Không có kết nối Internet", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
            }
        });
    }
}
