package edu.sjsu.fall2016.cs175.maps.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by kevin on 10/8/16.
 */

public interface APIService {

    //full url
    //https://api.unsplash.com/search/photos?client_id=67ac3222bfd4ba972349eee7f37387067105a795d63f186bbd1db774820deaa9&query=dog

    @GET("photos/random/?client_id=67ac3222bfd4ba972349eee7f37387067105a795d63f186bbd1db774820deaa9")
    Call<PicResponse> getPic();

    @GET
    Call<SearchResponse> getSearchPic(@Url String url);

    /*
    @GET("search/photos?client_id=67ac3222bfd4ba972349eee7f37387067105a795d63f186bbd1db774820deaa9&query=dog")
    Call<SearchResponse> getSearchPic();
    */




}
