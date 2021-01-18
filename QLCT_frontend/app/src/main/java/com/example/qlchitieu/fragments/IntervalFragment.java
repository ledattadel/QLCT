package com.example.qlchitieu.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.example.qlchitieu.models.DanhSach;
import com.example.qlchitieu.models.KiemTra;
import com.example.qlchitieu.models.TienVaoRa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntervalFragment extends Fragment {
    TextView tv_in, tv_out, tv_sum, tv_from, tv_to;
    ImageView bt_seach;
    LinearLayout lay_from, lay_to, empty_view;
    RecyclerView recycler_view;
    MainActivity activity;
    public ArrayList<DanhSach> list = new ArrayList<>();
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar1 = Calendar.getInstance();
    final Calendar myCalendarf = Calendar.getInstance();
    ItemAdapter itemAdapter;
    LinearLayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    int matk;
    public ProgressDialog dialog;
    public KiemTra list1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interval_transaction, container, false);
        activity = (MainActivity) getActivity();
        tv_in = view.findViewById(R.id.tv_in);
        tv_out = view.findViewById(R.id.tv_out);
        tv_sum = view.findViewById(R.id.tv_sum);
        tv_from = view.findViewById(R.id.tv_from);
        tv_to = view.findViewById(R.id.tv_to);
        lay_from = view.findViewById(R.id.lay_from);
        lay_to = view.findViewById(R.id.lay_to);
        bt_seach = view.findViewById(R.id.bt_seach);
        recycler_view = view.findViewById(R.id.recycler_view);
        empty_view = view.findViewById(R.id.empty_view);

        sharedPreferences = activity.getSharedPreferences("saveData", Context.MODE_PRIVATE);
        matk = sharedPreferences.getInt("ma_taikhoan", -1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        tv_from.setText(dateFormat.format(new Date()));
        tv_to.setText(dateFormat.format(new Date()));


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };
        lay_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              DatePickerDialog datePickerDialog = new DatePickerDialog(activity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
              datePickerDialog.getDatePicker().setMaxDate(myCalendarf.getTimeInMillis());
              datePickerDialog.show();
            }
        });
        lay_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DatePickerDialog datePickerDialog1 = new DatePickerDialog(activity, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH));
               datePickerDialog1.getDatePicker().setMaxDate(myCalendarf.getTimeInMillis());
               datePickerDialog1.show();
            }
        });
        final Api api = ApiUtils.getAPIService();
        bt_seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String interval = getDate(tv_from.getText().toString()) +"&"+ getDate(tv_to.getText().toString());
                if(getDate(tv_to.getText().toString()).compareTo(getDate(tv_from.getText().toString())) <=0){
                    tv_from.setError("_");
                    CustomToast.makeText(activity,"Ngày bắt đầu phải trước ngày " + tv_to.getText().toString(), CustomToast.LENGTH_LONG,CustomToast.WARNING,true).show();
                    return;
                }
                tv_from.setError(null);
                tv_to.setError(null);
                showDialog();
                api.getInOut(interval +"&"+matk).enqueue(new Callback<TienVaoRa>() {
                    @Override
                    public void onResponse(Call<TienVaoRa> call, Response<TienVaoRa> response) {
                        if(response.code() == 200){
                            empty_view.setVisibility(View.GONE);
                            TienVaoRa tienVaoRa = response.body();
                            double b = Double.parseDouble(tienVaoRa.getTienvao());
                            double c = Double.parseDouble(tienVaoRa.getTienra());
                            tv_in.setText(String.format("%,.2f", b));
                            tv_out.setText(String.format("%,.2f", c));
                            double a = b - c;
                            tv_sum.setText(String.format("%,.2f", a));
                            System.out.println(a+"\n"+b+"\n"+ c);
                        }
                        if(response.code()==404){
                            empty_view.setVisibility(View.VISIBLE);
                            tv_in.setText("0");
                            tv_out.setText("0");
                            tv_sum.setText("0");
                        }

                    }

                    @Override
                    public void onFailure(Call<TienVaoRa> call, Throwable t) {
                     //   Toast.makeText(activity, "Có lỗi không xác định! Vui lòng thử lại sau", Toast.LENGTH_LONG).show();
                    }
                });

                api.getInterval(interval +"&"+matk).enqueue(new Callback<ArrayList<DanhSach>>() {
                    @Override
                    public void onResponse(Call<ArrayList<DanhSach>> call, Response<ArrayList<DanhSach>> response) {
                        if(response.code() == 200){
                            list = response.body();
                            layoutManager = new LinearLayoutManager(activity);
                            itemAdapter = new ItemAdapter(activity,list);
                            recycler_view.setAdapter(itemAdapter);
                            recycler_view.setLayoutManager(layoutManager);
                            hideDialog();
                        }
                        if(response.code() == 404){
                            if(list.size() > 0) {
                                itemAdapter.clearApplications();
                            }
                            hideDialog();
                        }
                        if (response.code() == 500){
                            hideDialog();
                            CustomToast.makeText(activity,"In sao kê không thành công",
                                    CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<DanhSach>> call, Throwable t) {
                     //   CustomToast.makeText(activity,"In sao kê không thành công",
                      //          CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                    }
                });
                hideDialog();
            }

        });

        return view;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tv_from.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel1() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String temp = sdf.format(myCalendar1.getTime());
        if(getDate(temp).compareTo(getDate(tv_from.getText().toString())) <= 0){
            tv_to.setError("_");
            CustomToast.makeText(activity,"Ngày kết thúc phải sau ngày " + tv_from.getText().toString(), CustomToast.LENGTH_LONG,CustomToast.WARNING,true).show();
            tv_to.setText(temp);

        }
        else {
            tv_to.setError(null);
            tv_to.setText(temp);
        }

    }
    private String getDate(String datein){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObject;
        String date;

        try{

            dateObject = formatter.parse(datein);
            date = new SimpleDateFormat("yyyy-MM-dd").format(dateObject);

            return date;
        }
        catch (java.text.ParseException e)
        {
            e.printStackTrace();
            Log.i("E11111111111", e.toString());
            return "";
        }

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
}
