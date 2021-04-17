package com.ellison.jetpackdemo.hilt

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
// import android.app.Fragment
import androidx.fragment.app.activityViewModels

import com.ellison.jetpackdemo.R
import com.ellison.jetpackdemo.hilt.bean.Movie
import com.ellison.jetpackdemo.hilt.bean.MovieResponse
import com.ellison.jetpackdemo.hilt.viewmodel.MovieViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DemoFragment : Fragment(R.layout.fragment_hilt) {
    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true
        movieViewModel.searchMovie("", requireActivity()) {
            response: MovieResponse<List<Movie>> ->
                Log.d("Hilt", "onChanged() got response:$response")
        }
    }
}