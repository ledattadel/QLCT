package com.example.qlchitieu.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cottacush.android.currencyedittext.CurrencyEditText;
import com.example.qlchitieu.R;
import com.example.qlchitieu.UploadCostActivity;
import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.models.TaiKhoan1;
import com.example.qlchitieu.models.VayNo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanFragment extends Fragment {
    EditText et_category2, et_date2, et_des2, et_nameloan, et_addressloan, et_phoneloan, et_repay_date;
    CurrencyEditText et_money2;
    Button button_save2;
    UploadCostActivity activity;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar1 = Calendar.getInstance();
    Calendar myCalendar7 = Calendar.getInstance();
    Calendar myCalendarf = Calendar.getInstance();
    private ProgressDialog dialog;
    Api api = ApiUtils.getAPIService();
    private Double sodu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_loan, container, false);
        activity = (UploadCostActivity) getActivity();

        et_money2 = view.findViewById(R.id.et_money2);
        et_category2 = view.findViewById(R.id.et_category2);
        et_date2 = view.findViewById(R.id.et_date2);
        et_des2 = view.findViewById(R.id.et_des2);
        et_nameloan = view.findViewById(R.id.et_nameloan);
        et_addressloan = view.findViewById(R.id.et_addressloan);
        et_phoneloan = view.findViewById(R.id.et_phoneloan);
        et_repay_date = view.findViewById(R.id.et_repay_date);
        button_save2 = view.findViewById(R.id.button_save2);

        myCalendar7.add(myCalendar7.DATE, -7);

        final int matk = getArguments().getInt("ma_tk");

        et_category2.setText("Cho vay");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        et_date2.setText(dateFormat.format(new Date()));


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
        et_date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             DatePickerDialog datePickerDialog = new DatePickerDialog(activity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
             datePickerDialog.getDatePicker().setMinDate(myCalendar7.getTimeInMillis());
             datePickerDialog.getDatePicker().setMaxDate(myCalendarf.getTimeInMillis());
             datePickerDialog.show();
            }
        });
        et_repay_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              DatePickerDialog datePickerDialog1 = new DatePickerDialog(activity, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH));
              datePickerDialog1.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
              datePickerDialog1.show();
            }
        });

        et_money2.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                    et_money2.setSelectAllOnFocus(true);
            }
        });
        et_money2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_money2.getText().length() == 0){
                    et_money2.append("0");
                }
            }
        });
        api.getBalace1(matk).enqueue(new Callback<TaiKhoan1>() {
            @Override
            public void onResponse(Call<TaiKhoan1> call, Response<TaiKhoan1> response) {
                if(response.code() == 200)
                {
                    TaiKhoan1 taiKhoan1 = response.body();
                    sodu = taiKhoan1.getSodu();
                }
                if (response.code() == 500){
                    CustomToast.makeText(activity,"Lấy số dư không thành công", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan1> call, Throwable t) {

            }
        });
        button_save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = getDate(et_date2.getText().toString());
                String category = et_category2.getText().toString();
                Double money = et_money2.getNumericValue(); // Double.parseDouble(et_money2.getText().toString());
                String categoryFlag = "v";
                String des = et_des2.getText().toString();
                String nameloan = et_nameloan.getText().toString();
                String addressloan = et_addressloan.getText().toString();
                String phoneloan = et_phoneloan.getText().toString();
                String dateRepay = "";
                if(et_repay_date.getText().toString() != "")
                    dateRepay = getDate(et_repay_date.getText().toString());
                int matk = getArguments().getInt("ma_tk");

                if(money <= 0){
                    et_money2.setError("Số tiền cho vay phải lớn hơn 0đ");
                    et_money2.requestFocus();
                    return;
                }
                if(money > sodu)
                {
                    et_money2.setError("Số tiền cho vay lớn hơn số dư đang có");
                    et_money2.requestFocus();
                    return;
                }
                if(money > 9999999999.99){
                    et_money2.setError("Vượt quá hạn mức giao dịch 9,999,999,999.99đ");
                    et_money2.requestFocus();
                    return;
                }
                if(category.isEmpty()){
                    et_category2.setError("Hạng mục không được để trống");
                    et_category2.requestFocus();
                    return;
                }
                if(nameloan.isEmpty()){
                    et_nameloan.setError("Tên người vay không được để trống");
                    et_nameloan.requestFocus();
                    return;
                }
                if(!phoneloan.isEmpty() && phoneloan.length() < 10)
                {
                    et_phoneloan.setError("Số điện thoại phải đủ 10 số");
                    et_phoneloan.requestFocus();
                    return;
                }
                if(!dateRepay.isEmpty() && dateRepay.compareTo(date) < 0 && dateRepay != null){
                    et_repay_date.setError("_");
                    CustomToast.makeText(activity,"Ngày thu nợ không trước ngày " + date, CustomToast.LENGTH_LONG,CustomToast.WARNING,true).show();
                    return;
                }
                if(dateRepay.isEmpty())
                {
                    et_repay_date.setError("_");
                    CustomToast.makeText(activity,"Ngày thu nợ không được để trống", CustomToast.LENGTH_LONG,CustomToast.WARNING,true).show();
                    return;
                }
                if(des.compareTo("") == 0 ){
                    des = "Cho " + nameloan + " vay";
                }
                ///////////
                /////////////////////////Validation? Bro
                ///////////////////
                final VayNo vayNo = new VayNo(date, category, categoryFlag, money, des, matk, dateRepay, nameloan, addressloan, phoneloan);
                new AlertDialog.Builder(activity)
                        .setTitle("Thông báo!")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("Bạn chắc chắn muốn thêm khoản cho vay này không?")

                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showDialog();
                                api.LoanBorrowadd(vayNo).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        hideDialog();
                                        if(response.code() == 201){
                                            CustomToast.makeText(activity,"Thêm khoản cho vay thành công", CustomToast.LENGTH_LONG,CustomToast.SUCCESS,true).show();
                                            et_money2.setText("0");
                                            et_category2.setText("Cho vay");
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                            et_date2.setText(dateFormat.format(new Date()));
                                            et_des2.setText("");
                                            et_nameloan.setText("");
                                            et_addressloan.setText("");
                                            et_phoneloan.setText("");
                                            et_repay_date.setText("");
                                            et_repay_date.setError(null);
                                        }
                                        if(response.code() == 500){
                                            CustomToast.makeText(activity,"Thêm khoản cho vay không thành công", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        hideDialog();
                                        Toast.makeText(activity, "Có lỗi không xác định! Vui lòng thử lại sau", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();

            }
        });
        return view;
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_date2.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel1() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String temp = sdf.format(myCalendar1.getTime());
        if(getDate(temp).compareTo(getDate(et_date2.getText().toString())) < 0){
            et_repay_date.setError("_");
            CustomToast.makeText(activity,"Ngày thu nợ không trước ngày " + et_date2.getText().toString(), CustomToast.LENGTH_LONG,CustomToast.WARNING,true).show();
            et_repay_date.setText(temp);

        }
        else {
            et_repay_date.setError(null);
            et_repay_date.setText(temp);
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
