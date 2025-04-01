package taskmanager.domain;

import taskmanager.models.Task;

import java.util.ArrayList;

public class TaskResult {
    public ArrayList<String> getMessages() {
        return messages;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    private final ArrayList<String> messages = new ArrayList<>();
    private Task task;

    public void addMessage(String message)
    {
        messages.add(message);
    }

    public boolean isSuccess()
    {
        return messages.size() == 0;
    }
}
