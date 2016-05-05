package ru.tcns.vktrgt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.tcns.vktrgt.domain.util.parser.JsonParser;
import ru.tcns.vktrgt.repository.UserTaskRepository;

/**
 * Created by TIMUR on 02.05.2016.
 */
@Document(collection = "user_task")
public class UserTask {
    @Id
    private String id;
    @Indexed
    private String userId;
    private String kind;
    private String payload;
    private String taskInfo;
    private Integer currentStatus;

    public String getCurrentStatusDesc() {
        return currentStatusDesc;
    }

    public void setCurrentStatusDesc(String currentStatusDesc) {
        this.currentStatusDesc = currentStatusDesc;
    }

    private String currentStatusDesc;
    private Integer currentProgress;
    private Integer maxProgress;

    @Transient
    @JsonIgnore
    private UserTaskSettings settings;
    @Transient
    @JsonIgnore
    private UserTaskRepository repository;

    public UserTask() {

    }
    public UserTask(String kind, UserTaskSettings settings, UserTaskRepository repository) {
        setUserId(settings.getUser().getId());
        this.kind = kind;
        this.taskInfo = settings.getTaskDescription();
        this.currentProgress = 0;
        this.settings = settings;
        this.repository = repository;
    }
    @Transient
    public UserTask updateStatusMessage(String status) {
        if (settings.isCreateTask()) {
            setCurrentStatusDesc(status);
            return repository.save(this);
        }
        return this;
    }
    @Transient
    public UserTask saveInitial(Integer count) {
        if (settings.isCreateTask()) {
            setMaxProgress(count);
            return repository.save(this);
        }
        return this;
    }
    @Transient
    public UserTask saveProgress(Integer count) {
        if (settings.isCreateTask()) {
            addProgress(count);
            return repository.save(this);
        }
        return this;
    }
    @Transient
    public UserTask saveFinal(Object payload) {
        if (settings.isCreateTask()) {
            this.setPayload(JsonParser.objectToJson(payload));
            this.setCurrentProgress(this.getMaxProgress());
            this.setCurrentStatus(1);
            this.setCurrentStatusDesc("Задача завершена успешно");
            return repository.save(this);
        }
        return this;
    }
    @Transient
    public UserTask saveError(Exception error) {
        if (settings.isCreateTask()) {
            this.setCurrentStatus(2);
            this.setCurrentStatusDesc("Задача завершена с ошибкой");
            this.setPayload(error.getMessage());
            return repository.save(this);
        }
        return this;
    }
    @Transient
    public void addProgress(Integer val) {
        currentProgress += val;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public Integer getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Integer getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(Integer currentProgress) {
        this.currentProgress = currentProgress;
    }

    public Integer getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(Integer maxProgress) {
        this.maxProgress = maxProgress;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

