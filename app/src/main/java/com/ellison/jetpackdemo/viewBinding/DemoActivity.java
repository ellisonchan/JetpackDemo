package com.ellison.jetpackdemo.viewBinding;

import android.os.Bundle;
import android.widget.Toast;

import com.ellison.jetpackdemo.R;
import com.ellison.jetpackdemo.databinding.ActivityViewBindingBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityViewBindingBinding binding = ActivityViewBindingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.testLayout.setOnClickListener(view -> Toast.makeText(this, "U clicked empty area.", Toast.LENGTH_SHORT).show());
        binding.testBtn.setOnClickListener(view -> Toast.makeText(this, "U clicked button.", Toast.LENGTH_SHORT).show());
        getSupportFragmentManager().beginTransaction().replace(R.id.test_container, DemoFragment.newInstance()).commitNow();
    }
}
