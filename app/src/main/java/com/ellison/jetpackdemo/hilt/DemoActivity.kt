package com.ellison.jetpackdemo.hilt

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.ellison.jetpackdemo.R
import com.ellison.jetpackdemo.databinding.ActivityHiltBinding
import com.ellison.jetpackdemo.hilt.bean.Movie
import com.ellison.jetpackdemo.hilt.bean.MovieResponse
import com.ellison.jetpackdemo.hilt.view.MovieAdapter
import com.ellison.jetpackdemo.hilt.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

// @AndroidEntryPoint
class DemoActivity : BaseActivity() {
    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var binding: ActivityHiltBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHiltBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.isSubmitButtonEnabled = true
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("Hilt", "onQueryTextSubmit() got query:$query")
                movieViewModel.searchMovie(query, this@DemoActivity) { response: MovieResponse<List<Movie>> ->
                    Log.d("Hilt", "onChanged() got response:$response")

                    if (response.Response.toBoolean() && response.Search != null) {
                        bindRecyclerView(response.Search)
                    } else {
                        Toast.makeText(
                                this@DemoActivity,
                                "Got no movie",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

//        supportFragmentManager.beginTransaction()
//                .add(R.id.container, DemoFragment::class.java, null)
//                .commit()
    }

    private fun bindRecyclerView(movieList: List<Movie>) {
        movieAdapter = movieViewModel.movieAdapter
        movieAdapter.movieList = movieList
        movieAdapter.movieViewModel = movieViewModel

        binding.movieList.layoutManager = GridLayoutManager(binding.movieList.context, 2)
        binding.movieList.addItemDecoration(DividerItemDecoration(binding.movieList.context,
                DividerItemDecoration.VERTICAL))
        binding.movieList.adapter = movieAdapter
    }
}