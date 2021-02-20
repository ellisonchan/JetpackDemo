package com.ellison.jetpackdemo.room

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ellison.jetpackdemo.databinding.ActivityRoomDbItemBinding
import com.ellison.jetpackdemo.databinding.ActivityViewModelBindingItemBinding
import java.util.*

class MovieAdapter(movieList: List<Movie>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), GestureListener {
    private var movieList: List<Movie>?
    private var movieGestureCallback: MovieGestureCallback? = null
    private var gestureDone = true

    override fun onGestureDone() {
        Log.d(MovieDataBase.TAG, "onGestureDone() RESET gestureDone:$gestureDone")
        gestureDone = true
    }

    override fun onDeleteItem(targetHolder: RecyclerView.ViewHolder?) {
        Log.d(MovieDataBase.TAG, "onDeleteItem() targetHolder:$targetHolder")
        if (targetHolder == null || movieList == null) return

        val position = targetHolder.adapterPosition
        val result: Int = MovieDataBase.getInstance(targetHolder.itemView.context)!!.movieDao()
                .delete(getItem(position))

        Log.d(MovieDataBase.TAG, "onDeleteItem() result:$result")
        notifyItemRemoved(position)
    }

    override fun onSwapItem(fromHolder: RecyclerView.ViewHolder?, targetHolder: RecyclerView.ViewHolder?) {
        gestureDone = false
        Log.d(
                MovieDataBase.TAG,
                "onSwapItem() fromHolder:$fromHolder targetHolder:$targetHolder gestureDone:$gestureDone"
        )

        if (fromHolder == null || targetHolder == null || movieList == null) return

        val fromPosition = fromHolder.adapterPosition
        val toPosition = targetHolder.adapterPosition
        val fromMovie = getItem(fromPosition)
        val targetMovie = getItem(toPosition)
        val updatingMovies = buildUpdateMovies(fromMovie, targetMovie)

        // Movie[] updatingMovies = buildUpdateMoviesTruly(fromMovie, targetMovie);
        Log.d(MovieDataBase.TAG, "onSwapItem() updatingMovies:" + updatingMovies.contentToString())
        val result: Int = MovieDataBase.getInstance(targetHolder.itemView.context)
                ?.movieDao()?.update(*updatingMovies) ?: -1
        Log.d(MovieDataBase.TAG, "onSwapItem() result:$result")

        // Collections.swap(movieList, fromPosition, toPosition);
        // notifyItemMoved(fromPosition, toPosition);
    }

    private fun buildUpdateMovies(fromMovie: Movie?, targetMovie: Movie?): Array<Movie> {
        Log.d(MovieDataBase.TAG, "buildUpdateMovies() fromMovie:$fromMovie targetMovie:$targetMovie")

        val newFromMovie = Movie(targetMovie!!.name, targetMovie.actor, targetMovie.year, targetMovie.score)
        val newTargetMovie = Movie(fromMovie!!.name, fromMovie.actor, fromMovie.year, fromMovie.score)

        newFromMovie.id = fromMovie.id
        newTargetMovie.id = targetMovie.id

        return arrayOf(newFromMovie, newTargetMovie)
    }

    fun update(movieList: List<Movie>) {
        this.movieList = movieList
        Log.d(MovieDataBase.TAG, "update() movieList:$movieList")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (movieList != null) movieList!!.size else 0
    }

    private fun getItem(position: Int): Movie? {
        return if (movieList != null) movieList!![position] else null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieHolder(ActivityRoomDbItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = getItem(position)
        Log.d(MovieDataBase.TAG, "onBindViewHolder() movie:$movie")

        (holder as MovieHolder).binding.setMovie(movie)
    }

    private class MovieHolder(binding: ActivityRoomDbItemBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: ActivityRoomDbItemBinding

        init {
            this.binding = binding
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("bindData")
        fun bindAdapter(recyclerView: RecyclerView?, movieList: List<Movie>?) {
            Log.d(MovieDataBase.TAG, "bindAdapter() recyclerView:" + recyclerView
                    + " movieList:" + movieList)
            if (recyclerView == null || movieList == null) return

            val movieAdapter: MovieAdapter?
            if (recyclerView.adapter is MovieAdapter) {
                movieAdapter = recyclerView.adapter as MovieAdapter?
                movieAdapter!!.update(movieList)
                Log.d(MovieDataBase.TAG, "bindAdapter() update:$movieList")
            } else {
                movieAdapter = MovieAdapter(movieList)
                Log.d(MovieDataBase.TAG, "bindAdapter() setAdapter:"
                        + System.identityHashCode(movieAdapter))
                recyclerView.adapter = movieAdapter
                recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
                recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context,
                        DividerItemDecoration.VERTICAL))
            }
            if (movieAdapter.movieGestureCallback == null) {
                movieAdapter.movieGestureCallback = MovieGestureCallback(movieAdapter)
                val itemTouchHelper = ItemTouchHelper(movieAdapter.movieGestureCallback!!)
                Log.d(MovieDataBase.TAG, "bindAdapter() init gesture callback")
                itemTouchHelper.attachToRecyclerView(recyclerView)
            }
        }
    }

    init {
        this.movieList = movieList
        Log.d(MovieDataBase.TAG, "MovieAdapter() movieList:$movieList")
    }

    private fun buildUpdateMoviesTruly(fromMovie: Movie, targetMovie: Movie): Array<Movie> {
        Log.d(MovieDataBase.TAG, "buildUpdateMovies() fromMovie:" + fromMovie
                + " targetMovie:" + targetMovie)
        val tempMovie = Movie(fromMovie.name, fromMovie.actor, fromMovie.year, fromMovie.score)

        fromMovie.name = targetMovie.name
        fromMovie.actor = targetMovie.actor
        fromMovie.year = targetMovie.year
        fromMovie.score = targetMovie.score

        targetMovie.name = tempMovie.name
        targetMovie.actor = tempMovie.actor
        targetMovie.year = tempMovie.year
        targetMovie.score = tempMovie.score

        return arrayOf(fromMovie, targetMovie)
    }
}