package com.ellison.jetpackdemo.dagger2.model

import com.ellison.jetpackdemo.dagger2.bean.Movie
import com.ellison.jetpackdemo.dagger2.bean.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("http://omdbapi.com/")
    suspend fun requestSearchByCoroutines(
            @Query("s") keywords: String,
            @Query("apikey") apikey: String
    ): MovieResponse<List<Movie>>
}