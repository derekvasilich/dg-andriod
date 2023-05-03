package com.example.dg_andriod.data.model;

public class User {
    public static final String JWT_SHARED_PREF_KEY = "com.example.dg_andriod";
    public static final String JWT_TOKEN_PREF_KEY = "jwt:token";
    public static final String JWT_TYPE_PREF_KEY = "jwt:type";
    public static final String REMEMBER_PREF_KEY = "remember:me";

    public String password;
    public String[] roles;
    public String token;
    public String type;
    public String username;
}