package com.example.android.booklistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>>{

    ListView listView;
    EditText editText;
    Button button;
    Adapter adapter;
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    //key=AIzaSyBBljU5TvdxTKmgBeUprwMiipTG1ITw7aQ&
    private final int Books_LOADER_ID = 0;
    static String searchedBook;
    private TextView emptyView;
    String noBooks = "No books found.";
    String noInternet = "There is no internt.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(savedInstanceState != null) {
//            searchedBook = editText.getText().toString();
//            getSupportLoaderManager().initLoader(Books_LOADER_ID, null, MainActivity.this);}

        adapter = new Adapter(this, new ArrayList<Books>());
        listView = (ListView) findViewById(R.id.list);
//        listView.setAdapter(adapter);
        button = (Button) findViewById(R.id.searchButton);
        editText = (EditText) findViewById(R.id.searchBar);
        emptyView = (TextView) findViewById(R.id.empty_TextView);
        listView.setEmptyView(emptyView);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listView.setAdapter(adapter);
                searchedBook = editText.getText().toString();
                ConnectivityManager connectivityManager = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
//                    searchedBook = editText.getText().toString();
                    LoaderManager loaderManager = getSupportLoaderManager();
                    loaderManager.initLoader(Books_LOADER_ID, null, MainActivity.this);
                } else {
                    emptyView.setText(noInternet);
                }
            }

        });

        if(savedInstanceState != null) {
//            adapter.clear();
            searchedBook = editText.getText().toString();
            getSupportLoaderManager().initLoader(Books_LOADER_ID, null, MainActivity.this);
    }}

    @Override
    public Loader<List<Books>> onCreateLoader(int id, Bundle args) {

        return new BooksAsyncTask(this, API_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> books) {

        emptyView.setText(noBooks);

        if (books == null) {
            Toast.makeText(MainActivity.this, "Please enter book name!", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter.clear();


        if ( books != null && !books.isEmpty()) {
//            adapter.clear();
            listView.setAdapter(adapter);
            adapter.addAll(books);
            adapter.notifyDataSetChanged();
        }
        else {
            emptyView.setText(noBooks);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        adapter.clear();
    }

    public static class BooksAsyncTask extends AsyncTaskLoader<List<Books>> {

        private String qURL;
//        private ArrayList<Books> books;

        public BooksAsyncTask(Context context, String url) {
            super(context);
            qURL = url;
        }

        @Override
        protected void onStartLoading() {
//            if (books != null) {
//                deliverResult(books);
//                return;
//            }
            forceLoad();
        }

        @Override
        public ArrayList<Books> loadInBackground() {

            URL url = createUrl(qURL + searchedBook);

            int length;
            length = searchedBook.length();

            if (length == 0) {
                return null;
            }

            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {

                Log.e("ERROR HERE!!!!!", "HttpRequest failed", e);
            }
            ArrayList<Books> booksList = QueryUtils.extractBooksInfo(jsonResponse);
            return booksList;
        }

        private URL createUrl(String stringUrl) {
            try {
                return new URL(stringUrl);
            } catch (MalformedURLException exception) {
                return null;
            }
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                urlConnection.getResponseCode();

                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }

            } catch (Exception e) {
                Log.e("ERROR HERE!!!!!", "Connection failed", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }
    }

}
