package com.ellison.jetpackdemo.room

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.ellison.jetpackdemo.R
import com.ellison.jetpackdemo.databinding.ActivityRoomDbBinding

class DemoActivity : AppCompatActivity() {
    private var movieViewModel: MovieViewModel? = null
    private var binding: ActivityRoomDbBinding? = null
    private var movieList: List<Movie?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoomDbBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.lifecycleOwner = this

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        movieViewModel?.getMovieList(this, { movieList: List<Movie?>? ->
            if (movieList == null) return@getMovieList
            Log.d(MovieDataBase.TAG, "onChanged() movieList:$movieList")

            this.movieList = movieList
            binding?.setMovieList(movieList)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.room_menu, menu)

        val myActionMenuItem = menu.findItem(R.id.action_search)
        val searchView = myActionMenuItem.actionView as SearchView

        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d(MovieDataBase.TAG, "onQueryTextSubmit() query:$query")

                if (!TextUtils.isEmpty(query) && movieViewModel != null) {
                    movieViewModel!!.searchMovie(this@DemoActivity, { movieList: List<Movie?>? ->
                        Log.d(MovieDataBase.TAG, "onChanged() movieList:$movieList")
                        binding?.setMovieList(movieList)
                    }, query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d(MovieDataBase.TAG, "onQueryTextChange() newText:$newText")
                if (TextUtils.isEmpty(newText)) {
                    binding?.setMovieList(movieList)
                }
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (movieViewModel == null) return false

        if (R.id.action_filter == item.itemId) {
            movieViewModel!!.filterMovieByScore(this, { movieList: List<Movie?>? ->
                Log.d(MovieDataBase.TAG, "onOptionsItemSelected sort onChanged() movieList:$movieList")
                binding?.setMovieList(movieList)
            }, 7.0)
            return true
        } else if (R.id.action_add == item.itemId) {
            movieViewModel!!.getMovie(this, { movie: Movie? ->
                val newMovie = Movie("Kungfu kid", "Jackie Chan", 2011, 7.0)
                Log.d(MovieDataBase.TAG, "onOptionsItemSelected add onChanged() movie:$movie")
                if (movie != null) {
                    movieViewModel!!.addDeleteMovie(this, null, newMovie, movie)
                }
            }, 1)
            return true
        }
        return false
    }
}