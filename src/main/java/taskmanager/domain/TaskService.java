package taskmanager.domain;

import taskmanager.data.DataAcessException;
import taskmanager.data.TaskRepository;
import taskmanager.models.Task;

import java.util.List;

public class TaskService {
    /*cannot add a task with an id >0,
    need to check that createdOn, title exists, title can't be greater than 50 char
    description exists and can't be greater than 20 chars, dueDate exists
    need to ensure task has a status
    if anything fails we need to let user know*/
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    //Read
    public List<Task> findAll() throws DataAcessException {
        return repository.findAll();
    }

    public Task findById(int taskId) throws DataAcessException {
        return  repository.findById(taskId);
    }

    //Create
    public TaskResult create (Task task) throws DataAcessException {
        TaskResult result = validate(task); //enforce our business rules
        if(!result.isSuccess())
        {
            return result;
        }
        if(task == null)
        {
            result.addMessage("Task cannot be null");
            return result;
        }

        if(task.getId() > 0)
        {
            result.addMessage("Cannot create existing task");
            return result;
        }

        task = repository.create(task);
        result.setTask(task);
        return result;
    }

    //Update
    public TaskResult update(Task task) throws DataAcessException {
        TaskResult result = validate(task);
        if(!result.isSuccess())
        {
            return result;
        }
        boolean updated = repository.update(task);
        if(!updated)
        {
            result.addMessage(String.format("Task with id: %s does not exist", task.getId()));
        }
        return result;
    }

    //Delete
    public TaskResult deleteById(int taskId) throws DataAcessException {
        TaskResult result = new TaskResult();
        if(!repository.delete(taskId))
        {
            result.addMessage(String.format("Task with id: %s does not exist", taskId));
        }
        return result;
    }
    //Helper methods
    public TaskResult validate(Task task)
    {
        TaskResult result = new TaskResult();
        if(task == null)
        {
            result.addMessage("Task cannot be null");
            return result;
        }
        if(task.getCreatedOn() == null || task.getCreatedOn().isBlank())
        {
            result.addMessage("Create on date is required");
            return  result;
        }
        if(task.getTitle() == null || task.getTitle().isBlank() || task.getTitle().length() > 50)
        {
            result.addMessage("Title must exist and cannot be longer than 50 characters");
            return result;
        }
        if(task.getDescription() == null || task.getDescription().isBlank() || task.getDescription().length() < 20)
        {
            result.addMessage("Description is required and must be more than 20 characters");
            return result;
        }
        if(task.getDueDate() == null || task.getDueDate().isBlank())
        {
            result.addMessage("Due date is required");
            return result;
        }
        if(task.getStatus() == null)
        {
            result.addMessage("Status is required");
            return result;
        }
        return result;
    }
}
