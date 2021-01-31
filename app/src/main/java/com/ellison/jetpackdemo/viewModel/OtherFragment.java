    package com.ellison.jetpackdemo.viewModel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ellison.jetpackdemo.databinding.FragmentOtherViewModelBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class OtherFragment extends Fragment {
    private FragmentOtherViewModelBinding fragmentOtherViewModelBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("ViewModel", "onCreateView()");
        fragmentOtherViewModelBinding = FragmentOtherViewModelBinding.inflate(inflater, container, false);
        return fragmentOtherViewModelBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ViewModelProvider(getActivity()).get(PersonModel.class).mPersonLiveData.observe(this, person -> {
            // new ViewModelProvider(getActivity()).get(PersonStateModel.class).mPersonLiveData.observe(this, person -> {
            Log.d("ViewModel", "OtherFragment#onViewCreated() update");
            fragmentOtherViewModelBinding.name.setText(person.getName());
            fragmentOtherViewModelBinding.age.setText(String.valueOf(person.getAge()));
        });

//        new ViewModelProvider(this).get(PersonModel.class).mPersonLiveData.observe(this, person -> {
//            Log.d("ViewModel", "OtherFragment#onViewCreated() update person:" + person);
//        });
    }
}
