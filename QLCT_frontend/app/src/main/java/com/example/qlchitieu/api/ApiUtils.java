package com.example.qlchitieu.api;

public class ApiUtils {
    public static final String BASE_URL = "http://192.168.1.114" +
            "" +
            ":3000/";
        public static Api getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(Api.class);
    }
}
