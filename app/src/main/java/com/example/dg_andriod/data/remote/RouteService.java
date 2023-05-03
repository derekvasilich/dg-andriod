package com.example.dg_andriod.data.remote;

import com.example.dg_andriod.data.model.PagedList;
import com.example.dg_andriod.data.model.Route;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RouteService {
    @GET("routes")
    Call<Route[]> findAll();

    @GET("routes/paginated")
    Call<PagedList<Route>> findAllPaginated(@Query("page") Integer page, @Query("size") Integer size);

    @GET("routes/{id}")
    Call<Route> findById(@Path("id") Long id);
}
