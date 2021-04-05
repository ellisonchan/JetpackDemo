package com.ellison.jetpackdemo.viewModel;

import android.os.Bundle;
import android.util.Log;

import com.ellison.jetpackdemo.R;
import com.ellison.jetpackdemo.databinding.ActivityViewModelBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.ViewModelProvider;

public class DemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityViewModelBinding binding = ActivityViewModelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("ViewModel", "onCreate() GET VIEW MODEL INSTANCE WITH:" + ((HasDefaultViewModelProviderFactory) this).getDefaultViewModelProviderFactory());
        // final PersonModel model = new ViewModelProvider(this).get(PersonModel.class);
        // final PersonModel model = ViewModelProviders.of(this).get(PersonModel.class);
        final PersonContextModel model = new ViewModelProvider(this).get(PersonContextModel.class);
        // final PersonContextModel model = new ViewModelProvider(this).get(PersonContextStateModel.class);

        Log.d("ViewModel", "onCreate() OBSERVE START");
        model.mPersonLiveData.observe(this, person -> {
            Log.d("ViewModel", "onCreate() OBSERVED DATA:" + person);
                binding.testName.setText(person.getName());
                binding.testAge.setText(String.valueOf(person.getAge()));
            }
        );

        binding.testGet.setOnClickListener(view -> model.getPersonInWork());
        binding.testUpdate.setOnClickListener(view -> model.updatePersonInWork());
        binding.testFragment.setOnClickListener(view -> getSupportFragmentManager().beginTransaction().replace(R.id.test_container, new DemoFragment()).commit());
        binding.testFragment2.setOnClickListener(view -> getSupportFragmentManager().beginTransaction().replace(R.id.test_container2, new OtherFragment()).commit());
    }
}
