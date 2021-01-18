package com.example.qlchitieu.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.customs.ItemAdapter;
import com.example.qlchitieu.customs.YearPickerDialog;
import com.example.qlchitieu.models.DanhSach;
import com.example.qlchitieu.models.TienVaoRa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YearFragment extends Fragment {
    TextView tv_in3, tv_out3, tv_sum3, tv_year;
    LinearLayout lay_year, empty_view;
    RecyclerView recycler_view3;
    MainActivity activity;
    LinearLayoutManager layoutManager;
    ItemAdapter itemAdapter;
    public ArrayList<DanhSach> list = new ArrayList<>();
    final Calendar myCalendar = Calendar.getInstance();
    final Api api = ApiUtils.getAPIService();
    SharedPreferences sharedPreferences;
    int matk;
    public ProgressDialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_year_transaction, container, false);
        activity = (MainActivity) getActivity();
        tv_in3 = view.findViewById(R.id.tv_in3);
        tv_out3 = view.findViewById(R.id.tv_out3);
        tv_sum3 = view.findViewById(R.id.tv_sum3);
        tv_year = view.findViewById(R.id.tv_year);
        lay_year = view.findViewById(R.id.lay_year);
        recycler_view3 = view.findViewById(R.id.recycler_view3);
        empty_view = view.findViewById(R.id.empty_view);

        sharedPreferences = activity.getSharedPreferences("saveData", Context.MODE_PRIVATE);
        matk = sharedPreferences.getInt("ma_taikhoan", -1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        tv_year.setText(dateFormat.format(new Date()));
        lay_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearPickerDialog pickerDialog = new YearPickerDialog(myCalendar);
                pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                        myCalendar.set(Calendar.YEAR, year);
                        updateLabel();
                    }
                });
                pickerDialog.show(activity.getSupportFragmentManager(), "MonthYearPickerDialog");
            }
        });


        return view;
    }
    private void todo(){
        showDialog();
        String year = tv_year.getText().toString();
        api.getInOutYear(year + "&" + matk).enqueue(new Callback<TienVaoRa>() {
            @Override
            public void onResponse(Call<TienVaoRa> call, Response<TienVaoRa> response) {
                if(response.code() == 200){
                    empty_view.setVisibility(View.GONE);
                    TienVaoRa tienVaoRa = response.body();
                    double b = Double.parseDouble(tienVaoRa.getTienvao());
                    double c = Double.parseDouble(tienVaoRa.getTienra());
                    tv_in3.setText(String.format("%,.2f", b));
                    tv_out3.setText(String.format("%,.2f", c));
                    double a = b - c;
                    tv_sum3.setText(String.format("%,.2f", a));
                }
                if(response.code()==404){
                    empty_view.setVisibility(View.VISIBLE);
                    tv_in3.setText("0");
                    tv_out3.setText("0");
                    tv_sum3.setText("0");
                }

            }

            @Override
            public void onFailure(Call<TienVaoRa> call, Throwable t) {
           //     Toast.makeText(activity, "Có lỗi không xác định! Vui lòng thử lại sau", Toast.LENGTH_LONG).show();
            }
        });
        api.getYear(year + "&" + matk).enqueue(new Callback<ArrayList<DanhSach>>() {
            @Override
            public void onResponse(Call<ArrayList<DanhSach>> call, Response<ArrayList<DanhSach>> response) {
                if(response.code() == 200){
                    list = response.body();
                    layoutManager = new LinearLayoutManager(activity);
                    itemAdapter = new ItemAdapter(activity,list);
                    recycler_view3.setAdapter(itemAdapter);
                    recycler_view3.setLayoutManager(layoutManager);
                }
                if(response.code() == 404){
                    if(list.size() > 0) {
                        itemAdapter.clearApplications();
                    }
                }
                if (response.code() == 500){
                    CustomToast.makeText(activity,"In sao kê không thành công", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DanhSach>> call, Throwable t) {
           //     CustomToast.makeText(activity,"In sao kê không thành công", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
            }
        });
        hideDialog();
    }
    private void updateLabel() {
        String myFormat = "yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tv_year.setText(sdf.format(myCalendar.getTime()));
        todo();
    }
    public void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(activity);
        }
        dialog.setMessage("Chờ một chút...");
        dialog.show();

    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        todo();
    }
}
