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
import com.example.qlchitieu.models.Chovay;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanListFragment extends Fragment {
    SharedPreferences sharedPreferences;
    private TextView tv_loan;
    private RecyclerView list_loan;
    MainActivity activity;
    Api api = ApiUtils.getAPIService();
    int matk;
    private Chovay chovay;
    private ItemListBLAdapter itemListBLAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_list, container, false);
        activity = (MainActivity) getActivity();
        tv_loan = view.findViewById(R.id.tv_sum_loan);
        list_loan = view.findViewById(R.id.list_loan);
        list_loan.setHasFixedSize(true);
        sharedPreferences = activity.getSharedPreferences("saveData", Context.MODE_PRIVATE);
        matk = sharedPreferences.getInt("ma_taikhoan", -1);

        todo();


        return view;
    }
    private void todo(){
        api.getLoan(matk).enqueue(new Callback<Chovay>() {
            @Override
            public void onResponse(Call<Chovay> call, Response<Chovay> response) {
                if(response.code() == 200){
                    chovay = response.body();
                    tv_loan.setText(String.format("%,.2f", chovay.getTongvay()));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                    list_loan.setLayoutManager(linearLayoutManager);
                    itemListBLAdapter = new ItemListBLAdapter(activity.getApplicationContext(),chovay.getChovay(),"v");
                    list_loan.setAdapter(itemListBLAdapter);
                }
                if(response.code() == 404){
                    tv_loan.setText("0");
                    if(chovay != null && chovay.getChovay().size() > 0)
                    {
                        itemListBLAdapter.clearApplications();
                    }

                }

            }
            @Override
            public void onFailure(Call<Chovay> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        todo();
    }
}
