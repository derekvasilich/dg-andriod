package com.example.dg_andriod.ui.routes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dg_andriod.R;
import com.example.dg_andriod.data.model.Route;

public class SlideshowFragment extends Fragment implements AdapterView.OnItemClickListener {

    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        final ListView listView = root.findViewById(R.id.routes_list);
        listView.setOnItemClickListener(this);

        slideshowViewModel.getRoutes().observe(getViewLifecycleOwner(), routeFieldList -> {
            listView.setAdapter(new RouteFieldAdapter(getContext(), routeFieldList));
        });

        slideshowViewModel.doGetRoutes(getContext());

        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Route route = slideshowViewModel.getRoutes().getValue().get(i);
        // TODO Navigate to the map and display the route points along with start location
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        Bundle bundle = new Bundle();
        bundle.putLong("routeId", route.id);
        navController.navigate(R.id.nav_gallery, bundle);
    }
}