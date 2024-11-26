package com.example.bookreview;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookreview.Helper.BookDatabaseHelper;
import com.example.bookreview.Model.Book;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private TextView bookTitle, bookAuthor, bookYear, bookPlatform;
    private ImageView bookCover;
    private Button addToReadingList, addToWishlist;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        bookTitle = findViewById(R.id.bookTitle);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookYear = findViewById(R.id.bookYear);
        bookPlatform = findViewById(R.id.bookPlatform);
        bookCover = findViewById(R.id.bookCover);
        addToReadingList = findViewById(R.id.addToReadingList);
        addToWishlist = findViewById(R.id.addToWishlist);

        Intent intent = getIntent();
        if (intent != null) {
            book = new Book(
                    intent.getStringExtra("BOOK_TITLE"),
                    intent.getStringExtra("BOOK_AUTHOR"),
                    intent.getStringExtra("BOOK_YEAR"),
                    intent.getStringExtra("BOOK_PLATFORM"),
                    intent.getIntExtra("BOOK_COVER", 0)
            );

            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
            bookYear.setText(book.getYear());
            bookPlatform.setText(book.getPlatform());
            bookCover.setImageResource(book.getCoverResourceId());
        }

        addToReadingList.setOnClickListener(v -> {
            BookDatabaseHelper dbHelper = new BookDatabaseHelper(this);
            dbHelper.addBookToReadingList(book); // Add book to reading list
            Toast.makeText(this, "Book added to Reading List", Toast.LENGTH_SHORT).show();
        });

        addToWishlist.setOnClickListener(v -> {
            BookDatabaseHelper dbHelper = new BookDatabaseHelper(this);
            dbHelper.addBookToWishlist(book); // Add book to wishlist
            Toast.makeText(this, "Book added to Wishlist", Toast.LENGTH_SHORT).show();
        });

    }

}
