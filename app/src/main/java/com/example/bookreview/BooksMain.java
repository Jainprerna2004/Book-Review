package com.example.bookreview;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookreview.Model.Book;

public class BooksMain extends AppCompatActivity {

    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_item);

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("BOOK_TITLE");
            String author = intent.getStringExtra("BOOK_AUTHOR");
            String year = intent.getStringExtra("BOOK_YEAR");
            String platform = intent.getStringExtra("BOOK_PLATFORM");
            int coverImage = intent.getIntExtra("BOOK_COVER", 0);

            book = new Book(title, author, year, platform, coverImage);
        }

    }
}
