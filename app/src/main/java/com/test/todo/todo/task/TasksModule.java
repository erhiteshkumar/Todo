package com.test.todo.todo.task;

import com.test.todo.todo.dependencyinject.ActivityScoped;
import com.test.todo.todo.dependencyinject.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by hitesh on 16/11/17.
 */

@Module
public abstract class TasksModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract TasksFragment tasksFragment();

    @ActivityScoped
    @Binds
    abstract TasksContract.Presenter taskPresenter(TasksPresenter presenter);
}
