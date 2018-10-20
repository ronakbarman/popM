package com.ronak.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Review {

    private String mAuthor;
    private  String mContent;

    public String getmAuthor(){
        return  mAuthor;
    }

    public String getmContent(){
        return mContent;
    }

    public void setContent(String content){
        mContent = content;
    }

    Review(String author, String content) {
        mAuthor = author;
        mContent = content;
    }


}
