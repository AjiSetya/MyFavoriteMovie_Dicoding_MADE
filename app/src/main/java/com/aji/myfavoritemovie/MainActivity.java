package com.aji.myfavoritemovie;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.aji.myfavoritemovie.adapter.FavoriteMovieAdapter;
import com.aji.myfavoritemovie.entity.MovieItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.aji.myfavoritemovie.MappingHelper.mapCursorToArrayList;
import static com.aji.myfavoritemovie.db.DatabaseContract.MovieColumns.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements LoadMovieCallback {

    private FavoriteMovieAdapter favoriteMovieAdapter;
    private DataObserver dataObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvFavMovie = findViewById(R.id.rv_favmovie);
        favoriteMovieAdapter = new FavoriteMovieAdapter(this);
        rvFavMovie.setLayoutManager(new LinearLayoutManager(this));
        rvFavMovie.setHasFixedSize(true);
        rvFavMovie.setAdapter(favoriteMovieAdapter);

        favoriteMovieAdapter.setOnItemClickCallback(new FavoriteMovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(MovieItem data) {
                Toast.makeText(MainActivity.this, data.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        dataObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, dataObserver);
        new getData(this, this).execute();
    }

    @Override
    public void postExecute(Cursor movies) {
        ArrayList<MovieItem> movieItems = mapCursorToArrayList(movies);
        if (movieItems.size() > 0) {
            favoriteMovieAdapter.setListMovies(movieItems);
        } else {
            Toast.makeText(this, "Tidak Ada data saat ini", Toast.LENGTH_SHORT).show();
            favoriteMovieAdapter.setListMovies(new ArrayList<MovieItem>());
        }
    }

    private static class getData extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieCallback> weakCallback;


        private getData(Context context, LoadMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(CONTENT_URI, null,
                    null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor data) {
            super.onPostExecute(data);
            weakCallback.get().postExecute(data);
        }
    }

    static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new getData(context, (MainActivity) context).execute();
        }
    }
}
