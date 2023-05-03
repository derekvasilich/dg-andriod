package com.example.dg_andriod.data.remote;

import android.content.Context;

public class ApiUtils {

    public static UserService getUserService(Context context) {
        return RetrofitClient.getClient(context).create(UserService.class);
    }

    public static CustomerService getCustomerService(Context context) {
        return RetrofitClient.getClient(context).create(CustomerService.class);
    }

    public static RouteService getRouteService(Context context) {
        return RetrofitClient.getClient(context).create(RouteService.class);
    }

}