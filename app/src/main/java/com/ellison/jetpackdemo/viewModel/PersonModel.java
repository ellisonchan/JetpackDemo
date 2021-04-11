package com.ellison.jetpackdemo.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PersonModel extends ViewModel {
    public final MutableLiveData<Person> mPersonLiveData = new MutableLiveData<>();

    public PersonModel() {
        Log.d("ViewModel", getClass().getSimpleName() + "#PersonModel()", new Throwable());
    }

    public PersonModel(String v) {
        Log.d("ViewModel", getClass().getSimpleName() + "#PersonModel()", new Throwable());
    }

    public void getPersonInWork() {
        Log.d("ViewModel", getClass().getSimpleName() + "#getPersonInWork()");
        Person testPerson = new Person(30, "ELC1020");
        // Post set value task.
        mPersonLiveData.postValue(testPerson);
    }

    public void updatePersonInWork() {
        Log.d("ViewModel", getClass().getSimpleName() + "#updatePersonInWork()");
        Person person = mPersonLiveData.getValue();
        if (person != null) {
            // Set value directly.
            person.setName("Ellison");
            person.setAge(20);
            mPersonLiveData.setValue(person);

            // // Can set value directly only at main thread, else exception occurred.
            // Executors.newSingleThreadExecutor().execute(() -> {
            //    mPersonLiveData.setValue(person);
            // });
        }
    }

    @Override
    protected void onCleared() {
        Log.d("ViewModel", getClass().getSimpleName() + "#onCleared()", new Throwable());
        super.onCleared();
    }
}
