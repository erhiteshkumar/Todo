package com.test.todo.todo.taskdetail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.test.todo.todo.R;
import com.test.todo.todo.utils.ActivityUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by hitesh on 16/11/17.
 */

public class TaskDetailActivity extends DaggerAppCompatActivity {
    public static final String EXTRA_TASK_ID = "TASK_ID";
    @Inject
    TaskDetailFragment injectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.taskdetail_act);

        // Set up the toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (taskDetailFragment == null) {
            taskDetailFragment = injectedFragment;
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    taskDetailFragment, R.id.contentFrame);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
