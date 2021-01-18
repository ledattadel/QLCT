package com.example.qlchitieu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("saveData", MODE_PRIVATE);
        final int mand = sharedPreferences.getInt("ma_nguoidung", -1);
        final int matk = sharedPreferences.getInt("ma_taikhoan",-1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mand > 0 && matk > 0){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }
}
