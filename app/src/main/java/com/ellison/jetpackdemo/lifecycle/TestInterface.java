package com.ellison.jetpackdemo.lifecycle;

public interface TestInterface {
    static final String TAG = TestInterface.class.getSimpleName();
    default void getName() {}
    int getAge();
}
