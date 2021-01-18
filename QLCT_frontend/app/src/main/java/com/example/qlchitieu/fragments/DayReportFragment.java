package com.example.qlchitieu.fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qlchitieu.MainActivity;
import com.example.qlchitieu.R;
import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.models.BaoCaoNgay;
import com.example.qlchitieu.models.DanhSach;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayReportFragment extends Fragment {
    SharedPreferences sharedPreferences;
    TextView tv_day, tv_first, tv_last, tv_thu, tv_chi, tv_vay, tv_no, tv_thuno, tv_trano;
    LinearLayout lay_day, lay_header, lay_hide;
    ImageView im_hide;
    MainActivity activity;
    BarChart barChart;
    XAxis xAxis;
    public ArrayList<DanhSach> list = new ArrayList<>();
    final Calendar myCalendar = Calendar.getInstance();
    final Api api = ApiUtils.getAPIService();
    int matk;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_report, container, false);
        activity = (MainActivity) getActivity();
        tv_day = view.findViewById(R.id.tv_day);
        lay_day = view.findViewById(R.id.lay_day);
        tv_first = view.findViewById(R.id.tv_first);
        tv_last = view.findViewById(R.id.tv_last);
        tv_thu= view.findViewById(R.id.tv_thu);
        tv_chi = view.findViewById(R.id.tv_chi);
        tv_vay = view.findViewById(R.id.tv_chovay);
        tv_no = view.findViewById(R.id.tv_no);
        tv_thuno = view.findViewById(R.id.tv_thuno);
        tv_trano = view.findViewById(R.id.tv_trano);
        barChart = view.findViewById(R.id.chart);
        lay_header = view.findViewById(R.id.lay_header);
        lay_hide = view.findViewById(R.id.lay_hide);

        im_hide =view.findViewById(R.id.im_hide);

        lay_hide.setVisibility(View.GONE);


        barChart.setScaleEnabled(false);


        sharedPreferences = activity.getSharedPreferences("saveData", Context.MODE_PRIVATE);
        matk = sharedPreferences.getInt("ma_taikhoan", -1);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        tv_day.setText(dateFormat.format(new Date()));


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        lay_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(activity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        lay_header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (lay_hide.getVisibility()==View.GONE){
                    im_hide.setImageResource(R.drawable.ic_baseline_expand_less_24);
                    expand();
                }else{
                    im_hide.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    collapse();
                }
            }
        });
        return view;
    }

    private void todo(){
        String data = getDate(tv_day.getText().toString()) + "&" + matk;

        api.getDayReport(data).enqueue(new Callback<BaoCaoNgay>() {
            @Override
            public void onResponse(Call<BaoCaoNgay> call, Response<BaoCaoNgay> response) {
                if(response.code() == 200){
                    BaoCaoNgay baoCaoNgay = response.body();
                    tv_first.setText(String.format("%,.2f", baoCaoNgay.getSodudau()));
                    tv_last.setText(String.format("%,.2f", baoCaoNgay.getSoducuoi()));
                    tv_thu.setText(String.format("%,.2f", baoCaoNgay.getThu()));
                    tv_chi.setText(String.format("%,.2f", baoCaoNgay.getChi()));
                    tv_vay.setText(String.format("%,.2f", baoCaoNgay.getVay()));
                    tv_no.setText(String.format("%,.2f", baoCaoNgay.getNo()));
                    tv_thuno.setText(String.format("%,.2f", baoCaoNgay.getLay()));
                    tv_trano.setText(String.format("%,.2f", baoCaoNgay.getTra()));

                    ArrayList<BarEntry> barEntries = new ArrayList<>();
                    ArrayList<BarEntry> barEntries1 = new ArrayList<>();

                    barChart.fitScreen();
                    barEntries.clear();
                    barEntries1.clear();
                    barChart.invalidate();
                    barChart.clear();
                    barChart.fitScreen();
                    if(barChart.getData() != null)
                        barChart.getData().clearValues();
                    barChart.notifyDataSetChanged();
                    barChart.clear();
                    barChart.invalidate();
                    double a = baoCaoNgay.getThu();
                    double b = baoCaoNgay.getChi();
                    barEntries.add(new BarEntry(1, (float) a));

                    barEntries1.add(new BarEntry(1,(float) b));

                    BarDataSet barDataSet = new BarDataSet(barEntries,"Khoản thu");
                    barDataSet.setColor(Color.parseColor("#42f46e"));
                    BarDataSet barDataSet1 = new BarDataSet(barEntries1,"Khoản chi");
                    barDataSet1.setColors(Color.parseColor("#F44336"));

                    String[] months = new String[] {""};
                    BarData data = new BarData(barDataSet,barDataSet1);
                    barChart.setData(data);

                    xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
                    barChart.getAxisLeft().setAxisMinimum(0);
                    barChart.getAxisLeft().setTextSize(13f);
                    barChart.getAxisRight().setEnabled(false);
                    barChart.getDescription().setEnabled(false);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1);
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setGranularityEnabled(true);

                    float barSpace = 0.01f;
                    float groupSpace = 0.3f;

                    data.setBarWidth(0.15f);
                    barChart.getXAxis().setAxisMinimum(0);
                    barChart.getXAxis().setAxisMaximum(barChart.getBarData().getGroupWidth(groupSpace, barSpace));
                    barChart.groupBars(0, groupSpace, barSpace);

                }

                if (response.code() == 500){
                    CustomToast.makeText(activity,"Lỗi hiển thị báo cáo",
                            CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                }
            }

            @Override
            public void onFailure(Call<BaoCaoNgay> call, Throwable t) {
           //     Toast.makeText(activity, "Có lỗi không xác định! Vui lòng thử lại sau", Toast.LENGTH_LONG).show();
            }
        });



    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tv_day.setText(sdf.format(myCalendar.getTime()));
        todo();
    }
    private String getDate(String datein){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObject;
        String date;

        try{

            dateObject = formatter.parse(datein);
            date = new SimpleDateFormat("yyyy-MM-dd").format(dateObject);

            return date;
        }
        catch (java.text.ParseException e)
        {
            e.printStackTrace();
            Log.i("E11111111111", e.toString());
            return "";
        }

    }
    private void expand() {
        //set Visible
        lay_hide.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        lay_hide.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, lay_hide.getMeasuredHeight());
        mAnimator.start();
    }

    private void collapse() {
        int finalHeight = lay_hide.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                lay_hide.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = lay_hide.getLayoutParams();
                layoutParams.height = value;
                lay_hide.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
    @Override
    public void onResume() {
        super.onResume();
        todo();
    }
}
