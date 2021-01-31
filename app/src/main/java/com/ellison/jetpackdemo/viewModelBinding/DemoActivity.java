package com.ellison.jetpackdemo.viewModelBinding;

import android.os.Bundle;
import android.util.Log;

import com.ellison.jetpackdemo.databinding.ActivityViewModelBindingBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class DemoActivity extends AppCompatActivity {
    PersonsContextModel model;
    ActivityViewModelBindingBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewModelBindingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = new ViewModelProvider(this).get(PersonsContextModel.class);

        binding.setResponder(new EventResponder());
        binding.setLifecycleOwner(this);
    }

    // Event binding.
    public class EventResponder {
        public void onClickGet() {
            Log.d("ViewModelBinding", "onClickGet() TITLE & GET INFO");
            model.getPersonInWork(DemoActivity.this, persons -> {
                Log.d("ViewModelBinding", "observe GOT & NOTIFY");
                binding.setDataList(persons);
            });
        }

        public void onClickUpdate() {
            Log.d("ViewModelBinding", "onClickUpdate() TITLE & GET INFO");
            model.updatePersonsInWork(DemoActivity.this, persons -> Log.d("ViewModelBinding", "observe UPDATED & NO NEED NOTIFY DUE TO DATA BINDING"));
        }
    }
}