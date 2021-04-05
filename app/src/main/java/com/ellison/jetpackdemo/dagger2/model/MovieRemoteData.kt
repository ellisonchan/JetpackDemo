package com.ellison.jetpackdemo.dagger2.model

import android.util.Log
import com.ellison.jetpackdemo.dagger2.bean.Movie
import com.ellison.jetpackdemo.dagger2.bean.MovieResponse
import javax.inject.Inject

    class MovieRemoteData @Inject constructor(private val movieService: MovieService) {
    suspend fun searchMovie(keyWorld: String): MovieResponse<List<Movie>> {
        Log.d("Dagger", "searchMovie() movieService:$movieService")
        return movieService.requestSearchByCoroutines(keyWorld, "19b0bce5")
    }
}