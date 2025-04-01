package taskmanager.data;

import taskmanager.models.Status;
import taskmanager.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRepositoryDouble implements  TaskRepository {

    @Override
    public List<Task> findAll() throws DataAcessException {
        List<Task> all = new ArrayList<>();
        all.add(new Task(1, "2024-08-01","Bake cake","Bake a cake for birthday","2024-08-22", Status.TODO));
        all.add(new Task(2, "2024-06-02","Send Invitations","Send invitaions for party","2024-08-23", Status.IN_PROGRESS));
        all.add(new Task(3, "2024-04-05","Send present","Send present to person","2024-05-22", Status.COMPLETED));
        return all;
    }

    @Override
    public Task findById(int taskId) throws DataAcessException {
        for(Task task : findAll())
        {
            if(task.getId() == taskId)
            {
                return  task;
            }
        }
        return null;
    }

    @Override
    public Task create(Task task) throws DataAcessException {
        task.setId(99);
        return task;
    }

    @Override
    public boolean update(Task task) throws DataAcessException {
        return task.getId() > 0;
    }

    @Override
    public boolean delete(int taskId) throws DataAcessException {
        return taskId != 99;
    }
}
