package edu.sjsu.fall2016.cs175.maps.model;

/**
 * Created by kevin on 10/8/16.
 */

public class PicResponse {
    String id;
    private PicModel urls;

    public String getPicURL(){
        return urls.full;
    }


}
