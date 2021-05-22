package com.ellison.jetpackdemo.hilt.viewmodel

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ellison.jetpackdemo.hilt.bean.Movie
import com.ellison.jetpackdemo.hilt.bean.MovieResponse
import com.ellison.jetpackdemo.hilt.model.Repository
import com.ellison.jetpackdemo.hilt.view.MovieAdapter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel @ViewModelInject constructor(private val repository: Repository,
                                                  var movieAdapter: MovieAdapter
) : ViewModel() {
    private val resultData = MutableLiveData<MovieResponse<List<Movie>>>()

    fun searchMovie(keyWord: String, fragmentActivity: FragmentActivity, observer: Observer<MovieResponse<List<Movie>>>) {
        Log.d("Hilt", "searchMovie() repository:$repository")
        resultData.observe(fragmentActivity, observer)

        viewModelScope.launch(Dispatchers.Main + CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.d("Hilt", "coroutine exception: $throwable")
        }) {
            Log.d("Hilt", "searchMovie() searchMovieFromNetwork keyWord:$keyWord")
            val response = repository.searchMovieFromNetwork(keyWord)
            resultData.value = response
        }
    }

    fun analyseMovie(movie: Movie): String {
        return repository.analyseMovieByAI(movie)
    }
}