package com.ellison.jetpackdemo.viewModel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ellison.jetpackdemo.databinding.FragmentViewModelBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class DemoFragment extends Fragment {
    private FragmentViewModelBinding fragmentViewModelBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("ViewModel", "onCreateView()");
        fragmentViewModelBinding = FragmentViewModelBinding.inflate(inflater, container, false);
        return fragmentViewModelBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ViewModelProvider(getActivity()).get(PersonContextModel.class).mPersonLiveData.observe(this, person -> {
            // new ViewModelProvider(getActivity()).get(PersonContextStateModel.class).mPersonLiveData.observe(this, person -> {
            Log.d("ViewModel", "DemoFragment#onViewCreated() update");
            fragmentViewModelBinding.name.setText(person.getName());
            fragmentViewModelBinding.age.setText(String.valueOf(person.getAge()));
        });

        PersonModel model = new ViewModelProvider(getActivity()).get(PersonModel.class);
        // PersonModel model = new ViewModelProvider(getActivity()).get(PersonStateModel.class);

        fragmentViewModelBinding.testGet.setOnClickListener(view2 -> {
            Log.d("ViewModel", "DemoFragment#onViewCreated() set value");
            model.getPersonInWork();
        });

        fragmentViewModelBinding.testUpdate.setOnClickListener(view2 -> {
            Log.d("ViewModel", "DemoFragment#onViewCreated() set value");
            model.updatePersonInWork();
        });
    }
}
