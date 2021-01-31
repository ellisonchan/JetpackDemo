package com.ellison.jetpackdemo.viewBinding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ellison.jetpackdemo.databinding.FragmentViewBindingBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DemoFragment extends Fragment {
    FragmentViewBindingBinding mBinding;

    public static DemoFragment newInstance() {
        return new DemoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentViewBindingBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBinding.getRoot().setOnClickListener(view1 -> Toast.makeText(getContext(), "U clicked else empty area.", Toast.LENGTH_SHORT).show());
        if (mBinding.testIv != null) {
            mBinding.testIv.setOnClickListener(view1 -> Toast.makeText(getContext(), "U clicked image.", Toast.LENGTH_SHORT).show());
        }
        if (mBinding.testIvLand != null) {
            mBinding.testIvLand.setOnClickListener(view1 -> Toast.makeText(getContext(), "U clicked landscape image.", Toast.LENGTH_SHORT).show());
        }
    }
}
