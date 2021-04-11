package com.ellison.jetpackdemo.hilt.model.network

import com.ellison.jetpackdemo.hilt.bean.Movie
import com.ellison.jetpackdemo.hilt.bean.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("http://omdbapi.com/")
    suspend fun requestSearchByCoroutines(
            @Query("s") keywords: String,
            @Query("apikey") apikey: String
    ): MovieResponse<List<Movie>>
}