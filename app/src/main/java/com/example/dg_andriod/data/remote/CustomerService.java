package com.example.dg_andriod.data.remote;

import com.example.dg_andriod.data.model.Customer;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CustomerService {
    @GET("customers")
    Call<Customer[]> findAll();

}
