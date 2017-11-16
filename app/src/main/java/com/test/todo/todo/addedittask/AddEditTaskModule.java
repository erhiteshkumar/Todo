package com.test.todo.todo.addedittask;

import android.support.annotation.Nullable;

import com.test.todo.todo.dependencyinject.ActivityScoped;
import com.test.todo.todo.dependencyinject.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by hitesh on 16/11/17.
 */

@Module
public abstract class AddEditTaskModule {

    // Rather than having the activity deal with getting the intent extra and passing it to the presenter
    // we will provide the taskId directly into the AddEditTaskActivitySubcomponent
    // which is what gets generated for us by Dagger.Android.
    // We can then inject our TaskId and state into our Presenter without having pass through dependency from
    // the Activity. Each UI object gets the dependency it needs and nothing else.
    @Provides
    @ActivityScoped
    @Nullable
    static String provideTaskId(AddEditTaskActivity activity) {
        return activity.getIntent().getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID);
    }

    @Provides
    @ActivityScoped
    static boolean provideStatusDataMissing(AddEditTaskActivity activity) {
        return activity.isDataMissing();
    }

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AddEditTaskFragment addEditTaskFragment();

    @ActivityScoped
    @Binds
    abstract AddEditTaskContract.Presenter taskPresenter(AddEditTaskPresenter presenter);

    //NOTE:  IF you want to have something be only in the Fragment scope but not activity mark a
    //@provides or @Binds method as @FragmentScoped.  Use case is when there are multiple fragments
    //in an activity but you do not want them to share all the same objects.
}
