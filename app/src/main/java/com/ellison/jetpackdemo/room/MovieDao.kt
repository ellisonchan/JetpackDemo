package com.ellison.jetpackdemo.room

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {
    @Insert
    fun insertWithOutId(movie: Movie?)

    @Insert
    fun insert(movie: Movie?): Long?

    @Insert
    fun insert(vararg movies: Movie?): LongArray?

    @Delete
    fun delete(movie: Movie?): Int

    @Delete
    fun delete(vararg movies: Movie?): Int

    @Update
    fun update(vararg movies: Movie?): Int

    @get:Query("SELECT * FROM movie")
    val allMovies: LiveData<List<Movie?>?>

    @get:Query("SELECT id, movie_name, actor_name, post_year, review_score FROM movie")
    val partMovies: LiveData<List<Movie?>?>

    @get:Query("SELECT * FROM movie ORDER BY post_year DESC")
    val allMoviesByOrder: LiveData<List<Movie?>?>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovie(id: Int): LiveData<Movie?>?

    @Query("SELECT * FROM movie WHERE post_year > :year")
    fun getMoveOverYear(year: Int): List<Movie?>?

    @Query("SELECT * FROM movie WHERE post_year BETWEEN :minYear AND :maxYear")
    fun getMoveOverYear(minYear: Int, maxYear: Int): LiveData<List<Movie?>?>?

    @Query("SELECT * FROM movie WHERE review_score >= :minScore")
    fun getMoveOverScore(minScore: Double): LiveData<List<Movie?>?>?

    @Query("SELECT * FROM movie WHERE movie_name LIKE :keyWord OR " +
            " actor_name LIKE :keyWord LIMIT 1")
    fun searchBestMove(keyWord: String?): Movie?

    @Query("SELECT * FROM movie WHERE movie_name LIKE :keyWord " +
            " OR actor_name LIKE :keyWord")
    fun searchMove(keyWord: String?): List<Movie?>?

    @Query("SELECT * FROM movie WHERE movie_name LIKE :keyWord " +
            " OR actor_name LIKE :keyWord")
    fun searchMoveFuzzyInternal(keyWord: String?): List<Movie?>?

    @Query("SELECT * FROM movie WHERE movie_name LIKE '%' || :keyWord || '%' " +
            " OR actor_name LIKE '%' || :keyWord || '%'")
    fun searchMoveFuzzy(keyWord: String?): List<Movie?>?

    @Query("SELECT * FROM movie WHERE movie_name LIKE '%' || :keyWord || '%' " +
            " OR actor_name LIKE '%' || :keyWord || '%' LIMIT :limit")
    fun searchMoveFuzzyByLimit(keyWord: String?, limit: Int): LiveData<List<Movie?>?>?

    @Query("SELECT * FROM movie WHERE movie_name IN (:keyWords)")
    fun findMove(keyWords: List<String?>?): List<Movie?>?

    @Query("SELECT * FROM movie WHERE movie_name LIKE '%' || :keyWord || '%' " +
            " OR actor_name LIKE '%' || :keyWord || '%' LIMIT :limit")
    fun searchMoveCursorByLimit(keyWord: String?, limit: Int): Cursor?

    @Query("SELECT * FROM movie WHERE movie_name LIKE '%' || :keyWord || '%' " +
            " OR actor_name LIKE '%' || :keyWord || '%'")
    fun searchMoveCursor(keyWord: String?): Cursor?

    @Transaction
    fun insetNewAndDeleteOld(newMovie: Movie?, oldMovie: Movie?) {
        insert(newMovie)
        delete(oldMovie)
    }
}