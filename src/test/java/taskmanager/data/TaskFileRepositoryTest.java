package taskmanager.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.models.Status;
import taskmanager.models.Task;

import java.util.List;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

class TaskFileRepositoryTest {

    private static final String SEED_FILE_PATH = "./data/tasks-seed.csv";
    private static final String TEST_FILE_PATH =  "./data/tasks-test.csv";

    private final TaskFileRepository repository = new TaskFileRepository(TEST_FILE_PATH);

    @BeforeEach
    public void setUp() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);

        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void shouldFindAll() throws DataAcessException {
        List<Task> actual = repository.findAll();
        assertEquals(3, actual.size());

        Task task = actual.get(0);
        assertEquals(1, task.getId());
        assertEquals("2023-10-08", task.getCreatedOn());
        assertEquals("Review Curriculum", task.getTitle());
        assertEquals("check content for spelling and grammar", task.getDescription());
        assertEquals("2023-10-10",task.getDueDate());
        assertEquals(Status.TODO, task.getStatus());
    }

    @Test
    public void shouldFindByExistingId() throws DataAcessException {
        Task taskOne = repository.findById(1);
        assertNotNull(taskOne);
        assertEquals(1, taskOne.getId());
        assertEquals("2023-10-08", taskOne.getCreatedOn());
        assertEquals("Review Curriculum", taskOne.getTitle());
        assertEquals("check content for spelling and grammar", taskOne.getDescription());
        assertEquals("2023-10-10",taskOne.getDueDate());
        assertEquals(Status.TODO, taskOne.getStatus());
    }

    @Test
    public void shouldNotFindNonExistingId() throws DataAcessException {
        Task notValid = repository.findById(9999);
        assertNull(notValid);
    }

    @Test
    public void shouldCreate() throws DataAcessException {
        Task task = new Task(0,"2024-02-01",
                "Birthday reminder",
                "Order and send flowers",
                "2024-02-05",Status.TODO);
        Task actual = repository.create(task);
        assertEquals(4, actual.getId());

        List<Task> all = repository.findAll();
        assertEquals(4, all.size());

        assertEquals("2024-02-01", actual.getCreatedOn());
        assertEquals("Birthday reminder", actual.getTitle());
        assertEquals("Order and send flowers", actual.getDescription());
        assertEquals("2024-02-05",actual.getDueDate());
        assertEquals(Status.TODO, actual.getStatus());
    }

    @Test
    public void shouldCreateWithCommans() throws DataAcessException {
        Task task = new Task(0,"2024-05-05",
                "Book venues, wedding, reception",
                "Book venues for upcoming event - wedding, reception",
                "2024-05-08",Status.IN_PROGRESS);

        Task actual = repository.create(task);
        assertEquals(4, actual.getId());

        List<Task> all = repository.findAll();
        assertEquals(4, all.size());

        assertEquals("2024-05-05", actual.getCreatedOn());
        assertEquals("Book venues, wedding, reception", actual.getTitle());
        assertEquals("Book venues for upcoming event - wedding, reception", actual.getDescription());
        assertEquals("2024-05-08",actual.getDueDate());
        assertEquals(Status.IN_PROGRESS, actual.getStatus());
    }

    @Test
    public void shouldUpdate() throws DataAcessException {
        Task task = repository.findById(1);
        task.setStatus(Status.IN_PROGRESS);
        task.setDescription("check content for spelling and grammar and proof read");

        boolean result = repository.update(task);
        assertTrue(result);

        assertNotNull(task);

        assertEquals(1, task.getId());
        assertEquals("2023-10-08", task.getCreatedOn());
        assertEquals("Review Curriculum", task.getTitle());
        assertEquals("check content for spelling and grammar and proof read", task.getDescription());
        assertEquals("2023-10-10",task.getDueDate());
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    public void shouldNotUpdateUnknownId() throws DataAcessException {
        Task task = new Task(7, " ", " ", " "," ",Status.TODO);
        boolean result = repository.update(task);
        assertFalse(result);
    }

    @Test
    public void shouldDelete() throws DataAcessException {
        boolean result = repository.delete(1);
        assertTrue(result);

        List<Task> all = repository.findAll();
        assertEquals(2, all.size());

        Task task = repository.findById(1);
        assertNull(task);
    }
    @Test
    public void shouldNotDeleteUnknownId() throws DataAcessException {
        boolean result = repository.delete(50);
        assertFalse(result);
    }

}