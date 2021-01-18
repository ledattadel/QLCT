package com.example.qlchitieu.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qlchitieu.ChangeInfoActivity;
import com.example.qlchitieu.MainActivity;
import com.example.qlchitieu.R;
import com.example.qlchitieu.SplashActivity;
import com.example.qlchitieu.UpdatePasswordActivity;
import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.models.ThongTin_NguoiDung;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserFragment extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView tv_name, tv_name1, tv_gender, tv_job, tv_phone, tv_address;
    ImageView dropdown_menu;
    SwipeRefreshLayout pullToRefresh;
    MainActivity activity;
    int mand;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        activity = (MainActivity) getActivity();
        tv_name = view.findViewById(R.id.tv_name);
        tv_name1 = view.findViewById(R.id.tv_name1);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_job = view.findViewById(R.id.tv_job);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_address = view.findViewById(R.id.tv_address);
        dropdown_menu = view.findViewById(R.id.dropdown_menu);

        sharedPreferences = activity.getSharedPreferences("saveData", Context.MODE_PRIVATE);
        mand = sharedPreferences.getInt("ma_nguoidung", -1);
        Api api = ApiUtils.getAPIService();


        api.getOneUser(mand).enqueue(new Callback<ThongTin_NguoiDung>() {
            @Override
            public void onResponse(Call<ThongTin_NguoiDung> call, Response<ThongTin_NguoiDung> response) {
                if (response.code() == 200) {
                    ThongTin_NguoiDung thongtin = response.body();
                    tv_name.setText(thongtin.getHoten());
                    tv_name1.setText(thongtin.getHoten());
                    if (thongtin.getGioitinh().equals("m"))
                        tv_gender.setText("Nam");
                    else
                        tv_gender.setText("Nữ");

                    tv_job.setText(thongtin.getNghenghiep());
                    tv_phone.setText(thongtin.getSodienthoai());
                    tv_address.setText(thongtin.getDiachi());
                }
                if (response.code() == 404) {
                    Toast.makeText(activity, "Không tìm thấy thông tin người dùng", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ThongTin_NguoiDung> call, Throwable t) {
            //    Toast.makeText(activity, "Không thể tải thông tin người dùng", Toast.LENGTH_LONG).show();
            }
        });
        dropdown_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(activity, dropdown_menu);
                popup.getMenuInflater()
                        .inflate(R.menu.user_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.user_change:
                                Intent intent = new Intent(activity, ChangeInfoActivity.class);
                                intent.putExtra("name", tv_name1.getText());
                                intent.putExtra("gender", tv_gender.getText());
                                intent.putExtra("job", tv_job.getText());
                                intent.putExtra("phone", tv_phone.getText());
                                intent.putExtra("address", tv_address.getText());
                                intent.putExtra("ma_nguoidung", mand);
                                activity.startActivity(intent);
                                return true;
                            case R.id.password_change:
                                Intent intent1 = new Intent(activity, UpdatePasswordActivity.class);
                                intent1.putExtra("ma_nguoidung", mand);
                                activity.startActivity(intent1);
                                return true;
                            case R.id.user_logout:
                                new AlertDialog.Builder(activity)
                                        .setTitle("Chờ một chút...")
                                        .setMessage("Đăng xuất khỏi thiết bị?\n\nDữ liệu trên thiết bị này của bạn sẽ bị " +
                                                "xóa nhưng bạn có thể lấy lại khi đăng nhập lại")

                                        .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                editor = sharedPreferences.edit();
                                                editor.remove("ma_nguoidung");
                                                editor.remove("ma_taikhoan");
                                                editor.apply();
                                                Intent i = new Intent(activity, SplashActivity.class);
                                                activity.finish();
                                                startActivity(i);
                                            }
                                        })
                                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        }).show();
                                return true;

                        }
                        return false;
                    }
                });

                popup.show(); //showing p
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Api api = ApiUtils.getAPIService();
        //     api.getOneUser(Global.getProcessName())

        api.getOneUser(mand).enqueue(new Callback<ThongTin_NguoiDung>() {
            @Override
            public void onResponse(Call<ThongTin_NguoiDung> call, Response<ThongTin_NguoiDung> response) {
                if (response.code() == 200) {
                    ThongTin_NguoiDung thongtin = response.body();
                    tv_name.setText(thongtin.getHoten());
                    tv_name1.setText(thongtin.getHoten());
                    if (thongtin.getGioitinh().equals("m"))
                        tv_gender.setText("Nam");
                    else
                        tv_gender.setText("Nữ");

                    tv_job.setText(thongtin.getNghenghiep());
                    tv_phone.setText(thongtin.getSodienthoai());
                    tv_address.setText(thongtin.getDiachi());
                }
                if (response.code() == 404) {
                    Toast.makeText(activity, "Không tìm thấy thông tin người dùng", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ThongTin_NguoiDung> call, Throwable t) {
           //     Toast.makeText(activity, "Không thể tải thông tin người dùng", Toast.LENGTH_LONG).show();
            }
        });
    }
}