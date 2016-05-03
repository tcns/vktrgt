package ru.tcns.vktrgt.domain;

/**
 * Created by TIMUR on 03.05.2016.
 */
public class UserTaskSettings {
    private User user;

    public User getUser() {
        return user;
    }

    private boolean createTask;

    public String getTaskDescription() {
        return taskDescription;
    }

    private String taskDescription;

    public boolean isCreateTask() {
        return createTask;
    }

    public UserTaskSettings(User user, boolean createTask, String taskDescription) {
        this.createTask = createTask;
        this.user = user;
        this.taskDescription = taskDescription;
    }

    @Override
    public UserTaskSettings clone() throws CloneNotSupportedException {
        UserTaskSettings settings = new UserTaskSettings(user, createTask, taskDescription);
        return settings;
    }
}
