package com.ellison.jetpackdemo.room

import android.util.Log
import java.util.*

object Utils {// Create instances with id.
    // movies[i] = new Movie(i + 1, "Harry Potter" + i, "Daniel_" + i );

//             // Create instances with same id.
//             movies[i] = new Movie(1, "Harry Potter" + i, "Daniel_" + i );

    // Create instances without id.
    // movies[i] = new Movie("Harry Potter" + (i + 1), "Daniel_" + (i + 1), 1999 + i);
    // Test column null
    // movies[i] = new Movie("Harry Potter" + i, null);
    // movies[i] = new Movie("Harry Potter" + i);
    //    public static Movie[] getInitData() {
    //        List<Movie> movies = new ArrayList<>(15);
    //        for (int i = 0; i < 10; i++) {
    //            movies.add(new Movie(i + 1, "Harry Potter" + i, "Daniel_" + i ));
    //        }
    //        return (Movie[]) movies.toArray();
    //    }
    val initData: Array<Movie?>
        get() {
            val movies = arrayOfNulls<Movie>(9)
            for (i in movies.indices) {
                // Create instances with id.
                // movies[i] = new Movie(i + 1, "Harry Potter" + i, "Daniel_" + i );

//             // Create instances with same id.
//             movies[i] = new Movie(1, "Harry Potter" + i, "Daniel_" + i );

                // Create instances without id.
                // movies[i] = new Movie("Harry Potter" + (i + 1), "Daniel_" + (i + 1), 1999 + i);
                movies[i] = Movie("Harry Potter" + (i + 1),
                        "Daniel_" + (i + 1),
                        1999 + i,
                        (1 + i).toDouble())
                // Test column null
                // movies[i] = new Movie("Harry Potter" + i, null);
                // movies[i] = new Movie("Harry Potter" + i);
            }
            Log.d(MovieDataBase.Companion.TAG, "getInitData movies:" + Arrays.toString(movies))
            return movies
        }
}