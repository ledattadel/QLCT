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
import android.widget.Toast;

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
import com.example.qlchitieu.models.TaiKhoan1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenditureFragment extends Fragment {
    EditText  et_category1, et_date1, et_des1;
    CurrencyEditText et_money1;
    ImageView im_ask;
    Button button_save1;
    UploadCostActivity activity;
    final Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar7 = Calendar.getInstance();
    Calendar myCalendarf = Calendar.getInstance();

    public ProgressDialog dialog;
    private Api api = ApiUtils.getAPIService();
    private Double sodu;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expenditure, container, false);
        activity = (UploadCostActivity) getActivity();

        et_money1 = view.findViewById(R.id.et_money1);
        et_category1 = view.findViewById(R.id.et_category1);
        et_date1 = view.findViewById(R.id.et_date1);
        et_des1 = view.findViewById(R.id.et_des1);
        button_save1 = view.findViewById(R.id.button_save1);
        im_ask = view.findViewById(R.id.im_ask);

        final int matk = getArguments().getInt("ma_tk");
        myCalendar7.add(myCalendar7.DATE,-7);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        et_date1.setText(dateFormat.format(new Date()));
        im_ask.setTooltipText("Hạng mục chi ví dụ: Nhà hàng, Giải trí, Di chuyển...");

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

        et_date1.setOnClickListener(new View.OnClickListener() {
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
        et_money1.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                et_money1.setSelectAllOnFocus(true);
            }
        });
        et_money1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_money1.getText().length() == 0){
                    et_money1.append("0");
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
        button_save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = getDate();
                String category = et_category1.getText().toString();
                Double money = et_money1.getNumericValue(); //Double.parseDouble(et_money1.getText().toString());
                String categoryFlag = "c";
                String des = et_des1.getText().toString();
                if(money <= 0)
                {

                    et_money1.setError("Số tiền phải lớn hơn 0đ");
                    et_money1.requestFocus();
                    return;
                }
                if(money > sodu)
                {

                    et_money1.setError("Số dư không đủ để tạo khoản chi này");
                    et_money1.requestFocus();
                    return;
                }
                if(money > 9999999999.99){
                    et_money1.setError("Vượt quá hạn mức giao dịch 9,999,999,999.99đ");
                    et_money1.requestFocus();
                    return;
                }
                if(category.isEmpty()){
                    et_category1.setError("Hạng mục không được để trống");
                    et_category1.requestFocus();
                    return;
                }
                if(category.length() > 30){
                    et_category1.setError("Hạng mục không quá 30 ký tự");
                    et_category1.requestFocus();
                    return;
                }
                ///////////
                /////////////////////////Validation? Bro
                ///////////////////
                final PhatSinh ps = new PhatSinh(date, category, categoryFlag, money, des, matk);
                new AlertDialog.Builder(activity)
                        .setTitle("Thông báo!")
                        .setIcon(R.mipmap.ic_launcher)
                        .setMessage("Bạn chắc chắn muốn thêm khoản chi này không?")

                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showDialog();
                                api.revenueExpenditureAdd(ps).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        hideDialog();
                                        if(response.code() == 201){
                                            CustomToast.makeText(activity,"Thêm khoản chi thành công",
                                                    CustomToast.LENGTH_LONG,CustomToast.SUCCESS,true).show();
                                            et_money1.setText("0");
                                            et_category1.setText("");
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                            et_date1.setText(dateFormat.format(new Date()));
                                            et_des1.setText("");
                                        }
                                        if(response.code() == 500){
                                            CustomToast.makeText(activity,"Thêm khoản chi không thành công",
                                                    CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
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

        et_date1.setText(sdf.format(myCalendar.getTime()));
    }
    private String getDate(){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObject;
        String date;

        try{
            String dob_var=(et_date1.getText().toString());
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
