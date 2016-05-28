package ru.tcns.vktrgt.domain;

import ru.tcns.vktrgt.service.external.google.impl.GoogleDriveImpl;

/**
 * Created by TIMUR on 03.05.2016.
 */
public class UserTaskSettings {
    private User user;

    private GoogleDriveImpl googleDrive;

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

    public GoogleDriveImpl getGoogleDriveService() {
        return googleDrive;
    }

    public UserTaskSettings(User user, boolean createTask, String taskDescription, GoogleDriveImpl googleDrive) {
        this.createTask = createTask;
        this.user = user;
        this.taskDescription = taskDescription;
        this.googleDrive = googleDrive;
    }
    public UserTaskSettings(UserTaskSettings settings, Boolean createTask) {
        this.createTask = createTask;
        this.user = settings.getUser();
        this.taskDescription = settings.getTaskDescription();
        this.googleDrive = settings.getGoogleDriveService();
    }

    @Override
    public UserTaskSettings clone() throws CloneNotSupportedException {
        UserTaskSettings settings = new UserTaskSettings(user, createTask, taskDescription, googleDrive);
        return settings;
    }
}
