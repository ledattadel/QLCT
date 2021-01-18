package com.example.qlchitieu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.fragments.BorrowLoanListFragment;
import com.example.qlchitieu.fragments.ReportFragment;
import com.example.qlchitieu.fragments.TransactionFragment;
import com.example.qlchitieu.fragments.UserFragment;
import com.example.qlchitieu.models.ThongBao;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FloatingActionButton floating_button;
    BottomNavigationView bottomNavigation;
    final Calendar myCalendar = Calendar.getInstance();
    int noti;
    BadgeDrawable badgeDrawable;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();
    }
    void setEvent() {
        badgeDrawable = bottomNavigation.getOrCreateBadge(R.id.navigation_vayno);
        sharedPreferences = getSharedPreferences("saveData", MODE_PRIVATE);
        final int mand = sharedPreferences.getInt("ma_nguoidung", -1);
        final int matk = sharedPreferences.getInt("ma_taikhoan", -1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());

        Api api = ApiUtils.getAPIService();
        api.getNotification(date+"&"+matk).enqueue(new Callback<ThongBao>() {
            @Override
            public void onResponse(Call<ThongBao> call, Response<ThongBao> response) {
                if(response.code() == 200)
                {
                   ThongBao thongBao = response.body();
                   noti = thongBao.getThongbao();

                    badgeDrawable.setNumber(noti);
                    if(noti > 0)
                        badgeDrawable.setVisible(true);
                    else
                        badgeDrawable.setVisible(false);
                }
                else {
                    badgeDrawable.setVisible(false);
                    return;
                }

            }

            @Override
            public void onFailure(Call<ThongBao> call, Throwable t) {
                    return;
            }
        });

        final TransactionFragment fragment1 = new TransactionFragment();
        final ReportFragment fragment2 = new ReportFragment();
        final BorrowLoanListFragment fragment3 = new BorrowLoanListFragment();
        final UserFragment fragment4 = new UserFragment();
        final FragmentManager fm = getSupportFragmentManager();
        final Fragment[] active = {fragment1};
       fm.beginTransaction().add(R.id.fragment_container, fragment4, "4").hide(fragment4).commit();
       fm.beginTransaction().add(R.id.fragment_container, fragment3, "3").hide(fragment3).commit();
       fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment1, "1").commit();
        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.navigation_giaodich:
                                fm.beginTransaction().hide(active[0]).show(fragment1).commit();
                                active[0] = fragment1;
                              //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, transactionFragment).commit();

                                return true;
                            case R.id.navigation_thongke:
                               fm.beginTransaction().hide(active[0]).show(fragment2).commit();
                                active[0] = fragment2;
                                return true;
                            case R.id.navigation_vayno:
                                    BadgeDrawable badgeDrawable1 = bottomNavigation.getBadge(R.id.navigation_vayno);
                                    badgeDrawable1.clearNumber();
                                    badgeDrawable1.setVisible(false);
                              fm.beginTransaction().hide(active[0]).show(fragment3).commit();
                                active[0] = fragment3;
                                return true;
                            case R.id.navigation_taikhoan:
                                fm.beginTransaction().hide(active[0]).show(fragment4).commit();
                                active[0] = fragment4;
                          //      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, userFragment).commit();
                                return true;
                        }
                        return false;
                    }
                };
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        floating_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UploadCostActivity.class);
                startActivity(intent);
            }
        });
    }

    void setControl() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        floating_button = findViewById(R.id.floating_button);
    }
}
