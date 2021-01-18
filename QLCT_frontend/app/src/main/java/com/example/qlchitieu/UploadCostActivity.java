package com.example.qlchitieu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qlchitieu.customs.CustomSpinnerAdapter;
import com.example.qlchitieu.fragments.BorrowFragment;
import com.example.qlchitieu.fragments.ExpenditureFragment;
import com.example.qlchitieu.fragments.LoanFragment;
import com.example.qlchitieu.fragments.RevenueFragment;
import com.example.qlchitieu.models.LoaiPhatSinh;

import java.util.ArrayList;

public class UploadCostActivity extends AppCompatActivity {
    private Toolbar toolbar_cost;
    ImageView bt_exit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int mand, matk;
    private Spinner sp_category;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_cost);
        setControl();
        setEvent();
    }
    private void setEvent(){
        sharedPreferences = getSharedPreferences("saveData", MODE_PRIVATE);
        matk = sharedPreferences.getInt("ma_taikhoan", -1);
        mand = sharedPreferences.getInt("ma_nguoidung", -1);
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (toolbar_cost != null) {
            setSupportActionBar(toolbar_cost);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
       addItemsToSpinner();

    }

    public void addItemsToSpinner() {

        ArrayList<LoaiPhatSinh> list = new ArrayList<LoaiPhatSinh>();
        list.add(new LoaiPhatSinh("Khoản thu", R.drawable.ic_themtien96));
        list.add(new LoaiPhatSinh("Khoản chi", R.drawable.ic_trutien96));
        list.add(new LoaiPhatSinh("Cho vay", R.drawable.ic_chovay96));
        list.add(new LoaiPhatSinh("Khoản nợ", R.drawable.ic_khoanno96));

        CustomSpinnerAdapter spinAdapter = new CustomSpinnerAdapter(
                getApplicationContext(), list);
        sp_category.setAdapter(spinAdapter);
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                switch (position){
                    case 0:
                        Bundle bundle = new Bundle();
                        bundle.putInt("ma_tk", matk);
                        RevenueFragment revenueExpenditureFragment = new RevenueFragment();
                        revenueExpenditureFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_cost, revenueExpenditureFragment).commit();
                        break;
                    case 1:
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("ma_tk", matk);
                        ExpenditureFragment expenditureFragment = new ExpenditureFragment();
                        expenditureFragment.setArguments(bundle1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_cost, expenditureFragment).commit();
                        break;
                    case 2:
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("ma_tk", matk);
                        LoanFragment loanFragment = new LoanFragment();
                        loanFragment.setArguments(bundle2);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_cost, loanFragment).commit();
                        break;
                    case 3:
                        Bundle bundle3 = new Bundle();
                        bundle3.putInt("ma_tk", matk);
                        BorrowFragment borrowFragment = new BorrowFragment();
                        borrowFragment.setArguments(bundle3);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_cost, borrowFragment).commit();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }
    private void setControl(){
        toolbar_cost = findViewById(R.id.toolbar_cost);
        sp_category = findViewById(R.id.sp_category);
        bt_exit = findViewById(R.id.bt_exit);
    }
}
