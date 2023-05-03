package com.example.dg_andriod.data.remote;

import android.content.Context;

public class ApiUtils {

    public static final String BASE_URL = "http://10.0.2.2:8080/api/";

    public static UserService getUserService(Context context) {
        return RetrofitClient.getClient(BASE_URL, context).create(UserService.class);
    }

    public static CustomerService getCustomerService(Context context) {
        return RetrofitClient.getClient(BASE_URL, context).create(CustomerService.class);
    }

    public static RouteService getRouteService(Context context) {
        return RetrofitClient.getClient(BASE_URL, context).create(RouteService.class);
    }

}