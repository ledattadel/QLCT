package com.example.qlchitieu;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cottacush.android.currencyedittext.CurrencyEditText;
import com.example.qlchitieu.api.Api;
import com.example.qlchitieu.api.ApiUtils;
import com.example.qlchitieu.customs.CustomToast;
import com.example.qlchitieu.models.TaiKhoan1;
import com.example.qlchitieu.models.VayNo;
import com.example.qlchitieu.models.VayNo1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteEditLoanBorrow extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    EditText  et_category_lb, et_date_lb, et_des_lb, et_nameborrow_lb,
            et_addressborrow_lb, et_phoneborrow_lb, et_repay_date_lb, et_paid;
    Button button_save_lb;
    CurrencyEditText et_money_lb;
    ImageView bt_exit_de, bt_edit_de, bt_delete_de;
    LinearLayout lay_de;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendar1 = Calendar.getInstance();
    Calendar myCalendarf = Calendar.getInstance();
    Api api = ApiUtils.getAPIService();
    int maps;
    VayNo1 vayNo;
    private Double sodu;
    private ProgressDialog dialog;
    int matk;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_edit_loan_borrow);
        setControl();
        setEvent();
    }

    private void setEvent(){
        sharedPreferences = getSharedPreferences("saveData", MODE_PRIVATE);
        matk = sharedPreferences.getInt("ma_taikhoan", -1);
        bt_exit_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        et_des_lb.setHint(" ");
        et_repay_date_lb.setHint(" ");
        et_phoneborrow_lb.setHint(" ");
        et_addressborrow_lb.setHint(" ");
        final Intent intent = getIntent();
        maps = intent.getIntExtra("ma_phatsinh",-1);
        todo();

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
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };
        et_date_lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(lay_de.getVisibility() == View.VISIBLE) {
//                    new DatePickerDialog(DeleteEditLoanBorrow.this, date, myCalendar
//                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                }
//                else
                    return;
            }
        });
        et_repay_date_lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lay_de.getVisibility() == View.VISIBLE) {

                 DatePickerDialog datePickerDialog = new DatePickerDialog(DeleteEditLoanBorrow.this, date1, myCalendar1
                            .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                            myCalendar1.get(Calendar.DAY_OF_MONTH));
                 datePickerDialog.getDatePicker().setMinDate(myCalendarf.compareTo(myCalendar) > 0? myCalendarf.getTimeInMillis(): myCalendar.getTimeInMillis());
                 datePickerDialog.show();
                }
            }
        });
        et_money_lb.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus){
                et_money_lb.setSelectAllOnFocus(true);
            }
        });
        et_money_lb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_money_lb.getText().length() == 0){
                    et_money_lb.append("0");
                }
            }
        });

        et_paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vayNo.getDatra() != null){
                    Intent intent = new Intent(DeleteEditLoanBorrow.this, DeletePayActivity.class);
                    intent.putExtra("ma_phatsinh", vayNo.getDatra());
                    startActivity(intent);
                }
                else
                {
                    new AlertDialog.Builder(DeleteEditLoanBorrow.this)
                            .setTitle("Thông báo!")
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage(vayNo.getLoaiphatsinh().compareTo("v")==0 ? "Khoản cho vay chưa thu nợ\nVào sổ nợ để thêm khoản thu nợ!":"Khoản nợ chưa trả nợ\nVào sổ nợ để thêm khoản trả nợ!")

                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).show();
                }
            }
        });
        bt_edit_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if( vayNo.getNgay().compareTo(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) != 0 && vayNo.getDatra() == null)
               {
                   new AlertDialog.Builder(DeleteEditLoanBorrow.this)
                           .setTitle("Thông báo!")
                           .setIcon(R.mipmap.ic_launcher)
                           .setMessage(vayNo.getLoaiphatsinh().compareTo("v") == 0? "Chỉ được phép hiệu chỉnh ngày thu nợ":"Chỉ được phép hiệu chỉnh ngày trả nợ")

                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   lay_de.setVisibility(View.VISIBLE);
                               }
                           }).show();

               }
               else
                if(vayNo.getDatra() == null && vayNo.getNgay().compareTo(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) == 0) {
                    et_money_lb.setFocusableInTouchMode(true);
                    //    et_category_lb.setFocusableInTouchMode(true);
                    //    et_date_de.setFocusableInTouchMode(true);
                    et_des_lb.setFocusableInTouchMode(true);
                    et_nameborrow_lb.setFocusableInTouchMode(true);
                    et_phoneborrow_lb.setFocusableInTouchMode(true);
                    et_addressborrow_lb.setFocusableInTouchMode(true);
                    et_des_lb.setHint("Mô tả");
                    if (vayNo.getLoaiphatsinh().compareTo("v") == 0) {
                        et_repay_date_lb.setHint("Ngày thu nợ");
                        et_phoneborrow_lb.setHint("Số điện thoại người vay");
                        et_addressborrow_lb.setHint("Địa chỉ người vay");
                    } else {
                        et_repay_date_lb.setHint("Ngày trả nợ");
                        et_phoneborrow_lb.setHint("Số điện thoại chủ nợ");
                        et_addressborrow_lb.setHint("Địa chỉ chủ nợ");
                    }
                    lay_de.setVisibility(View.VISIBLE);
                }
                else{
                    new AlertDialog.Builder(DeleteEditLoanBorrow.this)
                            .setTitle("Thông báo!")
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage(vayNo.getLoaiphatsinh() == "v"? "Khoản vay đã thu nợ không thể hiệu chỉnh!":"Khoản nợ đã trả không thể hiệu chỉnh!")

                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   return;
                                }
                            }).show();
                }
            }
        });
        bt_delete_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vayNo.getSotien() - et_money_lb.getNumericValue()  > sodu && vayNo.getLoaiphatsinh().compareTo("n") == 0 &&
                vayNo.getNgay().compareTo(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) == 0)
                {
                    if(et_money_lb.getNumericValue() > sodu){
                        new AlertDialog.Builder(DeleteEditLoanBorrow.this)
                                .setTitle("Thông báo!")
                                .setIcon(R.mipmap.ic_launcher)
                                .setMessage("Vi phạm ràng buộc số dư âm")

                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                    return;
                }
                if(vayNo.getNgay().compareTo(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) == 0) {

                    if (vayNo.getDatra() == null) {
                        new AlertDialog.Builder(DeleteEditLoanBorrow.this)
                                .setTitle("Thông báo!")
                                .setIcon(R.mipmap.ic_launcher)
                                .setMessage("Bạn có muốn xóa phát sinh giao dịch này không?")

                                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        api.deleteOne(maps).enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.code() == 200) {
                                                    CustomToast.makeText(DeleteEditLoanBorrow.this, "Xóa thành công", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();
                                                    onBackPressed();
                                                }
                                                if (response.code() == 500) {
                                                    CustomToast.makeText(DeleteEditLoanBorrow.this, "Xóa thất bại", CustomToast.LENGTH_LONG, CustomToast.ERROR, true).show();
                                                    return;
                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                CustomToast.makeText(DeleteEditLoanBorrow.this, "Có lỗi không xác định! Vui lòng thử lại sau", CustomToast.LENGTH_LONG, CustomToast.CONFUSING, true).show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                    } else {
                        new AlertDialog.Builder(DeleteEditLoanBorrow.this)
                                .setTitle("Thông báo!")
                                .setIcon(R.mipmap.ic_launcher)
                                .setMessage(vayNo.getLoaiphatsinh() == "v" ? "Khoản vay đã thu nợ không thể xóa!" : "Khoản nợ đã trả không thể xóa!")

                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        return;
                                    }
                                }).show();
                    }
                }
                else{
                    new AlertDialog.Builder(DeleteEditLoanBorrow.this)
                            .setTitle("Thông báo!")
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage("Chỉ có thể xóa các khoản phát sinh trong ngày hôm nay")

                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    return;
                                }
                            }).show();
                }
            }
        });

        button_save_lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_money_lb.getNumericValue() <= 0){
                    et_money_lb.setError("Số tiền phải lớn hơn 0đ");
                    et_money_lb.requestFocus();
                    return;
                }
                if((et_money_lb.getNumericValue() - vayNo.getSotien()) > sodu && vayNo.getLoaiphatsinh().compareTo("v") == 0)
                {
                    et_money_lb.setError("Vượt quá số dư hiện tại");
                    et_money_lb.requestFocus();
                    return;
                }
                if((vayNo.getSotien() - et_money_lb.getNumericValue() ) > sodu && vayNo.getLoaiphatsinh().compareTo("n") == 0){
                    et_money_lb.setError("Vi phạm ràng buộc số dư âm");
                    et_money_lb.requestFocus();
                    return;
                }
                if(et_money_lb.getNumericValue() > 9999999999.99){
                    et_money_lb.setError("Vượt quá hạn mức giao dịch 9,999,999,999.99đ");
                    et_money_lb.requestFocus();
                    return;
                }
                if( et_nameborrow_lb.getText().toString().isEmpty()){
                    et_nameborrow_lb.setError("Tên người vay không được để trống");
                    et_nameborrow_lb.requestFocus();
                    return;
                }
                if(!et_phoneborrow_lb.getText().toString().isEmpty() && et_phoneborrow_lb.getText().toString().length() < 10)
                {
                    et_phoneborrow_lb.setError("Số điện thoại phải đủ 10 số");
                    et_phoneborrow_lb.requestFocus();
                    return;
                }
                if(!getDate1(et_repay_date_lb.getText().toString()).isEmpty() && getDate1(et_repay_date_lb.getText().toString()).compareTo(getDate1(et_date_lb.getText().toString())) < 0){
                    et_repay_date_lb.setError("_");
                    CustomToast.makeText(DeleteEditLoanBorrow.this,"Ngày thu không trước ngày " + et_date_lb.getText(), CustomToast.LENGTH_LONG,CustomToast.WARNING,true).show();
                    return;
                }
                VayNo vayNo1 = new VayNo(getDate1(et_date_lb.getText().toString()), et_category_lb.getText().toString(),
                        vayNo.getLoaiphatsinh(),et_money_lb.getNumericValue(),
                        et_des_lb.getText().toString(), vayNo.getMa_taikhoan(), getDate1(et_repay_date_lb.getText().toString())
                        , et_nameborrow_lb.getText().toString(), et_addressborrow_lb.getText().toString(), et_phoneborrow_lb.getText().toString());

                showDialog();
                api.editOneLB(maps, vayNo1).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        hideDialog();
                        if(response.code() == 200) {
                            CustomToast.makeText(DeleteEditLoanBorrow.this, "Cập nhật thành công", CustomToast.LENGTH_LONG, CustomToast.SUCCESS, true).show();
                            onBackPressed();
                        }
                        if(response.code() == 500){
                            CustomToast.makeText(DeleteEditLoanBorrow.this,"Cập nhật thất bại", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                            return;
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideDialog();
                        CustomToast.makeText(DeleteEditLoanBorrow.this, "Có lỗi không xác định! Vui lòng thử lại sau", CustomToast.LENGTH_LONG, CustomToast.CONFUSING, true).show();
                    }
                });
            }
        });
    }

    private String getDate(String datein){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObject;
        String date;

        try{

            dateObject = formatter.parse(datein);
            date = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);

            return date;
        }
        catch (java.text.ParseException e)
        {
            e.printStackTrace();
            Log.i("E11111111111", e.toString());
            return "";
        }

    }
    private String getDate1(String datein){
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
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_date_lb.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel1() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String temp = sdf.format(myCalendar1.getTime());
        if(getDate(temp).compareTo(getDate(et_date_lb.getText().toString())) < 0){
            et_repay_date_lb.setError("_");
            if(vayNo.getLoaiphatsinh() == "v")
                CustomToast.makeText(DeleteEditLoanBorrow.this,"Ngày thu nợ không trước ngày "
                        + et_date_lb.getText().toString(), CustomToast.LENGTH_LONG,CustomToast.WARNING,true).show();
            else
                CustomToast.makeText(DeleteEditLoanBorrow.this,"Ngày trả nợ không trước ngày "
                        + et_date_lb.getText().toString(), CustomToast.LENGTH_LONG,CustomToast.WARNING,true).show();
            et_repay_date_lb.setText(temp);
        }
        else {
            et_repay_date_lb.setError(null);
            et_repay_date_lb.setText(temp);

        }
    }
    private void setControl(){
        toolbar = findViewById(R.id.toolbar_de);
        et_money_lb = findViewById(R.id.et_money_lb);
        et_category_lb = findViewById(R.id.et_category_lb);
        et_date_lb = findViewById(R.id.et_date_lb);
        et_des_lb = findViewById(R.id.et_des_lb);
        bt_exit_de = findViewById(R.id.bt_exit_de);
        button_save_lb =findViewById(R.id.button_save_lb);
        lay_de = findViewById(R.id.lay_de);
        bt_edit_de = findViewById(R.id.bt_edit_de);
        bt_delete_de = findViewById(R.id.bt_delete_de);
        et_nameborrow_lb = findViewById(R.id.et_nameborrow_lb);
        et_addressborrow_lb = findViewById(R.id.et_addressborrow_lb);
        et_phoneborrow_lb = findViewById(R.id.et_phoneborrow_lb);
        et_repay_date_lb = findViewById(R.id.et_repay_date_lb);
        et_paid = findViewById(R.id.et_paid);
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
    private void todo(){
        showDialog();
        api.getOneLB(maps).enqueue(new Callback<VayNo1>() {
            @Override
            public void onResponse(Call<VayNo1> call, Response<VayNo1> response) {
                hideDialog();
                if(response.code() == 200){
                    vayNo = response.body();
                    String a,b = "";
                    if(vayNo.getLoaiphatsinh().compareTo("n") == 0 ){
                        et_money_lb.setTextColor(Color.parseColor("#43A047"));
                        a = "Đã trả nợ";
                        b = "Chưa trả nợ";
                    }
                    else
                    {
                        et_money_lb.setTextColor(Color.parseColor("#F44336"));
                        a = "Đã thu nợ";
                        b = "Chưa thu nợ";
                    }

                    et_money_lb.setText(String.format(java.util.Locale.US,"%,.2f", vayNo.getSotien()));
                    et_category_lb.setText(vayNo.getNoidung());
                    et_date_lb.setText(getDate(vayNo.getNgay()));
                    et_des_lb.setText(vayNo.getMota());
                    et_nameborrow_lb.setText(vayNo.getHoten_vayno());
                    if(vayNo.getNgaytra() != null)
                        et_repay_date_lb.setText(getDate(vayNo.getNgaytra()));
                    et_phoneborrow_lb.setText(vayNo.getSdt_vayno());
                    et_addressborrow_lb.setText(vayNo.getDiachi_vayno());
                    if(vayNo.getDatra() != null)
                    {
                        et_paid.setTextColor(Color.parseColor("#43A047"));
                        et_paid.setText(a);
                    }
                    else{
                        et_paid.setTextColor(Color.parseColor("#F44336"));
                        et_paid.setText(b);
                    }
                    try {
                        myCalendar1.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(vayNo.getNgaytra()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(response.code() == 500 || response.code() == 404){
                    CustomToast.makeText(DeleteEditLoanBorrow.this,"Khoản phát sinh không xác định", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<VayNo1> call, Throwable t) {
                hideDialog();
                Toast.makeText(DeleteEditLoanBorrow.this, "Có lỗi không xác định! Vui lòng thử lại sau", Toast.LENGTH_LONG).show();
            }
        });
        api.getBalace1(matk).enqueue(new Callback<TaiKhoan1>() {
            @Override
            public void onResponse(Call<TaiKhoan1> call, Response<TaiKhoan1> response) {
                if(response.code() == 200)
                {
                    TaiKhoan1 taiKhoan1 = response.body();
                    sodu = taiKhoan1.getSodu();
                }
                if (response.code() == 500){
                    CustomToast.makeText(DeleteEditLoanBorrow.this,"Lấy số dư không thành công", CustomToast.LENGTH_LONG,CustomToast.ERROR,true).show();
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan1> call, Throwable t) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        todo();
    }
}
