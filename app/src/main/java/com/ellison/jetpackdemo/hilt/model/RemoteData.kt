package com.ellison.jetpackdemo.hilt.model

import android.util.Log
import com.ellison.jetpackdemo.hilt.bean.Movie
import com.ellison.jetpackdemo.hilt.bean.MovieResponse
import com.ellison.jetpackdemo.hilt.model.network.NetworkService
import javax.inject.Inject

// class RemoteData @Inject constructor()
class RemoteData @Inject constructor(private val networkService: NetworkService) {
    suspend fun searchMovie(keyWord: String): MovieResponse<List<Movie>> {
        Log.d("Hilt", "searchMovie() networkService:$networkService")
        return networkService.requestSearchByCoroutines(keyWord, "19b0bce5")
    }
}