package com.ellison.jetpackdemo.viewModelBinding;

import android.app.Application;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class PersonsContextModel extends AndroidViewModel {
    public final MutableLiveData<List<Person>> personsLiveData;

    public PersonsContextModel(@NonNull Application application) {
        super(application);
        Log.d("ViewModelBinding", getClass().getSimpleName() + "#PersonContextModel() WITH:" + getApplication());
        personsLiveData = new MutableLiveData<>();
    }

    public void getPersonInWork(LifecycleOwner owner, Observer<List<Person>> observer) {
        Log.d("ViewModelBinding", getClass().getSimpleName() + "#getPersonInWork() WITH:" + getApplication());
        personsLiveData.observe(owner, observer);

        Executors.newSingleThreadExecutor().execute(() -> {
            Log.d("ViewModelBinding", getClass().getSimpleName() + "#getPersonInWork() GOT ASYNC");
            List<Person> persons = Arrays.asList(new Person[] {
                    new Person("18", "Audi"),
                    new Person("7", "Benz"),
                    new Person("24", "Cadillac"),
                    new Person("1", "DS"),
                    new Person("34", "Ever"),
                    new Person("199", "Ferrai")});
            personsLiveData.postValue(persons);
        });
    }

    public void updatePersonsInWork(LifecycleOwner owner, Observer<List<Person>> observer) {
        Log.d("ViewModelBinding", getClass().getSimpleName() + "#updatePersonInWork() WITH:" + getApplication());
        List<Person> persons = personsLiveData.getValue();
        personsLiveData.observe(owner, observer);

        Executors.newSingleThreadExecutor().execute(() -> {
            Log.d("ViewModelBinding", getClass().getSimpleName() + "#updatePersonInWork() UPDATE ASYNC");
            if (persons != null) {
                persons.get(0).setAge("5");
                persons.get(1).setAge("35");
                persons.get(2).setAge("15");
                persons.get(3).setAge("20");
                persons.get(4).setAge("45");
                persons.get(5).setAge("7");
            }
        });
    }

    @Override
    protected void onCleared() {
        Log.d("ViewModelBinding", getClass().getSimpleName() + "#onCleared() WITH:" + getApplication(), new Throwable());
        super.onCleared();
    }
}