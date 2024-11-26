package com.example.bookreview.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;  // Import the Log class

import com.example.bookreview.Model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookDatabase";
    private static final int DATABASE_VERSION = 2;  // Increment version if schema changes

    private static final String TABLE_READING_LIST = "readingList";
    private static final String TABLE_WISHLIST = "wishlist";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_PLATFORM = "platform";
    private static final String COLUMN_COVER = "cover";

    public BookDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_READING_LIST + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_YEAR + " TEXT, " +
                COLUMN_PLATFORM + " TEXT, " +
                COLUMN_COVER + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_WISHLIST + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_YEAR + " TEXT, " +
                COLUMN_PLATFORM + " TEXT, " +
                COLUMN_COVER + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_READING_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST);
        onCreate(db);
    }

    public void addBookToReadingList(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, book.getTitle());
        values.put(COLUMN_AUTHOR, book.getAuthor());
        values.put(COLUMN_YEAR, book.getYear());
        values.put(COLUMN_PLATFORM, book.getPlatform());
        values.put(COLUMN_COVER, book.getCoverResourceId());

        long result = db.insert(TABLE_READING_LIST, null, values);
        if (result == -1) {
            // Log error if insert fails
            Log.e("Database", "Failed to insert book into readingList.");
        }
        db.close();
    }

    public List<Book> getAllReadingListBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Book> books = new ArrayList<>();

        Cursor cursor = db.query(TABLE_READING_LIST, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Log the column index for debugging
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
                int authorIndex = cursor.getColumnIndex(COLUMN_AUTHOR);
                int yearIndex = cursor.getColumnIndex(COLUMN_YEAR);
                int platformIndex = cursor.getColumnIndex(COLUMN_PLATFORM);
                int coverIndex = cursor.getColumnIndex(COLUMN_COVER);

                Log.d("ColumnIndexes", "ID: " + idIndex + ", Title: " + titleIndex + ", Author: " + authorIndex +
                        ", Year: " + yearIndex + ", Platform: " + platformIndex + ", Cover: " + coverIndex);

                // Check if column index is valid (≥ 0)
                if (idIndex >= 0 && titleIndex >= 0 && authorIndex >= 0 && yearIndex >= 0 && platformIndex >= 0 && coverIndex >= 0) {
                    // Create Book object using the valid column indices
                    Book book = new Book(
                            cursor.getInt(idIndex),
                            cursor.getString(titleIndex),
                            cursor.getString(authorIndex),
                            cursor.getString(yearIndex),
                            cursor.getString(platformIndex),
                            cursor.getInt(coverIndex)
                    );
                    books.add(book);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return books;
    }

    public void addBookToWishlist(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, book.getTitle());
        values.put(COLUMN_AUTHOR, book.getAuthor());
        values.put(COLUMN_YEAR, book.getYear());
        values.put(COLUMN_PLATFORM, book.getPlatform());
        values.put(COLUMN_COVER, book.getCoverResourceId());

        long result = db.insert(TABLE_WISHLIST, null, values);
        if (result == -1) {
            // Log error if insert fails
            Log.e("Database", "Failed to insert book into wishlist.");
        }
        db.close();
    }

    public List<Book> getAllWishlistBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Book> books = new ArrayList<>();

        Cursor cursor = db.query(TABLE_WISHLIST, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Log all column names to see what's actually being returned
            String[] columnNames = cursor.getColumnNames();
            for (String column : columnNames) {
                Log.d("Database", "Column: " + column);
            }

            // Check if the column indexes are valid
            int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
            int columnIndexTitle = cursor.getColumnIndex(COLUMN_TITLE);
            int columnIndexAuthor = cursor.getColumnIndex(COLUMN_AUTHOR);
            int columnIndexYear = cursor.getColumnIndex(COLUMN_YEAR);
            int columnIndexPlatform = cursor.getColumnIndex(COLUMN_PLATFORM);
            int columnIndexCover = cursor.getColumnIndex(COLUMN_COVER);

            Log.d("Database", "COLUMN_ID index: " + columnIndexId);
            Log.d("Database", "COLUMN_TITLE index: " + columnIndexTitle);

            // Validate column indices and fetch the data
            if (columnIndexId != -1 && columnIndexTitle != -1) {
                do {
                    Book book = new Book(
                            cursor.getInt(columnIndexId),
                            cursor.getString(columnIndexTitle),
                            cursor.getString(columnIndexAuthor),
                            cursor.getString(columnIndexYear),
                            cursor.getString(columnIndexPlatform),
                            cursor.getInt(columnIndexCover)
                    );
                    books.add(book);
                } while (cursor.moveToNext());
            } else {
                Log.e("Database", "Column indices are invalid. Unable to fetch data.");
            }

            cursor.close();
        }

        db.close();
        return books;
    }

}
