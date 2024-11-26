package com.example.bookreview.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookreview.DetailsActivity;
import com.example.bookreview.Model.Book;
import com.example.bookreview.R;
import com.example.bookreview.Helper.BookDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> implements Filterable {

    private List<Book> booksList;
    private List<Book> booksListFull;
    private Context context;
    private SQLiteDatabase database;
    boolean showDeleteIcon; // To control whether the delete icon should be visible

    public BooksAdapter(Context context, List<Book> booksList, boolean showDeleteIcon) {
        this.context = context;
        this.booksList = booksList;
        this.showDeleteIcon = showDeleteIcon;
        this.booksListFull = new ArrayList<>(booksList);

        BookDatabaseHelper dbHelper = new BookDatabaseHelper(context);
        this.database = dbHelper.getWritableDatabase();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthor());
        holder.bookYear.setText(book.getYear());
        holder.bookPlatform.setText(book.getPlatform());
        holder.bookCover.setImageResource(book.getCoverResourceId());

        // Open DetailsActivity when book is clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("BOOK_TITLE", book.getTitle());
            intent.putExtra("BOOK_AUTHOR", book.getAuthor());
            intent.putExtra("BOOK_YEAR", book.getYear());
            intent.putExtra("BOOK_PLATFORM", book.getPlatform());
            intent.putExtra("BOOK_COVER", book.getCoverResourceId());
            context.startActivity(intent);
        });

        // Handle delete icon visibility
        holder.deleteImageView.setVisibility(View.GONE); // Initially hide delete icon
        if (showDeleteIcon) {
            holder.deleteImageView.setVisibility(View.VISIBLE);
        } else {
            holder.deleteImageView.setVisibility(View.GONE);
        }

        // OnClick for the delete icon
        holder.deleteImageView.setOnClickListener(v -> removeBook(position));
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    // Method to remove a book from the list and database
    public void removeBook(int position) {
        Book book = booksList.get(position);

        // Remove from the database
        String whereClause = "title = ?";  // You may want to use an ID to delete more precisely
        String[] whereArgs = {book.getTitle()};
        database.delete("reading_list", whereClause, whereArgs);  // Adjust table name as needed

        // Remove from the list and notify adapter
        booksList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public Filter getFilter() {
        return bookFilter;
    }

    private final Filter bookFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(booksListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Book book : booksListFull) {
                    if (book.getTitle().toLowerCase().contains(filterPattern) ||
                            book.getAuthor().toLowerCase().contains(filterPattern)) {
                        filteredList.add(book);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            booksList.clear();
            booksList.addAll((List<Book>) results.values);
            notifyDataSetChanged();
        }
    };

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle, bookAuthor, bookYear, bookPlatform;
        ImageView bookCover, deleteImageView;

        public BookViewHolder(View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookCover = itemView.findViewById(R.id.bookCover);
            bookYear = itemView.findViewById(R.id.bookYear);
            bookPlatform = itemView.findViewById(R.id.bookPlatform);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);  // Delete icon
        }
    }
}
