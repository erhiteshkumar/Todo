package com.test.todo.todo;

/**
 * Created by hitesh on 16/11/17.
 */

public interface BasePresenter<T> {

    void takeView(T view);

    void dropView();

}