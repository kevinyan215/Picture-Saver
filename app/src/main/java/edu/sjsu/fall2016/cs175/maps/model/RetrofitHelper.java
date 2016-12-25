package edu.sjsu.fall2016.cs175.maps.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kevin on 10/8/16.
 */

public class RetrofitHelper {

    public static final String BASE_URL = "https://api.unsplash.com/";
    private static Retrofit retrofit;

    public static void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static APIService getService() {
        return retrofit.create(APIService.class);
    }
}
