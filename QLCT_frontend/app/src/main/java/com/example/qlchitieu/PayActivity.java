package com.example.qlchitieu;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.models.TaiKhoan1;
import com.example.qlchitieu.models.ThuTra;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private ImageView bt_back_pay;
    private EditText et_money_pay, et_category_pay, et_date_pay, et_des_pay;
    private TextView tv_title_pay;
    private Button bt_save_pay;
    final Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();
    Calendar myCalendar7 = Calendar.getInstance();
    Calendar myCalendarf = Calendar.getInstance();
    private Double sodu;
    public ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pay);
        setControl();
        setEvent();
    }

    private void setEvent() {
        sharedPreferences = getSharedPreferences("saveData", MODE_PRIVATE);
        final int matk = sharedPreferences.getInt("ma_taikhoan", -1);
        Intent intent = getIntent();
        final int maps = intent.getIntExtra("ma_phatsinh", -1);
        final double money = intent.getDoubleExtra("sotien", -1);
        final String cate = intent.getStringExtra("loaiphatsinh");
        String name = intent.getStringExtra("ten");
        String date1 = intent.getStringExtra("ngay");
        myCalendar7.add(myCalendar7.DATE, -7);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date datea = dateFormat.parse(date1);
            myCalendar1.setTime(datea);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        et_date_pay.setText(dateFormat.format(new Date()));
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
        et_date_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             DatePickerDialog datePickerDialog =  new DatePickerDialog(PayActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(myCalendar7.compareTo(myCalendar1) > 0? myCalendar7.getTimeInMillis():myCalendar1.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(myCalendarf.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        bt_back_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        et_money_pay.setText(String.format(java.util.Locale.US, "%,.2f", money));
        if (cate.compareTo("v") == 0) {
            tv_title_pay.setText("Thu nợ");
            et_category_pay.setText("Thu nợ");
            et_des_pay.setText("Thu nợ từ " + name);
            et_money_pay.setTextColor(Color.parseColor("#43A047"));
        } else {
            et_category_pay.setText("Trả nợ");
            et_des_pay.setText("Trả nợ cho " + name);
            et_money_pay.setTextColor(Color.parseColor("#F44336"));
        }

        final Api api = ApiUtils.getAPIService();
        api.getBalace1(matk).enqueue(new Callback<TaiKhoan1>() {
            @Override
            public void onResponse(Call<TaiKhoan1> call, Response<TaiKhoan1> response) {
                if (response.code() == 200) {
                    TaiKhoan1 taiKhoan1 = response.body();
                    sodu = taiKhoan1.getSodu();
                }
                if (response.code() == 500) {
                    CustomToast.makeText(PayActivity.this, "Lấy số dư không thành công", CustomToast.LENGTH_LONG, CustomToast.ERROR, true).show();
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan1> call, Throwable t) {

            }
        });
        bt_save_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cate.compareTo("n") == 0 && money > sodu) {
                    new AlertDialog.Builder(PayActivity.this)
                            .setTitle("Thông báo!")
                            .setMessage("Số dư không đủ để trả nợ")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    return;
                                }
                            }).show();
                } else {
                    final ThuTra thuTra = new ThuTra(getDate(et_date_pay.getText().toString()), et_category_pay.getText().toString(), cate.compareTo("v") == 0 ? "l" : "d",
                            money, et_des_pay.getText().toString(), matk, maps);
                    new AlertDialog.Builder(PayActivity.this)
                            .setTitle("Thông báo!")
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage("Bạn chắc chắn muốn thêm " + (cate.compareTo("v") == 0 ? "khoản thu nợ" : "khoản trả nợ") + " này không?")

                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showDialog();
                                    api.payAdd(thuTra).enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.code() == 201) {
                                                if (cate == "v")
                                                    CustomToast.makeText(PayActivity.this, "Thêm thu nợ thành công", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();
                                                else
                                                    CustomToast.makeText(PayActivity.this, "Thêm trả nợ thành công", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();
                                                onBackPressed();
                                            }
                                            if (response.code() == 500) {
                                                if (cate == "v")
                                                    CustomToast.makeText(PayActivity.this, "Thêm thu nợ thành công", CustomToast.LENGTH_LONG, CustomToast.ERROR, true).show();
                                                else
                                                    CustomToast.makeText(PayActivity.this, "Thêm trả nợ thành công", CustomToast.LENGTH_LONG, CustomToast.ERROR, true).show();
                                                return;
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            CustomToast.makeText(PayActivity.this, "Có lỗi không xác định! Vui lòng thử lại sau", Toast.LENGTH_LONG, CustomToast.CONFUSING, true).show();
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
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_date_pay.setText(sdf.format(myCalendar.getTime()));
    }

    private String getDate(String datein) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObject;
        String date;

        try {

            dateObject = formatter.parse(datein);
            date = new SimpleDateFormat("yyyy-MM-dd").format(dateObject);

            return date;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            Log.i("E11111111111", e.toString());
            return "";
        }

    }

    public void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(PayActivity.this);
        }
        dialog.setMessage("Chờ một chút...");
        dialog.show();

    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void setControl() {
        bt_back_pay = findViewById(R.id.bt_back_pay);
        et_money_pay = findViewById(R.id.et_money_pay);
        et_category_pay = findViewById(R.id.et_category_pay);
        et_date_pay = findViewById(R.id.et_date_pay);
        et_des_pay = findViewById(R.id.et_des_pay);
        bt_save_pay = findViewById(R.id.button_save_pay);
        tv_title_pay = findViewById(R.id.tv_title_pay);
    }
}
