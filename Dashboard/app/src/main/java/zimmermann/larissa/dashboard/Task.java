package zimmermann.larissa.dashboard;

import java.util.List;

/**
 * Created by laris on 24/10/2017.
 */

public class Task {

    private String taskName;
    private String taskStatus;
    private int taskPriority;
    private String taskSuitor;
    private String taskFinisher;
    private List<String> attachmentsList;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskSuitor() {
        return taskSuitor;
    }

    public void setTaskSuitor(String taskSuitor) {
        this.taskSuitor = taskSuitor;
    }

    public String getTaskFinisher() {
        return taskFinisher;
    }

    public void setTaskFinisher(String taskFinisher) {
        this.taskFinisher = taskFinisher;
    }

    public List<String> getAttachmentsList() {
        return attachmentsList;
    }

    public void setAttachmentsList(List<String> attachmentsList) {
        this.attachmentsList = attachmentsList;
    }
}
