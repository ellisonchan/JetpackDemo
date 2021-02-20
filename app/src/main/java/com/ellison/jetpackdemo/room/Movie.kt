package com.ellison.jetpackdemo.room

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class Movie() : BaseObservable() {
    constructor(name: String, actor: String, year: Int, score: Double) : this() {
        this.name = name
        this.actor = actor
        this.year = year
        this.score = score
    }

    @Ignore
    constructor(name: String, actor: String, year: Int) : this(name, actor, year, 8.0) {
    }

    @JvmField
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "movie_name", defaultValue = "Harry Potter")
    lateinit var name: String

    @ColumnInfo(name = "actor_name", defaultValue = "Jack Daniel")
    lateinit var actor: String

    @ColumnInfo(name = "post_year", defaultValue = "1999")
    var year = 1999

    @ColumnInfo(name = "review_score", defaultValue = "8.0")
    var score = 8.0

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    @JvmName("getName1")
    @Bindable
    fun getName(): String {
        return name
    }

    @JvmName("setName1")
    fun setName(name: String) {
        this.name = name
        notifyPropertyChanged(BR.name1)
    }

    @JvmName("getActor1")
    @Bindable
    fun getActor(): String {
        return actor
    }

    @JvmName("setActor1")
    fun setActor(actor: String) {
        this.actor = actor
        notifyPropertyChanged(BR.actor1)
    }

    @JvmName("getYear1")
    fun getYear(): Int {
        return year
    }

    @JvmName("setYear1")
    @Bindable
    fun setYear(year: Int) {
        this.year = year
        notifyPropertyChanged(BR.year1)
    }

    @JvmName("getScore1")
    fun getScore(): Double {
        return score
    }

    @JvmName("setScore1")
    @Bindable
    fun setScore(score: Double) {
        this.score = score
        notifyPropertyChanged(BR.score1)
    }

    override fun toString(): String {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", actor='" + actor + '\'' +
                ", year='" + year + '\'' +
                ", score='" + score + '\'' +
                '}'
    }
}