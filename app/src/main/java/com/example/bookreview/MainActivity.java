package com.example.bookreview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookreview.Adapter.BooksAdapter;
import com.example.bookreview.Model.Book;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BooksAdapter booksAdapter;
    private List<Book> bookList;
    Button read, bookmark, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SearchView searchView = findViewById(R.id.SearchView);

        read = findViewById(R.id.read);
        bookmark = findViewById(R.id.bookmark);
        logout = findViewById(R.id.Logout);

        // Initialize bookList
        bookList = new ArrayList<>();
        bookList.add(new Book("The Woman in the Window", "A. J. Finn", "2018", "Apple Books", R.drawable.one));
        bookList.add(new Book("To Kill a Mockingbird", "Harper Lee", "1960", "Penguin Books", R.drawable.two));
        bookList.add(new Book("1984", "George Orwell", "1949", "Houghton Mifflin Harcourt", R.drawable.three));
        bookList.add(new Book("Pride and Prejudice", "Jane Austen", "1813", "Oxford University Press", R.drawable.four));
        bookList.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "1925", "Scribner", R.drawable.five));
        bookList.add(new Book("The Catcher in the Rye", "J.D. Salinger", "1951", "Little, Brown and Company", R.drawable.six));
        bookList.add(new Book("The Alchemist", "Paulo Coelho", "1988", "HarperOne", R.drawable.seven));
        bookList.add(new Book("The Hobbit", "J.R.R. Tolkien", "1937", "George Allen & Unwin", R.drawable.eight));
        bookList.add(new Book("The Kite Runner", "Khaled Hosseini", "2003", "Riverhead Books", R.drawable.nine));
        bookList.add(new Book("The Road", "Cormac McCarthy", "2006", "Alfred A. Knopf", R.drawable.ten));
        bookList.add(new Book("Life of Pi", "Yann Martel", "2001", "Canongate Books", R.drawable.eleven));

        // Initialize the adapter with the populated bookList
        booksAdapter = new BooksAdapter(this, bookList, false); // No delete icon in MainActivity
        recyclerView.setAdapter(booksAdapter);

        // Search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                booksAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                booksAdapter.getFilter().filter(newText);
                return false;
            }
        });

        // Read button click
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                startActivity(intent);
            }
        });

        // Bookmark button click
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WishList.class);
                startActivity(intent);
            }
        });

        // Logout button click
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
