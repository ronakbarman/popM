package com.ronak.popularmovies;

public  class Trailer {

    private String mName;
    private String mKey;

    public  Trailer(String name, String key) {
        mName = name;
        mKey = key;
    }

    public  String getName(){
        return mName;
    }
    public String getKey(){
        return  mKey;
    }

}
