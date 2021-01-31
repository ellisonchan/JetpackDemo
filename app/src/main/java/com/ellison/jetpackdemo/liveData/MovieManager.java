package com.ellison.jetpackdemo.liveData;

import com.ellison.jetpackdemo.R;

public class MovieManager {
    String name;

    public MovieManager(String name) {
        this.name = name;
    }

    void movieChangeRequest(MovieChangeListener listener) {
        listener.onMovieUpdated(new Movie(name, "Action", "Jackie Chan", R.drawable.ic_movie_post));
    }

    void movieChangeDrop(MovieChangeListener listener) {
    }
}
