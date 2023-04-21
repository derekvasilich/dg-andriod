package com.example.dg_andriod.data.remote;

import com.example.dg_andriod.data.model.ResObj;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("login")
    Call<ResObj> login(@Body LoginRequest body);
}