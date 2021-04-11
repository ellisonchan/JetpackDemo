package com.ellison.jetpackdemo.hilt

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.ellison.jetpackdemo.databinding.ActivityHiltBinding
import com.ellison.jetpackdemo.hilt.bean.Movie
import com.ellison.jetpackdemo.hilt.bean.MovieResponse
import com.ellison.jetpackdemo.hilt.view.MovieAdapter
import com.ellison.jetpackdemo.hilt.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DemoActivity : AppCompatActivity() {
    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var binding: ActivityHiltBinding
    @Inject
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHiltBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.isSubmitButtonEnabled = true
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("Hilt", "onQueryTextSubmit() got query:$query")
                movieViewModel.searchMovie(query) { response: MovieResponse<List<Movie>> ->
                    Log.d("Hilt", "onChanged() got response:$response")
                    bindRecyclerView(response.Search)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun bindRecyclerView(movieList: List<Movie>) {
        binding.movieList.layoutManager = GridLayoutManager(binding.movieList.context, 2)
        binding.movieList.addItemDecoration(DividerItemDecoration(binding.movieList.context,
                DividerItemDecoration.VERTICAL))

        movieAdapter.movieList = movieList
        movieAdapter.movieViewModel = movieViewModel
        binding.movieList.adapter = movieAdapter
    }
}