package com.test.todo.todo.addedittask;

import com.test.todo.todo.BasePresenter;
import com.test.todo.todo.BaseView;

/**
 * Created by hitesh on 16/11/17.
 */

public interface AddEditTaskContract {

    interface View extends BaseView<Presenter> {

        void showEmptyTaskError();

        void showTasksList();

        void setTitle(String title);

        void setDescription(String description);

        boolean isActive();
    }

    interface Presenter extends BasePresenter<View> {

        void saveTask(String title, String description);

        void populateTask();

        boolean isDataMissing();
    }
}

