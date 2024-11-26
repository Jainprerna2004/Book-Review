package com.example.bookreview;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookreview.Adapter.BooksAdapter;
import com.example.bookreview.Helper.BookDatabaseHelper;
import com.example.bookreview.Model.Book;

import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        SearchView searchView = findViewById(R.id.SearchView);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        List<Book> readingList = getBooksFromDatabase("readingList");

        BooksAdapter adapter = new BooksAdapter(this, readingList, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        setupSwipeToDelete(recyclerView, adapter);

    }


    private List<Book> getBooksFromDatabase(String tableName) {
        BookDatabaseHelper dbHelper = new BookDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Book> books = new ArrayList<>();

        // Check if the table exists
        if (!doesTableExist(db, tableName)) {
            Log.e("ReadActivity", "Table " + tableName + " does not exist in the database.");
            return books; // Return an empty list if the table doesn't exist
        }

        // Query the database
        Cursor cursor = db.query(
                tableName,
                new String[]{"title", "author", "year", "platform", "cover"},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int titleIndex = cursor.getColumnIndex("title");
                int authorIndex = cursor.getColumnIndex("author");
                int yearIndex = cursor.getColumnIndex("year");
                int platformIndex = cursor.getColumnIndex("platform");
                int coverIndex = cursor.getColumnIndex("cover");

                if (titleIndex != -1 && authorIndex != -1 && yearIndex != -1 &&
                        platformIndex != -1 && coverIndex != -1) {
                    books.add(new Book(
                            cursor.getString(titleIndex),
                            cursor.getString(authorIndex),
                            cursor.getString(yearIndex),
                            cursor.getString(platformIndex),
                            cursor.getInt(coverIndex)
                    ));
                } else {
                    Log.e("ReadActivity", "Invalid column index found in cursor.");
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return books;
    }


    private boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{tableName}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private void setupSwipeToDelete(RecyclerView recyclerView, BooksAdapter adapter) {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                adapter.removeBook(position);  // Remove the item from list and database
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}
