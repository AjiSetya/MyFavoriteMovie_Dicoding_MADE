package com.aji.myfavoritemovie;

import android.database.Cursor;

public interface LoadMovieCallback {
    void postExecute(Cursor movies);
}
