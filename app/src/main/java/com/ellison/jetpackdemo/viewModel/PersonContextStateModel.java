package com.ellison.jetpackdemo.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

public class PersonContextStateModel extends PersonContextModel {
    public final MutableLiveData<Person> mPersonLiveData = new MutableLiveData<>();

    public PersonContextStateModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        Log.d("ViewModel", getClass().getSimpleName() + "#PersonContextStateModel() WITH:" + getApplication() + " handle:" + handle, new Throwable());
    }
}