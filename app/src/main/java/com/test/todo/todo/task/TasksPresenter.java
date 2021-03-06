package com.test.todo.todo.task;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.test.todo.todo.data.Task;
import com.test.todo.todo.data.source.TasksRepository;
import com.test.todo.todo.data.source.TasksDataSource;
import com.test.todo.todo.dependencyinject.ActivityScoped;
import com.test.todo.todo.utils.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hitesh on 16/11/17.
 */
@ActivityScoped
final class TasksPresenter implements TasksContract.Presenter {

    private final TasksRepository mTasksRepository;
    @Nullable
    private TasksContract.View mTasksView;

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    TasksPresenter(TasksRepository tasksRepository) {
        mTasksRepository = tasksRepository;
    }


    @Override
    public void result(int requestCode, int resultCode) {
//         If a task was successfully added, show snackbar
//        if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode
//                && Activity.RESULT_OK == resultCode) {
//            if (mTasksView != null) {
//                mTasksView.showSuccessfullySavedMessage();
//            }
//        }
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link TasksDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            if (mTasksView != null) {
                mTasksView.setLoadingIndicator(true);
            }
        }
        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<>();

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }

                // We filter the tasks based on the requestType
                for (Task task : tasks) {
                    switch (mCurrentFiltering) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            tasksToShow.add(task);
                            break;
                    }
                }
                // The view may not be able to handle UI updates anymore
                if (mTasksView == null || !mTasksView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mTasksView.setLoadingIndicator(false);
                }

                processTasks(tasksToShow);
            }

            @Override
            public void onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!mTasksView.isActive()) {
                    return;
                }
                mTasksView.showLoadingTasksError();
            }
        });
    }

    private void processTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            processEmptyTasks();
        } else {
            // Show the list of tasks
            if (mTasksView != null) {
                mTasksView.showTasks(tasks);
            }
            // Set the filter label's text.
            showFilterLabel();
        }
    }

    private void showFilterLabel() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                if (mTasksView != null) {
                    mTasksView.showActiveFilterLabel();
                }
                break;
            case COMPLETED_TASKS:
                if (mTasksView != null) {
                    mTasksView.showCompletedFilterLabel();
                }
                break;
            default:
                if (mTasksView != null) {
                    mTasksView.showAllFilterLabel();
                }
                break;
        }
    }

    private void processEmptyTasks() {
        if (mTasksView == null) return;
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mTasksView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                mTasksView.showNoCompletedTasks();
                break;
            default:
                mTasksView.showNoTasks();
                break;
        }
    }

    @Override
    public void addNewTask() {
        if (mTasksView != null) {
            mTasksView.showAddTask();
        }
    }

    @Override
    public void openTaskDetails(@NonNull Task requestedTask) {
        checkNotNull(requestedTask, "requestedTask cannot be null!");
        if (mTasksView != null) {
            mTasksView.showTaskDetailsUi(requestedTask.getId());
        }
    }

    @Override
    public void completeTask(@NonNull Task completedTask) {
        checkNotNull(completedTask, "completedTask cannot be null!");
        mTasksRepository.completeTask(completedTask);
        if (mTasksView != null) {
            mTasksView.showTaskMarkedComplete();
        }
        loadTasks(false, false);
    }

    @Override
    public void activateTask(@NonNull Task activeTask) {
        checkNotNull(activeTask, "activeTask cannot be null!");
        mTasksRepository.activateTask(activeTask);
        if (mTasksView != null) {
            mTasksView.showTaskMarkedActive();
        }
        loadTasks(false, false);
    }

    @Override
    public void clearCompletedTasks() {
        mTasksRepository.clearCompletedTasks();
        if (mTasksView != null) {
            mTasksView.showCompletedTasksCleared();
        }
        loadTasks(false, false);
    }

    @Override
    public TasksFilterType getFiltering() {
        return mCurrentFiltering;
    }

    @Override
    public void setFiltering(TasksFilterType requestType) {
        mCurrentFiltering = requestType;
    }

    @Override
    public void takeView(TasksContract.View view) {
        this.mTasksView = view;
        loadTasks(false);
    }

    @Override
    public void dropView() {
        mTasksView = null;
    }
}