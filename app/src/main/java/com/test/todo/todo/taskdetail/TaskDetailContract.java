package com.test.todo.todo.taskdetail;

import com.test.todo.todo.BasePresenter;
import com.test.todo.todo.BaseView;

/**
 * Created by hitesh on 16/11/17.
 */

public interface TaskDetailContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMissingTask();

        void hideTitle();

        void showTitle(String title);

        void hideDescription();

        void showDescription(String description);

        void showCompletionStatus(boolean complete);

        void showEditTask(String taskId);

        void showTaskDeleted();

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        boolean isActive();
    }

    interface Presenter extends BasePresenter<View> {

        void editTask();

        void deleteTask();

        void completeTask();

        void activateTask();

        void takeView(TaskDetailContract.View taskDetailFragment);

        void dropView();
    }
}

