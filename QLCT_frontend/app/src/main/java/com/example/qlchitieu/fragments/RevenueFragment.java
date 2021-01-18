package com.example.qlchitieu.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cottacush.android.currencyedittext.CurrencyEditText;
import com.example.qlchitieu.R;
import com.example.qlchitieu.UploadCostActivity;
import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.models.PhatSinh;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevenueFragment extends Fragment {
    EditText  et_category, et_date, et_des;
    CurrencyEditText et_money;
    Button button_save;
    ImageView im_ask;
    UploadCostActivity activity;
    final Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar7 = Calendar.getInstance();
    Calendar myCalendarf = Calendar.getInstance();
    public ProgressDialog dialog;
    Api api = ApiUtils.getAPIService();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_revenue, container, false);
        activity = (UploadCostActivity) getActivity();

        et_money = view.findViewById(R.id.et_money);
        et_category = view.findViewById(R.id.et_category);
        et_date = view.findViewById(R.id.et_date);
        et_des = view.findViewById(R.id.et_des);
        button_save = view.findViewById(R.id.button_save);
        im_ask = view.findViewById(R.id.im_ask);

        final int matk = getArguments().getInt("ma_tk");

        myCalendar7.add(myCalendar7.DATE,-7);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        et_date.setText(dateFormat.format(new Date()));
        im_ask.setTooltipText("Hạng mục thu ví dụ: Tiền lương, Tiền thưởng, Được cho...");

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

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              DatePickerDialog datePickerDialog =  new DatePickerDialog(activity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
              datePickerDialog.getDatePicker().setMinDate(myCalendar7.getTimeInMillis());
              datePickerDialog.getDatePicker().setMaxDate(myCalendarf.getTimeInMillis());
              datePickerDialog.show();
            }
        });
        et_money.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                et_money.setSelectAllOnFocus(true);
            }
        });
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_money.getText().length() == 0){
                    et_money.append("0");
                }
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String date = getDate();
                final String category = et_category.getText().toString();
                final Double money = et_money.getNumericValue(); //Double.parseDouble(et_money.getText().toString());
                final String categoryFlag = "t";
                final String des = et_des.getText().toString();

                if(money <= 0){
                    et_money.setError("Số tiền phải lớn hơn 0đ");
                    et_money.requestFocus();
                    return;
                }
                if(money > 9999999999.99){
                    et_money.setError("Vượt quá hạn mức giao dịch 9,999,999,999.99đ");
                    et_money.requestFocus();
                 return;
                }
                if(category.isEmpty()){
                    et_category.setError("Hạng mục không được để trống");
                    et_category.requestFocus();
                    return;
                }
                if(category.length() > 30){
                    et_category.setError("Hạng mục không quá 30 ký tự");
                    et_category.requestFocus();
                    return;
                }
                ///////////
                /////////////////////////Validation? Bro
                ///////////////////
                final PhatSinh ps = new PhatSinh(date, category, categoryFlag, money, des, matk);
                new AlertDialog.Builder(activity)
                        .setTitle("Thông báo!")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("Bạn chắc chắn muốn thêm khoản thu này không?")

                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showDialog();
                                api.revenueExpenditureAdd(ps).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        hideDialog();
                                        if(response.code() == 201){
                                            CustomToast.makeText(activity,"Thêm khoản thu thành công", CustomToast.LENGTH_LONG,CustomToast.SUCCESS,true).show();
                                            et_money.setText("0");
                                            et_category.setText("");
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                            et_date.setText(dateFormat.format(new Date()));
                                            et_des.setText("");
                                        }
                                        if(response.code() == 500){
                                            CustomToast.makeText(activity,"Thêm khoản thu không thành công", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        hideDialog();
                                   //     CustomToast.makeText(activity, "Có lỗi không xác định! Vui lòng thử lại sau", CustomToast.LENGTH_LONG, CustomToast.CONFUSING, true).show();
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

        et_date.setText(sdf.format(myCalendar.getTime()));
    }
    private String getDate(){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObject;
        String date;

        try{
            String dob_var=(et_date.getText().toString());
            dateObject = formatter.parse(dob_var);
            date = new SimpleDateFormat("yyyy-MM-dd").format(dateObject);

            return date;
        }
        catch (java.text.ParseException e)
        {
            e.printStackTrace();
            Log.i("E11111111111", e.toString());
            return null;
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
