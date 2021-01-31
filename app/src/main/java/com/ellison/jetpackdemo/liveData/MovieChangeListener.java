package com.ellison.jetpackdemo.liveData;

public interface MovieChangeListener {
    default void onMovieUpdated(Movie updatedMovie) {}
}
