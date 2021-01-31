package com.ellison.jetpackdemo.viewModelBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ellison.jetpackdemo.databinding.ActivityViewModelBindingItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PersonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<Person> personList;

    @BindingAdapter("bindData")
    public static void bindAdapter(RecyclerView recyclerView, List<Person> personList) {
        Log.d("ViewModelBinding", "PersonsAdapter#bindAdapter() recyclerView:" + recyclerView + " personList:" + personList, new Throwable());
        recyclerView.setAdapter(new PersonsAdapter(personList));
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    public PersonsAdapter(List<Person> personList) {
        Log.d("ViewModelBinding", "PersonsAdapter#PersonsAdapter()");
        this.personList = personList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ViewModelBinding", "BindingAdapter#onCreateViewHolder() parent:" + parent);
        return new RecyclerView.ViewHolder(ActivityViewModelBindingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot()) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("ViewModelBinding", "BindingAdapter#onBindViewHolder() holder:" + holder + " position:" + position);
        ActivityViewModelBindingItemBinding binding = DataBindingUtil.bind(holder.itemView);
        binding.setPerson(personList.get(position));
        // binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return personList != null ? personList.size() : 0;
    }
}