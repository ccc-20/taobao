package com.example.baekhyun.utils;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static final RetrofitManager outInstance=new RetrofitManager();
    private final Retrofit mRetrofit;
    public static RetrofitManager getInstance(){
        return outInstance;
    }
    private RetrofitManager(){
        mRetrofit=new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public Retrofit getRetrofit(){
        return mRetrofit;
    }
}
