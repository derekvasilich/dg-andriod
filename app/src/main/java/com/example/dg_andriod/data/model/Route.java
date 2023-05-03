package com.example.dg_andriod.data.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Route {
    public Long id;
    public Company company;
    public Date date;
    public String truck;
    public Integer startAddress;
    public String quoteIds;
    public List<Quote> quotes;
    public Integer duration;
    public Integer distance;
    public Boolean emailed;

    public static float GEOFENCE_RADIUS_IN_METRES = 250f;
    public static long GEOFENCE_EXIPRATION_DURATION_MS = 43200000; // 12h
    public static int GEOFENCE_LOITERING_DELAY_MS = 300; // 5m

    public static class StartPoint {
        public static double lat;
        public static double lng;
        public static String name;
        public static String address;

        public StartPoint(String name, String address, double lat, double lng) {
            this.name = name;
            this.address = address;
            this.lat = lat;
            this.lng = lng;
        }

        public static StartPoint create(String name, String address, double lat, double lng) {
            return new StartPoint(name, address, lat, lng);
        }
    }

    public static List<StartPoint> startPointList = Arrays.asList(
        StartPoint.create("ABA - Allison Brothers Asphalt", "30 Adelaide St N, London, ON",  42.9770286,  -81.22500689999998),
        StartPoint.create("Cambridge Hotel and Conference Centre", "700 Hespeler Rd, Cambridge, ON", 43.4095403,  -80.3292753 ),
        StartPoint.create("Residence & Conference Centre - Barrie", "101 Georgian Dr, Barrie, ON", 44.4133088, -79.66556149999997),
        StartPoint.create("London Asphalt Plant", "1788 Clark Rd, London, ON", 43.0500484, -81.1978533),
        StartPoint.create("Woodstock Asphalt Plant", "594728 Oxford 59, Woodstock, ON", 43.101577, -80.7303398)
    );
}
