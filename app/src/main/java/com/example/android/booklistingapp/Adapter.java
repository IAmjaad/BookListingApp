package com.example.android.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Amjaad on 9/17/2017.
 */

public class Adapter extends ArrayAdapter<Books> {

    ArrayList<Books> list;
    Context context;

    public Adapter(Context context, ArrayList<Books> list) {

        super(context, 0, list);
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Books getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {


        View listItemView = view;

        if (listItemView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listItemView = inflater.inflate(R.layout.books_info, parent, false);
        }

        Books currentBook = getItem(position);
        TextView title = listItemView.findViewById(R.id.bookTitle);
        title.setText(currentBook.getTitle());
        TextView author = listItemView.findViewById(R.id.bookAuthor);
        author.setText(currentBook.getAuthors());
        return listItemView;
    }

}
