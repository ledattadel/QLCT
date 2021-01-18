package com.example.qlchitieu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.qlchitieu.MainActivity;
import com.example.qlchitieu.R;
import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;

public class ReportFragment extends Fragment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView menu_report;
    MainActivity activity;
    Api api = ApiUtils.getAPIService();
    int matk;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report,container, false);
        activity = (MainActivity) getActivity();
        sharedPreferences = activity.getSharedPreferences("saveData", Context.MODE_PRIVATE);
        matk = sharedPreferences.getInt("ma_taikhoan", -1);
        menu_report = view.findViewById(R.id.menu_report);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_report, new DayReportFragment())
                .commit();
        menu_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(activity, menu_report);
                popup.getMenuInflater()
                        .inflate(R.menu.report_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.report_day:
                                getChildFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_report, new DayReportFragment())
                                        .commit();
                                return true;
                            case R.id.report_month:
                                getChildFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_report, new MonthReportFragment())
                                        .commit();
                                return true;
                            case R.id.report_year:
                                getChildFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_report, new YearReportFragment())
                                        .commit();
                                return true;
                        }
                        return false;
                    }
                });

                popup.show();
            }
        });
        return view;
    }
}
