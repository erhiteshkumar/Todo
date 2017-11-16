package com.test.todo.todo.data.source;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.test.todo.todo.data.FakeTasksRemoteDataSource;
import com.test.todo.todo.data.source.local.TasksDao;
import com.test.todo.todo.data.source.local.TasksLocalDataSource;
import com.test.todo.todo.data.source.local.ToDoDatabase;
import com.test.todo.todo.utils.AppExecutors;
import com.test.todo.todo.utils.DiskIOThreadExecutor;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * This is used by Dagger to inject the required arguments into the {@link TasksRepository}.
 */
@Module
abstract public class TasksRepositoryModule {

    private static final int THREAD_COUNT = 3;

    @Singleton
    @Binds
    @Local
    abstract TasksDataSource provideTasksLocalDataSource(TasksLocalDataSource dataSource);

    @Singleton
    @Binds
    @Remote
    abstract TasksDataSource provideTasksRemoteDataSource(FakeTasksRemoteDataSource dataSource);

    @Singleton
    @Provides
    static ToDoDatabase provideDb(Application context) {
        return Room.databaseBuilder(context.getApplicationContext(), ToDoDatabase.class, "Tasks.db")
                .build();
    }

    @Singleton
    @Provides
    static TasksDao provideTasksDao(ToDoDatabase db) {
        return db.taskDao();
    }

    @Singleton
    @Provides
    static AppExecutors provideAppExecutors() {
        return new AppExecutors(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
    }
}
