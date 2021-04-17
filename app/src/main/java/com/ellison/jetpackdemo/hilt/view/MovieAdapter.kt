package com.ellison.jetpackdemo.hilt.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ellison.jetpackdemo.databinding.ActivityHiltItemBinding
import com.ellison.jetpackdemo.hilt.bean.Movie
import com.ellison.jetpackdemo.hilt.viewmodel.MovieViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class MovieAdapter @Inject constructor(@ActivityContext private val context: Context)
        : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var movieList: List<Movie>
    lateinit var movieViewModel: MovieViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("Hilt", "MovieAdapter#onCreateViewHolder() parent:$parent")
        return MovieHolder(ActivityHiltItemBinding.inflate(LayoutInflater.from(parent.context),
                parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("Hilt", "MovieAdapter#onBindViewHolder() holder:$holder position:$position")
        val itemBinding = ((holder as MovieHolder).binding as ActivityHiltItemBinding)
        itemBinding.movie = movieList[position]
        itemBinding.movieName.tag = position
        itemBinding.setResponder(EventResponder())
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    private class MovieHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    // Event binding.
    inner class EventResponder {
        fun onClick(view: View) {
            Log.d("Hilt", "onClick() view:$view")
            val result = movieViewModel?.analyseMovie(movieList.get(view.tag as Int))
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
        }
    }
}