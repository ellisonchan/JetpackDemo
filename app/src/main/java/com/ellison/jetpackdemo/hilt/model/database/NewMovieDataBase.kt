package com.ellison.jetpackdemo.hilt.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ellison.jetpackdemo.hilt.bean.Movie

@Database(entities = [Movie::class], version = 1)
abstract class NewMovieDataBase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
}