package com.example.qlchitieu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.models.DangKyInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    EditText et_email, et_username, et_phone, et_password, et_confirm_password;
    Button button_signup;
    Toolbar toolbar;
    private ProgressDialog dialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setControl();
        setEvent();
    }

    private void setEvent() {
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String hoten = et_username.getText().toString().trim();
                String dienthoai = et_phone.getText().toString().trim();
                String matkhau = et_password.getText().toString().trim();
                String xacnhanpass = et_confirm_password.getText().toString().trim();
                if(email.isEmpty()){
                    et_email.setError("Email trống");
                    et_email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    et_email.setError("Email không hợp lệ");
                    et_email.requestFocus();
                    return;
                }
                if(hoten.isEmpty()){
                    et_username.setError("Họ tên không được để trống");
                    et_username.requestFocus();
                    return;
                }
                if(!dienthoai.isEmpty() && dienthoai.length() < 10)
                {
                    et_phone.setError("Số điện thoại phải đủ 10 số");
                    et_phone.requestFocus();
                    return;
                }
                if(matkhau.isEmpty()){
                    et_password.setError("Mật khẩu trống");
                    et_password.requestFocus();
                    return;
                }
                if(matkhau.length() < 6 || matkhau.length() > 16){
                    et_password.setError("Mật khẩu phải trong khoảng 6~16 ký tự");
                    et_password.requestFocus();
                    return;
                }
                if(matkhau.compareTo(xacnhanpass) != 0 ){
                    et_confirm_password.setError("Xác nhận mật khẩu không khớp");
                    et_confirm_password.requestFocus();
                    return;
                }
                showDialog();
                final DangKyInfo dangKyInfo = new DangKyInfo(
                        email, matkhau, hoten, dienthoai
                );
                Api api = ApiUtils.getAPIService();
                api.userSignup(dangKyInfo).enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        hideDialog();
                        if(response.isSuccessful()){
                            CustomToast.makeText(SignupActivity.this, "Đăng ký thành công! Đăng nhập để tiếp tục"
                                    , CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();
                            onBackPressed();
                        }
                        else{
                            if(response.code() == 409){
                                CustomToast.makeText(SignupActivity.this, "Email đã tồn tại", CustomToast.LENGTH_LONG, CustomToast.WARNING, true).show();
                            }
                            if(response.code() == 500){
                                CustomToast.makeText(SignupActivity.this, "Đăng ký không thành công", CustomToast.LENGTH_LONG, CustomToast.ERROR, true).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideDialog();
                        CustomToast.makeText(SignupActivity.this, "Không có kết nối! Vui lòng thử lại sau", CustomToast.LENGTH_LONG, CustomToast.CONFUSING, true).show();
                    }
                });

            }
        });
    }

    private void setControl() {
        et_email = findViewById(R.id.et_email);
        et_username = findViewById(R.id.et_username);
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        button_signup = findViewById(R.id.button_signup);
        toolbar = findViewById(R.id.toolbar);
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
