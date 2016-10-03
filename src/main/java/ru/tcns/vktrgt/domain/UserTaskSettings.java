package ru.tcns.vktrgt.domain;

import ru.tcns.vktrgt.service.external.google.impl.GoogleDriveImpl;

/**
 * Created by TIMUR on 03.05.2016.
 */
public class UserTaskSettings implements Cloneable {
    private User user;

    private GoogleDriveImpl googleDrive;

    public UserTask getUserTask() {
        return userTask;
    }

    public void setUserTask(UserTask userTask) {
        this.userTask = userTask;
    }

    private UserTask userTask;

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
        this.setCreateTask(createTask);
        this.user = user;
        this.taskDescription = taskDescription;
        this.googleDrive = googleDrive;
    }
    public UserTaskSettings(UserTaskSettings settings, Boolean createTask) {
        this.setCreateTask(createTask);
        this.user = settings.getUser();
        this.taskDescription = settings.getTaskDescription();
        this.googleDrive = settings.getGoogleDriveService();
    }
    public UserTaskSettings(UserTask task) {
        this.setCreateTask(false);
        this.userTask = task;
        this.user = task.getSettings().getUser();
        this.taskDescription = task.getSettings().getTaskDescription();
        this.googleDrive = task.getSettings().getGoogleDriveService();
    }

    @Override
    public UserTaskSettings clone() throws CloneNotSupportedException {
        UserTaskSettings settings = new UserTaskSettings(user, isCreateTask(), taskDescription, googleDrive);
        return settings;
    }

    public void setCreateTask(boolean createTask) {
        this.createTask = createTask;
    }
}
