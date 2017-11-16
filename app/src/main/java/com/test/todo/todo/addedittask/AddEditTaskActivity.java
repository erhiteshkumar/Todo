package com.test.todo.todo.addedittask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.test.todo.todo.R;
import com.test.todo.todo.utils.ActivityUtils;

import javax.inject.Inject;

import dagger.android.DaggerActivity;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by hitesh on 16/11/17.
 */

public class AddEditTaskActivity extends DaggerAppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

    @Inject
    AddEditTaskContract.Presenter mAddEditTasksPresenter;

    @Inject
    AddEditTaskFragment mFragment;

    @Inject
    @Nullable
    String mTaskId;

    private ActionBar mActionBar;

    // In a rotation it's important to know if we want to let the framework restore view state or
    // need to load data from the repository. This is saved into the state bundle.
    private boolean mIsDataMissing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtask_act);

        // Set up the toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        setToolbarTitle(mTaskId);

        AddEditTaskFragment addEditTaskFragment =
                (AddEditTaskFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (addEditTaskFragment == null) {
            addEditTaskFragment = mFragment;

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditTaskFragment, R.id.contentFrame);
        }
        restoreState(savedInstanceState);
    }

    private void restoreState(Bundle savedInstanceState) {
        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            mIsDataMissing = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }
    }

    private void setToolbarTitle(@Nullable String taskId) {
        if(taskId == null) {
            mActionBar.setTitle(R.string.add_task);
        } else {
            mActionBar.setTitle(R.string.edit_task);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state so that next time we know if we need to refresh data.
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, mAddEditTasksPresenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    boolean isDataMissing() {
        return mIsDataMissing;
    }
}

