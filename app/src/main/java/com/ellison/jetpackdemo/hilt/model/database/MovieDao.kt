package com.ellison.jetpackdemo.hilt.model.database

import androidx.room.Dao
import androidx.room.Insert

import com.ellison.jetpackdemo.hilt.bean.Movie

@Dao
interface MovieDao {
    @Insert
    fun insert(movie: Movie?): Long?
}