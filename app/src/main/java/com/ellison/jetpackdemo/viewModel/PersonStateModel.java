package com.ellison.jetpackdemo.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

public class PersonStateModel extends PersonModel {
    public final MutableLiveData<Person> mPersonLiveData = new MutableLiveData<>();

    public PersonStateModel(SavedStateHandle handle) {
        Log.d("ViewModel", getClass().getSimpleName() + "#PersonStateModel()" + " handle:" + handle, new Throwable());
    }
}
