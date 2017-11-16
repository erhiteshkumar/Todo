package com.test.todo.todo.utils;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Created by hitesh on 16/11/17.
 */

public class SingleExecutors extends AppExecutors {
    private static Executor instant = new Executor() {
        @Override
        public void execute(@NonNull Runnable command) {
            command.run();
        }
    };

    public SingleExecutors() {
        super(instant, instant, instant);
    }
}

