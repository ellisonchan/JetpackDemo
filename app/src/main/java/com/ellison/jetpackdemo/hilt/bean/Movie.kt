package com.ellison.jetpackdemo.hilt.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Movie(@ColumnInfo(name = "movie_name", defaultValue = "Harry Potter")
            private var Title: String,
            @ColumnInfo(name = "movie_year", defaultValue = "1991")
            private var Year: String,
            @ColumnInfo(name = "movie_id", defaultValue = "imdb324523")
            var imdbID: String,
            @ColumnInfo(name = "movie_type", defaultValue = "Movie")
            var Type: String,
            @ColumnInfo(name = "movie_poster", defaultValue = "https://ddd/dad.img")
            var Poster: String
) : BaseObservable() {
    override fun toString(): String {
        return "Movie(Title='$Title', Year='$Year', imdbID='$imdbID', Type='$Type')"
    }

    @JvmField
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @Bindable
    fun getTitle(): String {
        return Title
    }

    fun setTitle(title: String) {
        Title = title
        notifyPropertyChanged(BR.title)
    }

    @Bindable
    fun getYear(): String {
        return Year
    }

    fun setYear(year: String) {
        Year = year
        notifyPropertyChanged(BR.year)
    }
}