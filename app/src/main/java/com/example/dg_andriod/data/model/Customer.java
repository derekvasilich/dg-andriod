package com.example.dg_andriod.data.model;

public class Customer {
    public Long id;

    public String legalName;
    public String firstName;
    public String lastName;
    public String email;

    public String phone;
    public String mobile;
    public String licenseNumber;
    public String address_1;
    public String city;
    public String province;
    public String postal;
    public String country;

    public Float lat;
    public Float lng;

    public String getName() {
        if (legalName != null && legalName != "") {
            return legalName;
        }
        return firstName + " " + lastName;
    }

}
