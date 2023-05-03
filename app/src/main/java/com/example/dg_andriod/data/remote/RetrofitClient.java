package com.example.dg_andriod.data.remote;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.dg_andriod.data.model.User.*;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String url, Context context) {
        if(retrofit == null){
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request();
                    if (newRequest.header("No-Authentication") == null) {
                        String token = context.getSharedPreferences(JWT_SHARED_PREF_KEY, Context.MODE_PRIVATE).getString(JWT_TOKEN_PREF_KEY, null);
                        newRequest = newRequest.newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();
                    }
                    return chain.proceed(newRequest);
                }
            }).build();
            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}