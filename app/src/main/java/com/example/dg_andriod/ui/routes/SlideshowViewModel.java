package com.example.dg_andriod.ui.routes;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dg_andriod.data.model.PagedList;
import com.example.dg_andriod.data.model.Route;
import com.example.dg_andriod.data.remote.ApiUtils;
import com.example.dg_andriod.data.remote.RouteService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlideshowViewModel extends ViewModel implements Callback {

    private Context context;
    private MutableLiveData<List<Route>> mRoutes;

    public SlideshowViewModel() {
        mRoutes = new MutableLiveData<>();
    }

    public void doGetRoutes(Context context) {
        this.context = context;
        RouteService routeService = ApiUtils.getRouteService(context);
        Call call = routeService.findAllPaginated(1, 10);
        call.enqueue(this);
    }

    public LiveData<List<Route>> getRoutes() {
        return mRoutes;
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response.isSuccessful()) {
            List<Route> routesList = ((PagedList)response.body()).getContent();
            mRoutes.setValue(routesList);
        } else {
            Toast.makeText(context, "Error loading routes! Please try again!", Toast.LENGTH_SHORT).show();
        }
//                loadingProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
//                loadingProgressBar.setVisibility(View.INVISIBLE);
    }
}