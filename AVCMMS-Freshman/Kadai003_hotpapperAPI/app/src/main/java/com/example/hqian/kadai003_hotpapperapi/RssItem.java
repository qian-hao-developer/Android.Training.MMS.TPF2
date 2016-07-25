package com.example.hqian.kadai003_hotpapperapi;

/**
 * Created by hqian on 16/06/24.
 */


public class RssItem {
    String id;
    String name;
    String address;
    String lat;
    String lng;

    public RssItem(String id, String name, String address, String lat, String lng){
        this.id = id;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    // set method
    public void setId(String id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setAddr(String address){
        this.address = address;
    }
    public void setLat(String lat){
        this.lat = lat;
    }
    public void setLng(String lng){
        this.lng = lng;
    }

    // get method
    public String getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getAddr(){
        return this.address;
    }
    public String getLat(){
        return this.lat;
    }
    public String getLng(){
        return this.lng;
    }
}
