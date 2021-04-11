package com.ellison.jetpackdemo.hilt.model

import android.util.Log
import com.ellison.jetpackdemo.hilt.bean.Movie
import com.ellison.jetpackdemo.hilt.bean.MovieResponse
import javax.inject.Inject

class Repository @Inject constructor(private val remoteData: RemoteData, private val localData: LocalData) {
    suspend fun searchMovieFromNetwork(keyWord: String): MovieResponse<List<Movie>> {
        Log.d("Hilt", "searchMovieFromNetwork() remoteData:$remoteData")
        return remoteData.searchMovie(keyWord)
    }

    fun analyseMovieByAI(movie: Movie): String {
        Log.d("Hilt", "analyseMovieByAI() localData:$localData movie:$movie")
        return localData.analyseMovieByAI(movie)
    }
}