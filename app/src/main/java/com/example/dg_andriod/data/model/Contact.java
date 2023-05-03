package com.example.dg_andriod.data.model;

public class Contact {
    public Long id;
    public String phone;
    public String mobile;
    public String address_1;
    public String city;
    public String province;
    public String postal;
    public String country;
    public Float lat;
    public Float lng;

    public String getAddress() {
        return address_1 + ", " + city + ", " + province + ", " + postal;
    }
}
