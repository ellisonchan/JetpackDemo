package com.ellison.jetpackdemo.liveData;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ellison.jetpackdemo.databinding.ActivityLiveDataBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DemoActivity extends AppCompatActivity {
    static final String TAG = DemoActivity.class.getSimpleName();
    ActivityLiveDataBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "MYSELF onCreate()");
        super.onCreate(savedInstanceState);

        binding = ActivityLiveDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.setResponder(new EventResponder());
    }

    // Event binding.
    public class EventResponder {
        public void onClickUpdate() {
            Log.d(TAG, "onClickUpdate() UPDATE INFO");
            new MovieLiveData("Rush Hour").observe(DemoActivity.this, movie -> {
                Log.d(TAG, "onChanged() movie:" + movie);
                binding.testMovie.setVisibility(View.VISIBLE);
                binding.name.setText(movie.name);
                binding.type.setText(movie.type);
                binding.actor.setText(movie.actor);
                binding.post.setBackgroundResource(movie.postID);
            });
        }
    }
}