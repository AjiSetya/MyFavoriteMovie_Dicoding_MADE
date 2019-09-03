package com.aji.myfavoritemovie;

import android.database.Cursor;

import com.aji.myfavoritemovie.entity.MovieItem;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.aji.myfavoritemovie.db.DatabaseContract.MovieColumns.DATE;
import static com.aji.myfavoritemovie.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.aji.myfavoritemovie.db.DatabaseContract.MovieColumns.POSTER;
import static com.aji.myfavoritemovie.db.DatabaseContract.MovieColumns.TITLE;

public class MappingHelper {
    public static ArrayList<MovieItem> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<MovieItem> movieItems = new ArrayList<>();

        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(_ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
            String overview = notesCursor.getString(notesCursor.getColumnIndexOrThrow(OVERVIEW));
            String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DATE));
            String poster = notesCursor.getString(notesCursor.getColumnIndexOrThrow(POSTER));
            movieItems.add(new MovieItem(id, title, overview, date, poster));
        }

        return movieItems;
    }
}
