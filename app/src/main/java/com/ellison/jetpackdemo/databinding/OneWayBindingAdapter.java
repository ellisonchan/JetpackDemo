package com.ellison.jetpackdemo.databinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OneWayBindingAdapter extends TwoWayBindingAdapter {
    public OneWayBindingAdapter(List<Person> personList) {
        super(personList);
        Log.d("dataBinding", "OneWayBindingAdapter#OneWayBindingAdapter() personList:" + personList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("dataBinding", "OneWayBindingAdapter#onCreateViewHolder() parent:" + parent);
        return new MyHolder(ActivityDataBindingItemOneWayBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("dataBinding", "OneWayBindingAdapter#onBindViewHolder() holder:" + holder + " position:" + position);
        ((ActivityDataBindingItemOneWayBinding) ((MyHolder) holder).binding).setPerson(personList.get(position));
        ((ActivityDataBindingItemOneWayBinding) ((MyHolder) holder).binding).setCheckResponder(new CheckResponder() {
            @Override
            public void onCheckChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("dataBinding", "OneWayBindingAdapter#onCheckChanged() buttonView:" + buttonView + " isChecked:" + isChecked);
                if (buttonView == ((ActivityDataBindingItemOneWayBinding) ((MyHolder) holder).binding).testCheck) {
                    Log.d("dataBinding", "OneWayBindingAdapter#onCheckChanged() BTN & CHANGE COLOR");
                    personList.get(position).setChecked(isChecked);
                }
            }
        });
    }

    // Event binding.
    public static class CheckResponder {
        public void onCheckChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d("dataBinding", "CheckResponder#onCheckChanged() buttonView:" + buttonView + " isChecked:" + isChecked);
        }
    }
}