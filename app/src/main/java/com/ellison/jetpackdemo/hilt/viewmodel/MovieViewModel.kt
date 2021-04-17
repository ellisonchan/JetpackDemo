package com.ellison.jetpackdemo.hilt.viewmodel

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ellison.jetpackdemo.hilt.bean.Movie
import com.ellison.jetpackdemo.hilt.bean.MovieResponse
import com.ellison.jetpackdemo.hilt.model.Repository
import com.ellison.jetpackdemo.hilt.view.MovieAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MovieViewModel @ViewModelInject constructor(private val repository: Repository,
                                                  var movieAdapter: MovieAdapter
) : ViewModel() {
    private val resultData = MutableLiveData<MovieResponse<List<Movie>>>()

    fun searchMovie(keyWord: String, fragmentActivity: FragmentActivity, observer: Observer<MovieResponse<List<Movie>>>) {
        Log.d("Hilt", "searchMovie() repository:$repository")
        resultData.observe(fragmentActivity, observer)

        GlobalScope.launch(Dispatchers.Main) {
            Log.d("Hilt", "searchMovie() searchMovieFromNetwork keyWord:$keyWord")
            val response = repository.searchMovieFromNetwork(keyWord)
            resultData.postValue(response)
        }
    }

    fun analyseMovie(movie: Movie): String {
        return repository.analyseMovieByAI(movie)
    }
}