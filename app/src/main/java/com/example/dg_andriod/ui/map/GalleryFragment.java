package com.example.dg_andriod.ui.map;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dg_andriod.R;
import com.example.dg_andriod.data.model.Contact;
import com.example.dg_andriod.data.model.Customer;
import com.example.dg_andriod.data.model.Quote;
import com.example.dg_andriod.data.model.Route;
import com.example.dg_andriod.data.remote.ApiUtils;
import com.example.dg_andriod.data.remote.CustomerService;
import com.example.dg_andriod.data.remote.RouteService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private GalleryViewModel galleryViewModel;
    private GoogleMap mMap;
    private CustomerService customerService;
    private RouteService routeService;
    private List<Quote> quoteList = new ArrayList<>();

    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.ACCESS_FINE_LOCATION"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        customerService = ApiUtils.getCustomerService(getContext());
        routeService = ApiUtils.getRouteService(getContext());
        if (allPermissionsGranted()) {
            startMap(); //start camera if permission has been granted by user
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
        return root;
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    private void startMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        List<Quote> intersects = intersectingQuotes(location);
        intersects.forEach(quote -> {
            Toast.makeText(getContext(), "Current location intersects quote: " + quote.id, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if (allPermissionsGranted()) {
                startMap();
            } else {
                Toast.makeText(getContext(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public List<Quote> intersectingQuotes(Location center) {
        return quoteList.stream().filter(quote -> {
            float[] results = new float[1];
            Location.distanceBetween(center.getLatitude(), center.getLongitude(), quote.contact.lat, quote.contact.lng, results);
            return (results[0] <= Route.GEOFENCE_RADIUS_IN_METRES);
        }).collect(Collectors.toList());
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        LocationRequest locationRequest = new LocationRequest.Builder(Route.GEOFENCE_LOITERING_DELAY_MS).build();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        LocationServices.getFusedLocationProviderClient(getContext())
                .requestLocationUpdates(locationRequest, this, Looper.myLooper());

        // Add a marker in Sydney and move the camera
        Bundle bundle = getArguments();
        Long routeId = bundle != null ? bundle.getLong("routeId") : null;
        Call call;

        if (routeId != null) {
            quoteList.clear();
            call = routeService.findById(routeId);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        // TODO update to use actual route start position
                        Route route = (Route) response.body();

                        Route.StartPoint startPoint = Route.startPointList.get(route.startAddress);
                        LatLng startLatLng = new LatLng(startPoint.lat, startPoint.lng);
                        mMap.addMarker(new MarkerOptions().position(startLatLng).title(startPoint.address));

                        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                        bounds.include(startLatLng);

                        if (route.quotes.size() > 0) {
                            route.quotes.forEach(quote -> {
                                Contact contact = quote.contact;
                                Customer customer = quote.customer;
                                if (contact.lat != null && contact.lng != null) {
                                    LatLng point = new LatLng(contact.lat, contact.lng);
                                    mMap.addMarker(new MarkerOptions()
                                            .position(point)
                                            .title(customer.getName() + " (" + contact.getAddress() + ")"));
                                    bounds.include(point);
                                    quoteList.add(quote);
                                }
                            });
                        }
                        int padding = 40;
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds.build(), padding);
                        mMap.moveCamera(cu);
                    } else {
                        Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                    }
//                loadingProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                loadingProgressBar.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            call = customerService.findAll();
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        List<Customer> customerList = Arrays.asList((Customer[]) response.body());
                        if (customerList.size() > 0) {
                            customerList.forEach(customer -> {
                                if (customer.lat != null && customer.lng != null) {
                                    LatLng point = new LatLng(customer.lat, customer.lng);
                                    mMap.addMarker(new MarkerOptions()
                                            .position(point)
                                            .title(customer.firstName + ' ' + customer.lastName));
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                    }
//                loadingProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                loadingProgressBar.setVisibility(View.INVISIBLE);
                }

            });
        }
    }

}