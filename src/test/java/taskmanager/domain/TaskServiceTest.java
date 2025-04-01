package taskmanager.domain;

import org.junit.jupiter.api.Test;
import taskmanager.data.DataAcessException;
import taskmanager.data.TaskRepository;
import taskmanager.data.TaskRepositoryDouble;
import taskmanager.models.Status;
import taskmanager.models.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    TaskRepository repository = new TaskRepositoryDouble();
    TaskService service = new TaskService(repository);

    //Create
    @Test
    public void shouldCreateTask() throws DataAcessException {
        TaskResult actual = service.create(new Task(0, "2023-05-09","Prepare Snacks","Prepare a lot of snacks","2023-05-11",Status.COMPLETED));
        assertNotNull(actual.getTask());

        assertTrue(actual.isSuccess());

        assertEquals(99,actual.getTask().getId());
    }
    @Test
    public void shouldNotCreateANullTask() throws DataAcessException {
        TaskResult actual = service.create(null);
        assertFalse(actual.isSuccess());
        assertNull(actual.getTask());
        assertEquals("Task cannot be null", actual.getMessages().get(0));
    }
    @Test
    public void shouldNotCreateTaskWithoutStartDate() throws DataAcessException {
        TaskResult actual = service.create(new Task(0, "","Prepare Snacks","Prepare a lot of snacks","2023-05-11",Status.COMPLETED));
        assertFalse(actual.isSuccess());
        assertNull(actual.getTask());
        assertEquals("Create on date is required", actual.getMessages().get(0));
    }

    @Test
    public void shouldNotCreateTaskWithoutTitle() throws DataAcessException {
        TaskResult actual = service.create(new Task(0, "2023-05-09","","Prepare a lot of snacks","2023-05-11",Status.COMPLETED));
        assertFalse(actual.isSuccess());
        assertNull(actual.getTask());
        assertEquals("Title must exist and cannot be longer than 50 characters", actual.getMessages().get(0));
    }
    @Test
    public void shouldNotCreateTaskWithTitleWIthLongerThan50Char() throws DataAcessException {
        TaskResult actual = service.create(new Task(0, "2023-05-09","Prepare Snacks Prepare a lot of snacksPrepare a lot of snacksPrepare a lot of snacksPrepare a lot of snacksPrepare a lot of snacks","Prepare a lot of snacks","2023-05-11",Status.COMPLETED));
        assertFalse(actual.isSuccess());
        assertNull(actual.getTask());
        assertEquals("Title must exist and cannot be longer than 50 characters", actual.getMessages().get(0));
    }
    @Test
    public void shouldNotCreateTaskWithoutDescription() throws DataAcessException {
        TaskResult actual = service.create(new Task(0, "2023-05-09","Prepare Snacks","","2023-05-11",Status.COMPLETED));
        assertFalse(actual.isSuccess());
        assertNull(actual.getTask());
        assertEquals("Description is required and must be more than 20 characters", actual.getMessages().get(0));
    }
    @Test
    public void shouldNotCreateTaskWithDescriptionLessThan20Char() throws DataAcessException {
        TaskResult actual = service.create(new Task(0, "2023-05-09","Prepare Snacks","description","2023-05-11",Status.COMPLETED));
        assertFalse(actual.isSuccess());
        assertNull(actual.getTask());
        assertEquals("Description is required and must be more than 20 characters", actual.getMessages().get(0));
    }
    @Test
    public void shouldNotCreateTaskWithoutDueDate() throws DataAcessException {
        TaskResult actual = service.create(new Task(0, "2023-05-09","Prepare Snacks","Prepare a lot of snacks","",Status.COMPLETED));
        assertFalse(actual.isSuccess());
        assertNull(actual.getTask());
        assertEquals("Due date is required", actual.getMessages().get(0));
    }
    @Test
    public void shouldNotCreateTaskWithoutStaus() throws DataAcessException {
        TaskResult actual = service.create(new Task(0, "2023-05-09","Prepare Snacks","Prepare a lot of snacks","2023-05-11",null));
        assertFalse(actual.isSuccess());
        assertNull(actual.getTask());
        assertEquals("Status  is required", actual.getMessages().get(0));
    }

    //Read
    @Test
    public void shouldFindAll() throws DataAcessException {
        List<Task> actual = service.findAll();
        assertEquals(actual.size(), 3);

        Task task = actual.get(0);
        assertEquals(1, task.getId());
        assertEquals("2024-08-01", task.getCreatedOn());
        assertEquals("Bake cake", task.getTitle());
        assertEquals("Bake a cake for birthday", task.getDescription());
        assertEquals("2024-08-22", task.getDueDate());
        assertEquals( Status.TODO, task.getStatus());
    }
    @Test
    public void shouldFindExistingId() throws DataAcessException {
        Task task = service.findById(1);
        assertNotNull(task);

        assertEquals(1, task.getId());
        assertEquals("2024-08-01", task.getCreatedOn());
        assertEquals("Bake cake", task.getTitle());
        assertEquals("Bake a cake for birthday", task.getDescription());
        assertEquals("2024-08-22", task.getDueDate());
        assertEquals( Status.TODO, task.getStatus());
    }
    @Test
    public void shouldNotFindNonExistingId() throws DataAcessException {
        Task task = service.findById(6);
        assertNull(task);
    }
    //Update
    @Test
    public void shouldUpdateExistingTask() throws DataAcessException {
        List<Task> all = service.findAll();
        Task toUpdate = all.get(0);
        toUpdate.setDescription("This is an updated task description to test");

        TaskResult actual = service.update(toUpdate);
        assertTrue(actual.isSuccess());
        assertEquals(0, actual.getMessages().size());
        assertEquals("This is an updated task description to test", all.get(0).getDescription());
    }
    @Test
    public void shouldNotUpdateNonExistingTask() throws DataAcessException {
        Task task = new Task(0,"2024-08-01","Fake task","fake description to test","2024-09-22",Status.COMPLETED);
        TaskResult actual = service.update(task);

        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertTrue(actual.getMessages().get(0).contains("does not exist"));

    }
    @Test
    public void shouldNotUpdateNullTask() throws DataAcessException {
        TaskResult actual = service.update(null);

        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertTrue(actual.getMessages().get(0).contains("Task cannot be null"));
    }
    //Delete
    @Test
    public void shouldDeleteExistingTask() throws DataAcessException {
        TaskResult actual = service.deleteById(1);
        assertTrue(actual.isSuccess());

    }
    @Test
    public void shouldNotDeleteNonExistingTask() throws DataAcessException {
        TaskResult actual = service.deleteById(99);
        assertFalse(actual.isSuccess());
        assertEquals(1, actual.getMessages().size());
        assertTrue(actual.getMessages().get(0).contains("does not exist"));
    }

}