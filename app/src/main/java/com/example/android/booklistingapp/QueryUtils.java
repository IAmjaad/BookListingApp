package com.example.android.booklistingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Amjaad on 9/17/2017.
 */

public final class QueryUtils {

    private QueryUtils() {
    }

    public static ArrayList<Books> extractBooksInfo(String jsonResponse) {

        ArrayList<Books> books = new ArrayList<>();

        try {
            JSONObject JsonResponse = new JSONObject(jsonResponse);
            JSONArray booksArray = JsonResponse.getJSONArray("items");

            for (int i = 0; i < booksArray.length(); i++) {
                JSONObject currentBook = booksArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                String authors = authorsList(authorsArray);

                Books book = new Books(title, authors);
                books.add(book);
            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the books JSON results", e);
        }
        return books;
    }

    public static String authorsList(JSONArray authors) throws JSONException {
        String authorsNames ;

        if (authors.length() == 0) {
            return null;
        }

        authorsNames = "By: ";
        for (int i = 0; i < authors.length(); i++) {
            authorsNames += authors.getString(i) + ". ";
        }
        return authorsNames;
    }
}