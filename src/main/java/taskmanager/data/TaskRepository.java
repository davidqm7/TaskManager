package taskmanager.data;

import taskmanager.models.Task;

import java.util.List;

public interface TaskRepository {

    List<Task> findAll() throws DataAcessException;

    Task findById(int taskId) throws DataAcessException;

    Task create(Task task) throws DataAcessException;

    boolean update(Task task) throws DataAcessException;

    boolean delete(int taskId) throws DataAcessException;




}
