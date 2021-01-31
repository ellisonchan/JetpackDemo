package com.ellison.jetpackdemo.databinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TwoWayBindingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<Person> personList;

    public TwoWayBindingAdapter(List<Person> personList) {
        this.personList = personList;
        Log.d("dataBinding", "TwoWayBindingAdapter#TwoWayBindingAdapter() personList:" + personList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("dataBinding", "TwoWayBindingAdapter#onCreateViewHolder() parent:" + parent);
        return new MyHolder(ActivityDataBindingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("dataBinding", "TwoWayBindingAdapter#onBindViewHolder() holder:" + holder + " position:" + position);
        ((ActivityDataBindingItemBinding) ((MyHolder) holder).binding).setPerson(personList.get(position));
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }
}