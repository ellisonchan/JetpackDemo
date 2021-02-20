package com.ellison.jetpackdemo.room

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val mediatorLiveData = MediatorLiveData<List<Movie?>?>()
    private val db: MovieDataBase?
    private val mContext: Context
    val searchLiveData  = MutableLiveData<List<Movie?>?>()

    fun getMovie(owner: LifecycleOwner?, observer: Observer<Movie?>?, id: Int) {
        if (db == null || id < 1) return
        Log.d(MovieDataBase.TAG, "getMovieList() mediatorLiveData observe")
        val getMovieData = db.movieDao().getMovie(id)
        if (owner != null && observer != null)
            getMovieData!!.observe(owner, observer)
    }

    fun getMovieList(owner: LifecycleOwner?, observer: Observer<List<Movie?>?>?) {
        Log.d(MovieDataBase.TAG, "getMovieList() mediatorLiveData observe")
        if (owner != null && observer != null)
            mediatorLiveData.observe(owner, observer)
    }

    fun searchMovie(owner: LifecycleOwner?, observer: Observer<List<Movie?>?>?, keyword: String) {
        if (db == null || TextUtils.isEmpty(keyword)) return
        Log.d(MovieDataBase.TAG, "searchMovie() searchLiveData observe keyword:$keyword")

        // Fuzzy search column and limit by live data.
        val searchLiveDataTemp = db.movieDao().searchMoveFuzzyByLimit(keyword, 3)
        searchLiveDataTemp!!.observe(owner!!, observer!!)

//        var movieList: List<Movie?>?
//
//        // Find column by in cmd.
//        val keyWords = ArrayList<String>()
//        keyWords.add(keyword)
//        movieList = db.movieDao().findMove(keyWords)
//
//        // Find column by cursor and limit.
//        movieList = parseCursor(db.movieDao().searchMoveCursorByLimit(keyword, 3))
//
//        // Find column by cursor.
//        movieList = parseCursor(db.movieDao().searchMoveCursor(keyword));
//
//        // Search column.
//        movieList = db.movieDao().searchMove(keyword)
//
//        // Fuzzy search by ‘%’.
//        movieList = db.movieDao().searchMoveFuzzyInternal("%" + keyword + "%")
//
//        // Fuzzy search by cmd.
//        movieList = db.movieDao().searchMoveFuzzy(keyword)
//
//        Log.d(MovieDataBase.TAG, "searchMovie() searchLiveData movieList:$movieList")
//        if (movieList != null) {
//            searchLiveData.postValue(movieList);
//        }
    }

    fun addDeleteMovie(owner: LifecycleOwner?, observer: Observer<List<Movie?>?>?,
                       newMovie: Movie, oldMovie: Movie) {
        if (db == null) return
        Log.d(MovieDataBase.TAG, "addDeleteMovie() newMovie:" + newMovie
                + " oldMovie:" + oldMovie)

        // By transaction annotation.
        db.movieDao().insetNewAndDeleteOld(newMovie, oldMovie)

//        // By transaction api.
//        db.runInTransaction(Runnable {
//            val database = db.getOpenHelper().getWritableDatabase();
//
//            val contentValues = ContentValues()
//            contentValues.put("movie_name", newMovie.getName())
//            contentValues.put("actor_name", newMovie.getActor())
//            contentValues.put("post_year", newMovie.getYear())
//            contentValues.put("review_score", newMovie.getScore())
//
//            Log.d(MovieDataBase.TAG, "addDeleteMovie() in transaction insert & delete")
//            database.insert("movie", SQLiteDatabase.CONFLICT_ABORT, contentValues)
//            database.delete("movie", "id = " + oldMovie.getId(), null)
//        })
    }

    fun filterMovieByScore(owner: LifecycleOwner?, observer: Observer<List<Movie?>?>?, minScore: Double) {
        if (db == null) return
        Log.d(MovieDataBase.TAG, "filterMovieByScore() observe minScore:$minScore")

        val sortLiveData = db.movieDao().getMoveOverScore(minScore)
        sortLiveData!!.observe(owner!!, observer!!)
    }

    private fun parseCursor(cursor: Cursor?): List<Movie>? {
        Log.d(MovieDataBase.TAG, "parseCursor() cursor:$cursor")
        if (cursor == null || !cursor.moveToFirst()) return null

        val movies: MutableList<Movie> = ArrayList()
        while (cursor.moveToNext()) {
            val movie = Movie(cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4).toDouble())

            movie.setId(cursor.getInt(0))
            movies.add(movie)
            Log.d(MovieDataBase.TAG, "parseCursor() movie:$movie")
        }
        cursor.close()
        return movies
    }

    init {
        mContext = application
        Log.d(MovieDataBase.TAG, "MovieViewModel() application:$application")

        db = MovieDataBase.getInstance(mContext)
        Log.d(MovieDataBase.TAG, "MovieViewModel() addSource")

        if (db != null) {
            // Get by order
            // mediatorLiveData.addSource(db.movieDao().allMovies) { movieList ->
            // Get part columns
            // mediatorLiveData.addSource(db.movieDao().partMovies) { movieList ->
            mediatorLiveData.addSource(db.movieDao().allMoviesByOrder) { movieList ->
                if (db.databaseCreated.value != null) {
                    Log.d(MovieDataBase.TAG, "MovieViewModel() mediatorLiveData movieList:$movieList")
                    mediatorLiveData.postValue(movieList)
                }
            }
        };
    }
}