package com.ellison.jetpackdemo.room

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*
import java.util.concurrent.Executors

@Database(entities = [Movie::class], version = 1)
abstract class MovieDataBase : RoomDatabase() {
    val databaseCreated = MutableLiveData<Boolean?>()
    abstract fun movieDao(): MovieDao

    // Ensure initialized flag set after next use if DB exist.
    private fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath(DATA_BASE_NAME).exists()) {
            databaseCreated.postValue(true)
        }
    }

    companion object {
        @Volatile
        private var sInstance: MovieDataBase? = null
        private const val DATA_BASE_NAME = "jetpack_movie.db"
        val TAG = MovieDataBase::class.java.simpleName
        @JvmStatic
        fun getInstance(context: Context): MovieDataBase? {
            if (sInstance == null) {
                synchronized(MovieDataBase::class.java) {
                    if (sInstance == null) {
                        Log.d(TAG, "createInstance")
                        sInstance = createInstance(context)
                        sInstance?.updateDatabaseCreated(context)
                    }
                }
            }
            return sInstance
        }

        private fun createInstance(context: Context): MovieDataBase {
            Log.d(TAG, "createInstance context:$context")
            return Room.databaseBuilder(context.applicationContext, MovieDataBase::class.java, DATA_BASE_NAME)
                    .allowMainThreadQueries()
                    // .fallbackToDestructiveMigration()
                    .addMigrations(object : Migration(1, 2) {
                        override fun migrate(database: SupportSQLiteDatabase) {
                            Log.d(TAG, "migrate")
                            database.execSQL("ALTER TABLE movie "
                                    + " ADD COLUMN review_score INTEGER NOT NULL DEFAULT 8.0")
                        }
                    })
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // DB created.
                            Log.d(TAG, "onCreate")

                            // Init DB.
                            Executors.newFixedThreadPool(5).execute {
                                Log.d(TAG, "onCreate insert")

                                val dataBase = getInstance(context)
                                Log.d(TAG, "onCreate insert dataBase version:$db.version")

                                val ids = dataBase!!.movieDao().insert(*Utils.initData)
                                Log.d(TAG, "onCreate insert ids:" + Arrays.toString(ids))

                                // Allow change value not from main thread.
                                dataBase.databaseCreated.postValue(true)
                            }
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            // DB opened.
                            Log.d(TAG, "onOpen")
                        }

                        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                            super.onDestructiveMigration(db)
                            Log.d(TAG, "onDestructiveMigration")

                            // Init DB again after db removed.
                            Executors.newFixedThreadPool(5).execute {
                                Log.d(TAG, "onDestructiveMigration insert")
                                val dataBase = getInstance(context)
                                Log.d(TAG, "onDestructiveMigration insert dataBase version:$db.version")
                                val ids = dataBase!!.movieDao().insert(*Utils.initData)
                                Log.d(TAG, "onDestructiveMigration insert ids:" + Arrays.toString(ids))
                                dataBase.databaseCreated.postValue(true)
                            }
                        }
                    })
                    .build()
        }
    }
}