package com.test.todo.todo.dependencyinject;

import com.test.todo.todo.addedittask.AddEditTaskActivity;
import com.test.todo.todo.addedittask.AddEditTaskModule;
import com.test.todo.todo.task.TasksActivity;
import com.test.todo.todo.task.TasksModule;
import com.test.todo.todo.taskdetail.TaskDetailActivity;
import com.test.todo.todo.taskdetail.TaskDetailPresenterModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = TasksModule.class)
    abstract TasksActivity tasksActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = AddEditTaskModule.class)
    abstract AddEditTaskActivity addEditTaskActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = TaskDetailPresenterModule.class)
    abstract TaskDetailActivity taskDetailActivity();
}
