package com.example.livevideostreaming.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RetrofitClient {

     public static final String BASE_URL = "https://gist.githubusercontent.com/JagratiVerma1408/00f05455a2177daa4b61fb2f95a91d99/raw/620cff1d571d421378b6c543b019c6fd5b50694c/netflix_project.json/";

     public static ApiInterface getRetrofitClient()
     {
         Retrofit.Builder builder = new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                 .addConverterFactory(GsonConverterFactory.create())
                 .baseUrl(BASE_URL);

         return builder.build().create(ApiInterface.class);

     }


}
