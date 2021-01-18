package com.example.qlchitieu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.models.DangNhap;
import com.example.qlchitieu.models.NguoiDung;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    Button button_forgot_password, button_signin, button_signup;
    EditText et_password, et_username;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setControl();
        setEvent();
    }

    private void setEvent() {

        sharedPreferences = getSharedPreferences("saveData", MODE_PRIVATE);
        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tennguoidung = et_username.getText().toString().trim();
                String matkhau = et_password.getText().toString().trim();

                if (tennguoidung.isEmpty()) {
                    et_username.setError("Email trống");
                    et_username.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(tennguoidung).matches()) {
                    et_username.setError("Email không hợp lệ");
                    et_username.requestFocus();
                    return;
                }
                if (matkhau.isEmpty()) {
                    et_password.setError("Mật khẩu trống");
                    et_password.requestFocus();
                    return;
                }
                final NguoiDung nguoidung = new NguoiDung(
                        tennguoidung,
                        matkhau
                );
                showDialog();
                Api api = ApiUtils.getAPIService();
                api.userLogin(nguoidung).enqueue(new Callback<DangNhap>() {
                    @Override
                    public void onResponse(Call<DangNhap> call, Response<DangNhap> response) {
                        hideDialog();
                        if (response.isSuccessful()) {
                            DangNhap dangNhap = response.body();
                            editor = sharedPreferences.edit();
                            editor.putInt("ma_nguoidung", dangNhap.getMaNguoidung());
                            editor.putInt("ma_taikhoan", dangNhap.getMaTaikhoan());
                            editor.commit();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            finish();
                            startActivity(i);
                        } else {
                            CustomToast.makeText(LoginActivity.this, "Email hoặc mật khẩu không chính xác", CustomToast.LENGTH_LONG, CustomToast.ERROR, true).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<DangNhap> call, Throwable t) {
                        hideDialog();
                        CustomToast.makeText(LoginActivity.this, "Không có kết nối! Vui lòng thử lại sau", CustomToast.LENGTH_LONG, CustomToast.CONFUSING, true).show();

                    }
                });

            }
        });
        button_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(i1);
            }
        });
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(i);
            }
        });

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

    private void setControl() {
        button_forgot_password = findViewById(R.id.button_forgot_password);
        button_signin = findViewById(R.id.button_signin);
        button_signup = findViewById(R.id.button_signup);
        et_password = findViewById(R.id.et_password);
        et_username = findViewById(R.id.et_username);
    }


}
