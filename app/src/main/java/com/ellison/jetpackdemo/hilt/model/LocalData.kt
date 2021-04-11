package com.ellison.jetpackdemo.hilt.model

import android.util.Log
import com.ellison.jetpackdemo.hilt.bean.Movie
import com.ellison.jetpackdemo.hilt.model.analysis.AnalysisService
import com.ellison.jetpackdemo.hilt.model.database.MovieDao
import javax.inject.Inject

class LocalData @Inject constructor(private val analysisService: AnalysisService) {
    private var count = 0

    fun analyseMovieByAI(movie: Movie): String {
        return analysisService.checkSelectedMovie(movie)
    }
}