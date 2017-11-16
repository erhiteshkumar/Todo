package com.test.todo.todo.taskdetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;
import com.test.todo.todo.data.Task;
import com.test.todo.todo.data.source.TasksDataSource;
import com.test.todo.todo.data.source.TasksRepository;

import javax.inject.Inject;

/**
 * Created by hitesh on 16/11/17.
 */
final class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private TasksRepository mTasksRepository;
    @Nullable
    private TaskDetailContract.View mTaskDetailView;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Nullable
    private String mTaskId;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    TaskDetailPresenter(@Nullable String taskId,
                        TasksRepository tasksRepository) {
        mTasksRepository = tasksRepository;
        mTaskId = taskId;
    }


    private void openTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            if (mTaskDetailView != null) {
                mTaskDetailView.showMissingTask();
            }
            return;
        }

        if (mTaskDetailView != null) {
            mTaskDetailView.setLoadingIndicator(true);
        }
        mTasksRepository.getTask(mTaskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                // The view may not be able to handle UI updates anymore
                if (mTaskDetailView==null||!mTaskDetailView.isActive()) {
                    return;
                }
                mTaskDetailView.setLoadingIndicator(false);
                if (null == task) {
                    mTaskDetailView.showMissingTask();
                } else {
                    showTask(task);
                }
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mTaskDetailView.isActive()) {
                    return;
                }
                mTaskDetailView.showMissingTask();
            }
        });
    }

    @Override
    public void editTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            if (mTaskDetailView != null) {
                mTaskDetailView.showMissingTask();
            }
            return;
        }
        if (mTaskDetailView != null) {
            mTaskDetailView.showEditTask(mTaskId);
        }
    }

    @Override
    public void deleteTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            if (mTaskDetailView != null) {
                mTaskDetailView.showMissingTask();
            }
            return;
        }
        mTasksRepository.deleteTask(mTaskId);
        if (mTaskDetailView != null) {
            mTaskDetailView.showTaskDeleted();
        }
    }

    @Override
    public void completeTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            if (mTaskDetailView != null) {
                mTaskDetailView.showMissingTask();
            }
            return;
        }
        mTasksRepository.completeTask(mTaskId);
        if (mTaskDetailView != null) {
            mTaskDetailView.showTaskMarkedComplete();
        }
    }

    @Override
    public void activateTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            if (mTaskDetailView != null) {
                mTaskDetailView.showMissingTask();
            }
            return;
        }
        mTasksRepository.activateTask(mTaskId);
        if (mTaskDetailView != null) {
            mTaskDetailView.showTaskMarkedActive();
        }
    }

    @Override
    public void takeView(TaskDetailContract.View taskDetailView) {
        mTaskDetailView = taskDetailView;
        openTask();
    }

    @Override
    public void dropView() {
        mTaskDetailView = null;
    }

    private void showTask(@NonNull Task task) {
        String title = task.getTitle();
        String description = task.getDescription();

        if (Strings.isNullOrEmpty(title)) {
            if (mTaskDetailView != null) {
                mTaskDetailView.hideTitle();
            }
        } else {
            if (mTaskDetailView != null) {
                mTaskDetailView.showTitle(title);
            }
        }

        if (Strings.isNullOrEmpty(description)) {
            if (mTaskDetailView != null) {
                mTaskDetailView.hideDescription();
            }
        } else {
            if (mTaskDetailView != null) {
                mTaskDetailView.showDescription(description);
            }
        }
        if (mTaskDetailView != null) {
            mTaskDetailView.showCompletionStatus(task.isCompleted());
        }
    }
}
