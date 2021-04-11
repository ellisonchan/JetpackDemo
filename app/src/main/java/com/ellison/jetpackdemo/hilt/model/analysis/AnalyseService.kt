package com.ellison.jetpackdemo.hilt.model.analysis

import android.util.Log
import com.ellison.jetpackdemo.hilt.bean.Movie
import com.ellison.jetpackdemo.hilt.model.database.MovieDao
import javax.inject.Inject

interface AnalysisService {
    fun checkSelectedMovie(movie: Movie): String
}

class AnalysisServiceImpl @Inject constructor (
        private val moviedao: MovieDao): AnalysisService {
    override fun checkSelectedMovie(movie: Movie): String {
        return saveMovie(movie)
    }

    private fun saveMovie(movie: Movie): String {
        val result = moviedao.insert(movie)
        Log.d("Hilt", "saveMovie result:$result");
        return movie.getTitle() + ":" + result
    }
}

