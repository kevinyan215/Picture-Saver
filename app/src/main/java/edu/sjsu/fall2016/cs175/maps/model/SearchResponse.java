package edu.sjsu.fall2016.cs175.maps.model;

/**
 * Created by kevin on 10/8/16.
 */

public class SearchResponse {
    int total;
    int total_pages;
    private PicResponse[] results;

    public PicResponse[] getPicResponse(){
        return results;
    }


}
