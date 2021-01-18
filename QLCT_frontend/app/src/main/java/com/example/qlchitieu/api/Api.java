package com.example.qlchitieu.api;

import com.example.qlchitieu.models.BaoCaoNgay;
import com.example.qlchitieu.models.Chovay;
import com.example.qlchitieu.models.DangKyInfo;
import com.example.qlchitieu.models.DangNhap;
import com.example.qlchitieu.models.DanhSach;
import com.example.qlchitieu.models.KhoanNo;
import com.example.qlchitieu.models.MatKhau;
import com.example.qlchitieu.models.NguoiDung;
import com.example.qlchitieu.models.NguoiDungUpdate;
import com.example.qlchitieu.models.PhatSinh;
import com.example.qlchitieu.models.PhatSinh1;
import com.example.qlchitieu.models.TaiKhoan;
import com.example.qlchitieu.models.TaiKhoan1;
import com.example.qlchitieu.models.ThongBao;
import com.example.qlchitieu.models.ThongTin_NguoiDung;
import com.example.qlchitieu.models.ThuTra;
import com.example.qlchitieu.models.TienVaoRa;
import com.example.qlchitieu.models.VayNo;
import com.example.qlchitieu.models.VayNo1;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {


    @POST("nguoidung/dangnhap")
    Call<DangNhap> userLogin(@Body NguoiDung nguoiDung);

    @POST("nguoidung/dangky")
    Call<ResponseBody> userSignup(@Body DangKyInfo dangky);

    @GET("nguoidung/thongtin/{manguoidung}")
    Call<ThongTin_NguoiDung> getOneUser(@Path("manguoidung") int id);

    @PATCH("nguoidung/thongtin_nguoidung/{manguoidung}")
    Call<ResponseBody> updateUser(@Path("manguoidung") int id, @Body ArrayList<NguoiDungUpdate> nguoidungUpdates);

    @PATCH("nguoidung/{manguoidung}")
    Call<ResponseBody> updatePass(@Path("manguoidung") int id, @Body MatKhau mk);

    @POST("phatsinh/thuchi/")
    Call<ResponseBody> revenueExpenditureAdd(@Body PhatSinh phatSinh);

    @POST("phatsinh/vayno/")
    Call<ResponseBody> LoanBorrowadd(@Body VayNo vayNo);

    @GET("taikhoan/sodu/{id}")
    Call<TaiKhoan> getBalance(@Path("id") int id);

    @GET("phatsinh/tienvaora/{khoang}")
    Call<TienVaoRa> getInOut(@Path("khoang") String khoang);

    @GET("phatsinh/khoangthoigian/{khoang}")
    Call<ArrayList<DanhSach>> getInterval(@Path("khoang") String khoang);

    @GET("phatsinh/vaorathang/{ngay}")
    Call<TienVaoRa> getInOutMonth(@Path("ngay") String ngay);

    @GET("phatsinh/theothang/{ngay}")
    Call<ArrayList<DanhSach>> getMonth(@Path("ngay") String ngay);

    @GET("phatsinh/vaorangay/{ngay}")
    Call<TienVaoRa> getInOutDay(@Path("ngay") String ngay);

    @GET("phatsinh/theongay/{ngay}")
    Call<ArrayList<DanhSach>> getDay(@Path("ngay") String ngay);

    @GET("phatsinh/vaoranam/{ngay}")
    Call<TienVaoRa> getInOutYear(@Path("ngay") String ngay);

    @GET("phatsinh/theonam/{ngay}")
    Call<ArrayList<DanhSach>> getYear(@Path("ngay") String ngay);

    @GET("phatsinh/vaoratatca/{id}")
    Call<TienVaoRa> getInOutAll(@Path("id") int id);

    @GET("phatsinh/tatca/{id}")
    Call<ArrayList<DanhSach>> getAll(@Path("id") int id);

    @GET("phatsinh/hienthi/{id}")
    Call<PhatSinh1> getOneRE(@Path("id") int id);

    @DELETE("phatsinh/xoa/{id}")
    Call<ResponseBody> deleteOne(@Path("id") int id);

    @PATCH("phatsinh/suathuchi/{id}")
    Call<ResponseBody> editOneRE(@Path("id") int id, @Body PhatSinh phatSinh);

    @GET("phatsinh/hienthi/{id}")
    Call<VayNo1> getOneLB(@Path("id") int id);

    @PATCH("phatsinh/suavayno/{id}")
    Call<ResponseBody> editOneLB(@Path("id") int id, @Body VayNo vayNo);

    @GET("phatsinh/baocaongay/{data}")
    Call<BaoCaoNgay> getDayReport(@Path("data") String data);

    @GET("phatsinh/baocaothang/{data}")
    Call<BaoCaoNgay> getMonthReport(@Path("data") String data);

    @GET("phatsinh/baocaonam/{data}")
    Call<BaoCaoNgay> getYearReport(@Path("data") String data);

    @GET("phatsinh/khoanno/{id}")
    Call<KhoanNo> getBorrow(@Path("id") int id);

    @GET("phatsinh/chovay/{id}")
    Call<Chovay> getLoan(@Path("id") int id);

    @POST("phatsinh/thutra/")
    Call<ResponseBody> payAdd(@Body ThuTra thuTra);

    @GET("taikhoan/sodulay/{id}")
    Call<TaiKhoan1> getBalace1(@Path("id") int id);

    @GET("phatsinh/thongbao/{ngay}")
    Call<ThongBao> getNotification(@Path("ngay") String ngay);

    @GET("phatsinh/kiemtra/{id}")
    Call<ResponseBody> getCheck(@Path("id") int id);

}
