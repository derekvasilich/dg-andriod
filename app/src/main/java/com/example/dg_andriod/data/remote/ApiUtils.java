package com.example.dg_andriod.data.remote;

public class ApiUtils {

    public static final String BASE_URL = "http://10.0.2.2:8080/api/";

    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }

    public static CustomerService getCustomerService() {
        return RetrofitClient.getClient(BASE_URL).create(CustomerService.class);
    }
}