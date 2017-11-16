package com.test.todo.todo.taskdetail;

import com.test.todo.todo.dependencyinject.ActivityScoped;
import com.test.todo.todo.dependencyinject.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

import static com.test.todo.todo.taskdetail.TaskDetailActivity.EXTRA_TASK_ID;

/**
 * Created by hitesh on 16/11/17.
 */

@Module
public abstract class TaskDetailPresenterModule {


    @FragmentScoped
    @ContributesAndroidInjector
    abstract TaskDetailFragment taskDetailFragment();

    @ActivityScoped
    @Binds
    abstract TaskDetailContract.Presenter statitsticsPresenter(TaskDetailPresenter presenter);

    @Provides
    @ActivityScoped
    static String provideTaskId(TaskDetailActivity activity) {
        return activity.getIntent().getStringExtra(EXTRA_TASK_ID);
    }
}
