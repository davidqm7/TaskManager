package taskmanager;

import taskmanager.data.DataAcessException;
import taskmanager.data.TaskFileRepository;
import taskmanager.data.TaskRepository;
import taskmanager.models.Task;

import java.util.List;

public class App {
    public static void main(String[] args) throws DataAcessException {
        TaskRepository repository = new TaskFileRepository("./data/tasks.csv");
        List<Task> tasks = repository.findAll();
        for(Task task : tasks)
            System.out.println(task);
    }
}
