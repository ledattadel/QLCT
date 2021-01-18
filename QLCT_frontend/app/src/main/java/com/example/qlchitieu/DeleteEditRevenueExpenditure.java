package com.example.qlchitieu;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cottacush.android.currencyedittext.CurrencyEditText;
import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.models.PhatSinh;
import com.example.qlchitieu.models.PhatSinh1;
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

public class DeleteEditRevenueExpenditure extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    EditText et_category_de, et_date_de, et_des_de;
    CurrencyEditText et_money_de;
    Button button_save_de;
    ImageView bt_exit_de, bt_edit_de, bt_delete_de;
    LinearLayout lay_de;
    final Calendar myCalendar = Calendar.getInstance();
    Api api = ApiUtils.getAPIService();
    int maps;
    PhatSinh1 phatSinh1;
    private ProgressDialog dialog;
    private Double sodu1;
    double money;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_edit_expenditure_revenue);
        setControl();
        setEvent();
    }

    private void setEvent(){
        sharedPreferences = getSharedPreferences("saveData", MODE_PRIVATE);
        final int matk = sharedPreferences.getInt("ma_taikhoan", -1);
        bt_exit_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
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

        et_date_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(lay_de.getVisibility() == View.VISIBLE) {
//                    new DatePickerDialog(DeleteEditRevenueExpenditure.this, date, myCalendar
//                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                }
//                else
                    return;
            }
        });

        et_money_de.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                et_money_de.setSelectAllOnFocus(true);
            }
        });
        et_money_de.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_money_de.getText().length() == 0){
                    et_money_de.append("0");
                }
            }
        });
        final Intent intent = getIntent();
        maps = intent.getIntExtra("ma_phatsinh",-1);
        showDialog();
        api.getOneRE(maps).enqueue(new Callback<PhatSinh1>() {
            @Override
            public void onResponse(Call<PhatSinh1> call, Response<PhatSinh1> response) {
                hideDialog();
                if(response.code() == 200){
                    phatSinh1 = response.body();
                    if(phatSinh1.getLoaiphatsinh().compareTo("t") == 0){
                        et_money_de.setTextColor(Color.parseColor("#43A047"));
                    }
                    else et_money_de.setTextColor(Color.parseColor("#F44336"));
                     money = Double.parseDouble(phatSinh1.getSotien());
                    et_money_de.setText(String.format(java.util.Locale.US,"%,.2f", money));
                    et_category_de.setText(phatSinh1.getNoidung());
                    et_date_de.setText(getDate(phatSinh1.getNgay()));
                    if(phatSinh1.getMota() == ""){
                        et_des_de.setHint("");
                    }
                    else
                        et_des_de.setText(phatSinh1.getMota());
                }
                if(response.code() == 500 || response.code() == 404){
                    CustomToast.makeText(DeleteEditRevenueExpenditure.this,"Khoản phát sinh không xác định", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<PhatSinh1> call, Throwable t) {
                hideDialog();
                Toast.makeText(DeleteEditRevenueExpenditure.this, "Có lỗi không xác định! Vui lòng thử lại sau", Toast.LENGTH_LONG).show();
            }
        });
        api.getBalace1(matk).enqueue(new Callback<TaiKhoan1>() {
            @Override
            public void onResponse(Call<TaiKhoan1> call, Response<TaiKhoan1> response) {
                if(response.code() == 200)
                {
                    TaiKhoan1 taiKhoan1 = response.body();
                    sodu1 = taiKhoan1.getSodu();
                }
                if (response.code() == 500){
                    CustomToast.makeText(DeleteEditRevenueExpenditure.this,"Lấy số dư không thành công", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan1> call, Throwable t) {

            }
        });
        bt_edit_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phatSinh1.getNgay().compareTo(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) == 0){
                et_money_de.setFocusableInTouchMode(true);
                et_category_de.setFocusableInTouchMode(true);
                //    et_date_de.setFocusableInTouchMode(true);
                et_des_de.setFocusableInTouchMode(true);
                et_des_de.setHint("Mô tả");
                lay_de.setVisibility(View.VISIBLE);
                }
                else
                    new AlertDialog.Builder(DeleteEditRevenueExpenditure.this)
                            .setTitle("Thông báo!")
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage("Chỉ có thể hiệu chỉnh các khoản phát sinh trong ngày hôm nay")

                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
            }
        });
        bt_delete_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_money_de.getNumericValue() > sodu1 && phatSinh1.getLoaiphatsinh().compareTo("t") == 0)
                {
                    if(et_money_de.getNumericValue() > sodu1){
                        new AlertDialog.Builder(DeleteEditRevenueExpenditure.this)
                                .setTitle("Thông báo!")
                                .setIcon(R.mipmap.ic_launcher)
                                .setMessage("Vi phạm ràng buộc số dư âm")

                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                    return;
                }
                if(phatSinh1.getNgay().compareTo(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) == 0) {
                    new AlertDialog.Builder(DeleteEditRevenueExpenditure.this)
                            .setTitle("Thông báo!")
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage("Bạn có muốn xóa phát sinh giao dịch này không?")

                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    api.deleteOne(maps).enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if(response.code() == 200) {
                                                CustomToast.makeText(DeleteEditRevenueExpenditure.this, "Xóa thành công", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();
                                                onBackPressed();
                                            }
                                            if(response.code() == 500){
                                                CustomToast.makeText(DeleteEditRevenueExpenditure.this,"Xóa thất bại", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                                                return;
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            CustomToast.makeText(DeleteEditRevenueExpenditure.this, "Có lỗi không xác định! Vui lòng thử lại sau", CustomToast.LENGTH_LONG, CustomToast.CONFUSING, true).show();
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
                else{
                    new AlertDialog.Builder(DeleteEditRevenueExpenditure.this)
                            .setTitle("Thông báo!")
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage("Chỉ có thể xóa các khoản phát sinh trong ngày hôm nay")

                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                }


            }
        });
        button_save_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //System.out.println(getDate(et_date_de.getText().toString()));
                if(et_money_de.getNumericValue() <=0 ){
                    et_money_de.setError("Số tiền phải lớn hơn 0đ");
                    et_money_de.requestFocus();
                    return;
                }
                if((et_money_de.getNumericValue() - money ) > sodu1 && phatSinh1.getLoaiphatsinh().compareTo("c")==0) {
                    et_money_de.setError("Vượt quá số dư hiện tại");
                    et_money_de.requestFocus();
                    return;
                }
                if((money - et_money_de.getNumericValue() ) > sodu1 && phatSinh1.getLoaiphatsinh().compareTo("t")==0){
                    et_money_de.setError("Vi phạm ràng buộc số dư âm");
                    et_money_de.requestFocus();
                    return;
                }
                if(et_money_de.getNumericValue() > 9999999999.99){
                    et_money_de.setError("Vượt quá hạn mức giao dịch 9,999,999,999.99đ");
                    et_money_de.requestFocus();
                    return;
                }
                if(et_category_de.getText().toString().isEmpty()){
                    et_category_de.setError("Hạng mục không được để trống");
                    et_category_de.requestFocus();
                    return;
                }
                if(et_category_de.getText().toString().length() > 30){
                    et_category_de.setError("Hạng mục không quá 30 ký tự");
                    et_category_de.requestFocus();
                    return;
                }
                PhatSinh phatSinh = new PhatSinh(getDate1(et_date_de.getText().toString()), et_category_de.getText().toString(), phatSinh1.getLoaiphatsinh()
                        ,et_money_de.getNumericValue(),et_des_de.getText().toString(),phatSinh1.getMa_taikhoan());
                showDialog();
                api.editOneRE(maps, phatSinh).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        hideDialog();
                        if(response.code() == 200) {
                            CustomToast.makeText(DeleteEditRevenueExpenditure.this, "Cập nhật thành công", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();
                            onBackPressed();
                        }
                        if(response.code() == 500){
                            CustomToast.makeText(DeleteEditRevenueExpenditure.this,"Cập nhật thất bại", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                            return;
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideDialog();
                        CustomToast.makeText(DeleteEditRevenueExpenditure.this, "Có lỗi không xác định! Vui lòng thử lại sau", CustomToast.LENGTH_LONG, CustomToast.CONFUSING, true).show();
                    }
                });
            }
        });
    }
    private String getDate(String datein){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObject;
        String date;

        try{

            dateObject = formatter.parse(datein);
            date = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);

            return date;
        }
        catch (java.text.ParseException e)
        {
            e.printStackTrace();
            Log.i("E11111111111", e.toString());
            return "";
        }

    }

    private String getDate1(String datein){
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
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_date_de.setText(sdf.format(myCalendar.getTime()));
    }

    private void setControl(){
        toolbar = findViewById(R.id.toolbar_de);
        et_money_de = findViewById(R.id.et_money_de);
        et_category_de = findViewById(R.id.et_category_de);
        et_date_de = findViewById(R.id.et_date_de);
        et_des_de = findViewById(R.id.et_des_de);
        bt_exit_de = findViewById(R.id.bt_exit_de);
        button_save_de =findViewById(R.id.button_save_de);
        lay_de = findViewById(R.id.lay_de);
        bt_edit_de = findViewById(R.id.bt_edit_de);
        bt_delete_de = findViewById(R.id.bt_delete_de);
    }
    public void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
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
