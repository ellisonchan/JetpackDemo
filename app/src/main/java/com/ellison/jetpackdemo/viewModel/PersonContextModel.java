package com.ellison.jetpackdemo.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class PersonContextModel extends AndroidViewModel {
    public final MutableLiveData<Person> mPersonLiveData = new MutableLiveData<>();

    public PersonContextModel(@NonNull Application application) {
        super(application);
        Log.d("ViewModel", getClass().getSimpleName() + "#PersonContextModel() WITH:" + getApplication(), new Throwable());
    }

    public void getPersonInWork() {
        Log.d("ViewModel", getClass().getSimpleName() + "#getPersonInWork() WITH:" + getApplication());
        Person testPerson = new Person(30, "ELC1020");
        // Post set value task.
        mPersonLiveData.postValue(testPerson);
    }

    public void updatePersonInWork() {
        Log.d("ViewModel", getClass().getSimpleName() + "#updatePersonInWork() WITH:" + getApplication());
        Person person = mPersonLiveData.getValue();
        if (person != null) {
            // Set value directly.
            person.setName("Ellison");
            person.setAge(20);
            mPersonLiveData.setValue(person);
        }
    }

    @Override
    protected void onCleared() {
        Log.d("ViewModel", getClass().getSimpleName() + "#onCleared() WITH:" + getApplication(), new Throwable());
        super.onCleared();
    }
}