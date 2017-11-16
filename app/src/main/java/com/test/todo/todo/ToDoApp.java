package com.test.todo.todo;

import android.support.annotation.VisibleForTesting;

import com.test.todo.todo.data.source.TasksRepository;
import com.test.todo.todo.dependencyinject.AppComponent;
import com.test.todo.todo.dependencyinject.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by hitesh on 14/11/17.
 */

public class ToDoApp extends  DaggerApplication{

    @Inject
    TasksRepository tasksRepository;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }

    @VisibleForTesting
    public TasksRepository getTasksRepository() {
        return tasksRepository;
    }
}
