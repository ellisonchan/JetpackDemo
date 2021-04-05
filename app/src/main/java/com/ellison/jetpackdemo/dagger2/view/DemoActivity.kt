package com.ellison.jetpackdemo.dagger2.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.ellison.jetpackdemo.MyApplication
import com.ellison.jetpackdemo.dagger2.viewmodel.MovieViewModel
import com.ellison.jetpackdemo.databinding.ActivityDaggerBinding
import javax.inject.Inject

class DemoActivity : AppCompatActivity() {
    @Inject
    lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appGraph.inject(this)
        super.onCreate(savedInstanceState)

        Log.d("Dagger", "onCreate() movieViewModel:$movieViewModel")
        val binding = ActivityDaggerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.isSubmitButtonEnabled = true
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("Dagger", "onQueryTextSubmit() query:$query")
                movieViewModel.searchMovie(query, this@DemoActivity
                ) { result: String ->
                    Log.d("Dagger", "onChanged() result:$result")
                    binding.title.textSize = 14f
                    binding.title.text = result
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
}