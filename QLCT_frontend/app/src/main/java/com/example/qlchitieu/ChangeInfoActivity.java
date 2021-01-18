package com.example.qlchitieu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.models.NguoiDungUpdate;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeInfoActivity extends AppCompatActivity {
    EditText ed_name, ed_phone, ed_address, ed_job;
    RadioGroup rg_gender;
    RadioButton rd_male, rd_female;
    Button button_update;
    Toolbar toolbar3;
    private ProgressDialog dialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        setControl();
        setEvent();
    }
    private void setEvent(){
        toolbar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        final Intent intent = getIntent();
        ed_name.setText(intent.getStringExtra("name"));
        if(intent.getStringExtra("gender").equals("Nam")){
          rd_male.setChecked(true);
        }
        else{
            rd_female.setChecked(true);
        }
        ed_phone.setText(intent.getStringExtra("phone"));
        ed_job.setText(intent.getStringExtra("job"));
        ed_address.setText(intent.getStringExtra("address"));
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api api = ApiUtils.getAPIService();
                ArrayList<NguoiDungUpdate> listInfo = new ArrayList<>();
                if(ed_name.getText().toString().trim().isEmpty()){
                    ed_name.setError("Họ tên không được để trống");
                    ed_name.requestFocus();
                    return;
                }
                if(ed_job.getText().toString().length() > 50){
                    ed_job.setError("Nghề nghiệp không hợp lệ");
                    ed_job.requestFocus();
                    return;
                }
                if(!Patterns.PHONE.matcher(ed_phone.getText()).matches())
                {
                    ed_phone.setError("Số điện thoại không hợp lệ");
                    ed_phone.requestFocus();
                    return;
                }
                listInfo.add(new NguoiDungUpdate("hoten", ed_name.getText().toString()));
                if(rd_male.isChecked())
                    listInfo.add(new NguoiDungUpdate("gioitinh", "m"));
                else
                    listInfo.add(new NguoiDungUpdate("gioitinh", "f"));
                listInfo.add(new NguoiDungUpdate("sodienthoai", ed_phone.getText().toString()));
                listInfo.add(new NguoiDungUpdate("diachi", ed_address.getText().toString()));
                listInfo.add(new NguoiDungUpdate("nghenghiep", ed_job.getText().toString()));
                showDialog();
                api.updateUser(intent.getIntExtra("ma_nguoidung", -1), listInfo).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        hideDialog();
                        if(response.code() == 200){
                            CustomToast.makeText(ChangeInfoActivity.this, "Thay đổi thông tin thành công", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();
                            onBackPressed();
                        }
                        if(response.code() == 500)
                            CustomToast.makeText(ChangeInfoActivity.this, "Thay đổi thông tin thất bại", CustomToast.LENGTH_LONG, CustomToast.ERROR, true).show();

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideDialog();
                        CustomToast.makeText(ChangeInfoActivity.this, "Thay đổi thông tin không thành công", CustomToast.LENGTH_LONG, CustomToast.CONFUSING, true).show();
                    }
                });
            }
        });
    }
    private void setControl(){
        ed_name = findViewById(R.id.ed_name);
        ed_phone = findViewById(R.id.ed_phone);
        ed_job = findViewById(R.id.ed_job);
        ed_address = findViewById(R.id.ed_address);
        button_update = findViewById(R.id.button_update);
        toolbar3= findViewById(R.id.toolbar3);
        rg_gender = findViewById(R.id.toolbar);
        rd_male = findViewById(R.id.radioButton_male);
        rd_female = findViewById(R.id.radioButton_female);
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
