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
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.R;
import com.example.qlchitieu.UploadCostActivity;
import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
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

public class BorrowFragment extends Fragment {
    EditText  et_category3, et_date3, et_des3, et_nameborrow, et_addressborrow, et_phoneborrow, et_repay_date1;
    CurrencyEditText et_money3;
    Button button_save3;
    UploadCostActivity activity;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar1 = Calendar.getInstance();
    Calendar myCalendar7 = Calendar.getInstance();
    Calendar myCalendarf = Calendar.getInstance();
    public ProgressDialog dialog;
    Api api = ApiUtils.getAPIService();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_borrow, container, false);
        activity = (UploadCostActivity) getActivity();

        et_money3 = view.findViewById(R.id.et_money3);
        et_category3 = view.findViewById(R.id.et_category3);
        et_date3 = view.findViewById(R.id.et_date3);
        et_des3 = view.findViewById(R.id.et_des3);
        et_nameborrow = view.findViewById(R.id.et_nameborrow);
        et_addressborrow = view.findViewById(R.id.et_addressborrow);
        et_phoneborrow = view.findViewById(R.id.et_phoneborrow);
        et_repay_date1 = view.findViewById(R.id.et_repay_date1);
        button_save3 = view.findViewById(R.id.button_save3);

        myCalendar7.add(myCalendar7.DATE, -7);

        et_category3.setText("Khoản nợ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        et_date3.setText(dateFormat.format(new Date()));


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
        et_date3.setOnClickListener(new View.OnClickListener() {
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
        et_repay_date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              DatePickerDialog datePickerDialog1 =  new DatePickerDialog(activity, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH));
                datePickerDialog1.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                datePickerDialog1.show();
            }
        });
        et_money3.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                et_money3.setSelectAllOnFocus(true);
            }
        });
        et_money3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_money3.getText().length() == 0){
                    et_money3.append("0");
                }
            }
        });

        button_save3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = getDate(et_date3.getText().toString());
                String category = et_category3.getText().toString();
                Double money = et_money3.getNumericValue(); //Double.parseDouble(et_money3.getText().toString());
                String categoryFlag = "n";
                String des = et_des3.getText().toString();
                String nameborrow = et_nameborrow.getText().toString();
                String addressborrow = et_addressborrow.getText().toString();
                String phoneborrow = et_phoneborrow.getText().toString();

                //String dateRepay1 = getDate(et_repay_date1.getText().toString());
                String dateRepay1 = "";
                if(et_repay_date1.getText().toString() != "")
                    dateRepay1 = getDate(et_repay_date1.getText().toString());

                int matk = getArguments().getInt("ma_tk");
                if(money <=0 ){
                    et_money3.setError("Số tiền phải lớn hơn 0đ");
                    et_money3.requestFocus();
                    return;
                }
                if(money > 9999999999.99){
                    et_money3.setError("Vượt quá hạn mức giao dịch 9,999,999,999.99đ");
                    et_money3.requestFocus();
                    return;
                }
                if(category.isEmpty()){
                    et_category3.setError("Hạng mục không được để trống");
                    et_category3.requestFocus();
                    return;
                }
                if(nameborrow.isEmpty()){
                    et_nameborrow.setError("Tên chủ nợ không được để trống");
                    et_nameborrow.requestFocus();
                    return;
                }
                if(!phoneborrow.isEmpty() && phoneborrow.length() < 10)
                {
                    et_phoneborrow.setError("số điện thoại phải đủ 10 số");
                    et_phoneborrow.requestFocus();
                    return;
                }
                if(dateRepay1.isEmpty()){
                    et_repay_date1.setError("_");
                    et_repay_date1.requestFocus();
                    CustomToast.makeText(activity,"Ngày trả nợ không được để trống", CustomToast.LENGTH_LONG,CustomToast.WARNING,true).show();
                    return;
                }
                if(!dateRepay1.isEmpty() && dateRepay1.compareTo(date) < 0){
                    et_repay_date1.setError("_");
                    CustomToast.makeText(activity,"Ngày trả nợ không trước ngày " + date, CustomToast.LENGTH_LONG,CustomToast.WARNING,true).show();
                    return;
                }
                if(des.compareTo("") == 0){
                    des = "Vay tiền của " + nameborrow;
                }
                ///////////
                /////////////////////////Validation? Bro
                ///////////////////
                final VayNo vayNo = new VayNo(date, category, categoryFlag, money, des, matk, dateRepay1, nameborrow, addressborrow, phoneborrow);
                new AlertDialog.Builder(activity)
                        .setTitle("Thông báo!")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("Bạn chắc chắn muốn thêm khoản nợ này không?")

                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showDialog();
                                api.LoanBorrowadd(vayNo).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        hideDialog();
                                        if(response.code() == 201){
                                            CustomToast.makeText(activity,"Thêm khoản nợ thành công", CustomToast.LENGTH_LONG,CustomToast.SUCCESS,true).show();
                                            et_money3.setText("0");
                                            et_category3.setText("Khoản nợ");
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                            et_date3.setText(dateFormat.format(new Date()));
                                            et_des3.setText("");
                                            et_nameborrow.setText("");
                                            et_addressborrow.setText("");
                                            et_phoneborrow.setText("");
                                            et_repay_date1.setText("");
                                            et_repay_date1.setError(null);
                                        }
                                        if(response.code() == 500){
                                            CustomToast.makeText(activity,"Thêm khoản nợ không thành công", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        hideDialog();
                                        CustomToast.makeText(activity, "Có lỗi không xác định! Vui lòng thử lại sau", Toast.LENGTH_LONG, CustomToast.CONFUSING, true).show();
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

        et_date3.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel1() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String temp = sdf.format(myCalendar1.getTime());
        if(getDate(temp).compareTo(getDate(et_date3.getText().toString())) < 0){
            et_repay_date1.setError("_");
            CustomToast.makeText(activity,"Ngày trả nợ không trước ngày " + et_date3.getText().toString(), CustomToast.LENGTH_LONG,CustomToast.WARNING,true).show();
            et_repay_date1.setText(temp);
        }
        else {
            et_repay_date1.setError(null);
            et_repay_date1.setText(temp);

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
