package com.ellison.jetpackdemo.databinding;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

public class DemoActivity extends AppCompatActivity {
    ActivityDataBindingBinding binding;
    private List<Person> mPersons = Arrays.asList(new Person[] {
            new Person("18", "Audi"),
            new Person("7", "Benz"),
            new Person("24", "Cadillac"),
            new Person("1", "DS")});

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
        binding = ActivityDataBindingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setResponder(new EventResponder());
    }

    // Event binding.
    public class EventResponder {
        public void onClick(View view) {
            Log.d("dataBinding", "onClick() view:" + view);
            if (view == binding.testGet) {
                Log.d("dataBinding", "onClick() GET INFO");
                getInfo();
            } else if (view == binding.testUpdate) {
                Log.d("dataBinding", "onClick() UPDATE INFO");
                updateInfo();
            }
        }

        public void onClickUpdate() {
            Log.d("dataBinding", "onClickUpdate() UPDATE INFO");
            updateInfo();
        }
    }

    private void getInfo() {
        binding.testPersonList.setLayoutManager(new LinearLayoutManager(this));
        binding.testPersonList.setAdapter(new TwoWayBindingAdapter(mPersons));

        binding.testPersonListOneWay.setLayoutManager(new LinearLayoutManager(this));
        binding.testPersonListOneWay.setAdapter(new OneWayBindingAdapter(mPersons));
    }

    private void updateInfo() {
        mPersons.get(0).setAge("1");
        mPersons.get(3).setAge("100");
    }
}