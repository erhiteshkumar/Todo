package com.test.todo.todo.task;

import android.support.annotation.NonNull;

import com.test.todo.todo.BasePresenter;
import com.test.todo.todo.BaseView;
import com.test.todo.todo.data.Task;

import java.util.List;

/**
 * Created by hitesh on 16/11/17.
 */

public interface TasksContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTasks(List<Task> tasks);

        void showAddTask();

        void showTaskDetailsUi(String taskId);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showCompletedTasksCleared();

        void showLoadingTasksError();

        void showNoTasks();

        void showActiveFilterLabel();

        void showCompletedFilterLabel();

        void showAllFilterLabel();

        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showSuccessfullySavedMessage();

        boolean isActive();

        void showFilteringPopUpMenu();
    }

    interface Presenter extends BasePresenter<View> {

        void result(int requestCode, int resultCode);

        void loadTasks(boolean forceUpdate);

        void addNewTask();

        void openTaskDetails(@NonNull Task requestedTask);

        void completeTask(@NonNull Task completedTask);

        void activateTask(@NonNull Task activeTask);

        void clearCompletedTasks();

        TasksFilterType getFiltering();

        void setFiltering(TasksFilterType requestType);

        void takeView(TasksContract.View view);

        void dropView();
    }
}
