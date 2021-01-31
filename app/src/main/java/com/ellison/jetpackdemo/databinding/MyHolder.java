package com.ellison.jetpackdemo.databinding;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder {
    ViewDataBinding binding;

    public MyHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
