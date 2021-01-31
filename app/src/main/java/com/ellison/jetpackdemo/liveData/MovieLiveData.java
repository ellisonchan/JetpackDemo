package com.ellison.jetpackdemo.liveData;

import android.util.Log;

import androidx.lifecycle.LiveData;
import static com.ellison.jetpackdemo.liveData.DemoActivity.TAG;

public class MovieLiveData extends LiveData<Movie> {
    MovieManager movieManager;

    MovieChangeListener listener = new MovieChangeListener() {
        @Override
        public void onMovieUpdated(Movie updatedMovie) {
            Log.d(TAG, "onMovieUpdated() updatedMovie:" + updatedMovie);
            setValue(updatedMovie);
        }
    };

    public MovieLiveData(String name) {
        movieManager = new MovieManager(name);
    }

    @Override
    protected void onActive() {
        super.onActive();
        Log.d(TAG, "onActive()");
        movieManager.movieChangeRequest(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d(TAG, "onInactive()");
        movieManager.movieChangeDrop(listener);
    }
}
