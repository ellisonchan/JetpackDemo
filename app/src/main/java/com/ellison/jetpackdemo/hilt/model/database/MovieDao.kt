package com.ellison.jetpackdemo.hilt.model.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

import com.ellison.jetpackdemo.hilt.bean.Movie

@Dao
interface MovieDao {
    @Insert
    fun insert(movie: Movie?): Long?
}