package com.example.android.booklistingapp;

/**
 * Created by Amjaad on 9/17/2017.
 */


public class Books {

    private String title;
    private String authors;

    public Books(String title, String author) {
        this.title = title;
        this.authors = author;
    }

    public String getAuthors() {
        return authors;
    }

    public String getTitle() {
        return title;
    }

}
