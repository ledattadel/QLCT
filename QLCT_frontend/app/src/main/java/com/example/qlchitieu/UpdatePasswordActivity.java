package com.example.qlchitieu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.models.MatKhau;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordActivity extends AppCompatActivity {
    EditText et_oldpass, et_newpass, et_confirm_newpass;
    Toolbar toolbar4;
    Button button_update_pass;
    private ProgressDialog dialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        setControl();
        setEvent();
    }

    private void setEvent(){
        toolbar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final Intent intent = getIntent();
        button_update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String matkhau = et_oldpass.getText().toString();
                String matkhaumoi = et_newpass.getText().toString();
                String xacnhan = et_confirm_newpass.getText().toString();
                if(matkhau.isEmpty()){
                    et_oldpass.setError("Vui lòng nhập mật khẩu");
                    et_oldpass.requestFocus();
                    return;
                }
                if(matkhaumoi.isEmpty()){
                    et_oldpass.setError("Vui lòng nhập mật khẩu mới");
                    et_oldpass.requestFocus();
                    return;
                }

                if(matkhaumoi.length() < 6 || matkhaumoi.length() > 16){
                        et_newpass.setError("Mật khẩu phải trong khoảng 6~16 ký tự");
                        et_newpass.requestFocus();
                        return;
                }
                if(matkhau.compareTo(matkhaumoi) == 0){
                    et_newpass.setError("Mật khẩu này phải khác mật khẩu cũ");
                    et_newpass.requestFocus();
                    return;
                }
                if(xacnhan.isEmpty()){
                    et_confirm_newpass.setError("Chưa nhập xác nhận mật khẩu");
                    et_confirm_newpass.requestFocus();
                    return;
                }
                if(matkhaumoi.compareTo(xacnhan) != 0){
                    et_confirm_newpass.setError("Xác nhận mật khẩu không đúng");
                    et_confirm_newpass.requestFocus();
                    return;
                }
                showDialog();
                Api api = ApiUtils.getAPIService();
                MatKhau mk = new MatKhau(matkhau, matkhaumoi);
                api.updatePass(intent.getIntExtra("ma_nguoidung", -1), mk).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        hideDialog();
                        if(response.code() == 200){
                            CustomToast.makeText(UpdatePasswordActivity.this, "Đổi mật khẩu thành công", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();
                            onBackPressed();
                        }
                        if(response.code() == 401)
                        {
                            et_oldpass.setError("Mật khẩu cũ không đúng");
                            et_oldpass.requestFocus();
                            return;
                        }
                        if(response.code() == 500)
                            CustomToast.makeText(UpdatePasswordActivity.this, "Đổi mật khẩu thất bại", CustomToast.LENGTH_LONG,
                                    CustomToast.ERROR, true).show();
                        if(response.code() == 404)
                        CustomToast.makeText(UpdatePasswordActivity.this, "Có lỗi hệ thống! Vui lòng thử lại sau", CustomToast.LENGTH_LONG,
                                CustomToast.ERROR, true).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideDialog();
                        CustomToast.makeText(UpdatePasswordActivity.this, "Không thành công! Vui lòng kiểm tra kết nối internet", CustomToast.LENGTH_LONG,
                                CustomToast.CONFUSING, true).show();
                    }
                });
            }
        });
    }
    private void setControl(){
        et_oldpass = findViewById(R.id.et_oldpass);
        et_newpass = findViewById(R.id.et_newpass);
        et_confirm_newpass = findViewById(R.id.et_confirm_newpass);
        toolbar4 = findViewById(R.id.toolbar4);
        button_update_pass = findViewById(R.id.button_update_pass);
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
