package com.ellison.jetpackdemo.databinding;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Person extends BaseObservable {
    private String name;
    private String age;
    private boolean adult;
    private boolean checked;

    public Person(String age, String name) {
        this.age = age;
        this.name = name;
        adult = this.age != null && Integer.valueOf(this.age) >= 18;
    }

    @Bindable
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
        setAdult();
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public boolean isAdult() {
        return adult;
    }

    public void setAdult() {
        adult = this.age != null && Integer.valueOf(this.age) >= 18;
        notifyPropertyChanged(BR.adult);
    }

    @Bindable
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        notifyPropertyChanged(BR.checked);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", adult=" + adult +
                ", checked=" + checked +
                '}';
    }
}
