package taskmanager.data;

import taskmanager.models.Status;
import taskmanager.models.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskFileRepository implements TaskRepository {

    private static final String DELIMITER = ",";
    private static final String DELIMITER_REPLACEMENT = "@@@";
    private final String filePath;
    public TaskFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Task> findAll() throws DataAcessException {
        //create a list of tasks
        List<Task> results = new ArrayList<>();
        // try with resources
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            //skip the header
            reader.readLine();
            for(String line = reader.readLine(); line != null; line = reader.readLine())
            {
                Task task = linetoTask(line);
                results.add(task);
            }
        }catch (FileNotFoundException ex)
        {

        }catch (IOException ex)
        {
            throw new DataAcessException("Could not open file path: " + filePath);
        }
        return results;
    }

    @Override
    public Task findById(int taskId) throws DataAcessException {
        List<Task> all = findAll();
        for(Task task : all)
            if(task.getId() == taskId)
            {
                return task;
            }
        return null;
    }

    @Override
    public Task create(Task task) throws DataAcessException {
        List<Task> all = findAll();
        //generate id
        int nextId = getNextId(all);
        task.setId(nextId);
        all.add(task);
        writeToFile(all);
        return task;
    }

    @Override
    public boolean update(Task task) throws DataAcessException {
        List<Task> all = findAll();
        for(int i =0; i < all.size(); i++)
        {
            if(all.get(i).getId() == task.getId())
            {
                all.set(i , task);
                writeToFile(all);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int taskId) throws DataAcessException {
        List<Task> all = findAll();
        for(int i =0; i < all.size(); i++)
        {
           if(all.get(i).getId() == taskId)
           {
               all.remove(i);
               writeToFile(all);
               return true;
           }
        }
        return false;
    }

    //helper methods

    private String restore(String value)
    {
        return value.replace(DELIMITER_REPLACEMENT,DELIMITER);
    }
    private String clean(String value)
    {
        return value.replace(DELIMITER,DELIMITER_REPLACEMENT);
    }
    //deserialize
    private Task linetoTask(String line)
    {
        String[] fields = line.split(DELIMITER);
        if(fields.length != 6)
        {
            return null;
        }
        Task task = new Task(
                Integer.parseInt(fields[0]),
                restore(fields[1]),
                restore(fields[2]),
                restore(fields[3]),
                restore(fields[4]),
                Status.valueOf(fields[5]));
      return task;
    }
    //serializing
    private String taskToLine(Task task)
    {
        StringBuilder buffer = new StringBuilder(100); //add comma after each field
        buffer.append(task.getId()).append(DELIMITER);
        buffer.append(clean(task.getCreatedOn())).append(DELIMITER);
        buffer.append(clean(task.getTitle())).append(DELIMITER);
        buffer.append(clean(task.getDescription())).append(DELIMITER);
        buffer.append(clean(task.getDueDate())).append(DELIMITER);
        buffer.append(task.getStatus()); //last, no comma
        return buffer.toString();
    }
    private void writeToFile(List<Task> tasks) throws DataAcessException {
        try(PrintWriter writer = new PrintWriter(filePath))
        {
            //print the header
            writer.println("id,createdOn,title,description,dueDate,status");
            for(Task task: tasks)
            {
                String line = taskToLine(task);
                writer.println(line);
            }
        }catch(IOException ex)
        {
            throw new DataAcessException("Could not write to filepath: + " + filePath);
        }
    }
    private int getNextId(List<Task> tasks)
    {
        int maxId = 0;
        for(Task task : tasks)
        {
            if(maxId < task.getId())
            {
                maxId = task.getId();
            }
        }
        return maxId + 1;
    }

}
