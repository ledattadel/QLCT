package com.example.qlchitieu;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cottacush.android.currencyedittext.CurrencyEditText;
import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.models.PhatSinh1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeletePayActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText et_category_de, et_date_de, et_des_de;
    CurrencyEditText et_money_de;
    ImageView bt_exit_de, bt_delete_de;
    Api api = ApiUtils.getAPIService();
    int maps;
    PhatSinh1 phatSinh1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_pay);
        setControl();
        setEvent();
    }

    private void setEvent(){
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
        final Intent intent = getIntent();
        maps = intent.getIntExtra("ma_phatsinh",-1);
        api.getOneRE(maps).enqueue(new Callback<PhatSinh1>() {
            @Override
            public void onResponse(Call<PhatSinh1> call, Response<PhatSinh1> response) {
                if(response.code() == 200){
                    phatSinh1 = response.body();
                    if(phatSinh1.getLoaiphatsinh().compareTo("l") == 0 ){
                        et_money_de.setTextColor(Color.parseColor("#43A047"));
                    }
                    else et_money_de.setTextColor(Color.parseColor("#F44336"));
                    et_money_de.setText(phatSinh1.getSotien());
                    et_category_de.setText(phatSinh1.getNoidung());
                    et_date_de.setText(getDate(phatSinh1.getNgay()));
                    if(phatSinh1.getMota() == ""){
                        et_des_de.setHint("");
                    }
                    else
                        et_des_de.setText(phatSinh1.getMota());
                }
                if(response.code() == 500 || response.code() == 404){
                    CustomToast.makeText(DeletePayActivity.this,"Khoản phát sinh không xác định", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<PhatSinh1> call, Throwable t) {
                Toast.makeText(DeletePayActivity.this, "Có lỗi không xác định! Vui lòng thử lại sau", Toast.LENGTH_LONG).show();
            }
        });

        bt_delete_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(DeletePayActivity.this)
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
                                            CustomToast.makeText(DeletePayActivity.this, "Xóa thành công", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();
                                            onBackPressed();
                                        }
                                        if(response.code() == 500){
                                            CustomToast.makeText(DeletePayActivity.this,"Xóa thất bại", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                                            return;
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        CustomToast.makeText(DeletePayActivity.this, "Có lỗi không xác định! Vui lòng thử lại sau", CustomToast.LENGTH_LONG, CustomToast.CONFUSING, true).show();
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


    private void setControl(){
        toolbar = findViewById(R.id.toolbar_de);
        et_money_de = findViewById(R.id.et_money_de);
        et_category_de = findViewById(R.id.et_category_de);
        et_date_de = findViewById(R.id.et_date_de);
        et_des_de = findViewById(R.id.et_des_de);
        bt_exit_de = findViewById(R.id.bt_exit_de);
        bt_delete_de = findViewById(R.id.bt_delete_de);
    }
}
