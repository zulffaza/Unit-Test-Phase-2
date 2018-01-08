package repository;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import springboot.model.Todo;
import springboot.model.constants.TodoPriority;
import springboot.repository.TodoRepository;

import java.util.List;

/**
 * created at 8 January 2018
 */
public class TodoRepositoryTest {

    @InjectMocks
    private TodoRepository todoRepository; // Membuat object TodoRepository

    /*
        Menset data awal yang akan disimpan
     */
    private final String name = "Mencuci Baju";
    private final TodoPriority priority = TodoPriority.HIGH;

    private final Todo todo = new Todo(name, priority); // Menset obyek todo yang akan disimpan

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Mengenable penggunaan anotasi mockito
    }

    @Test
    public void storeTest() {
        boolean result = todoRepository.store(todo); // Menyimpan data didalam todoRepository

        Assert.assertThat(result, Matchers.equalTo(true)); // Mengecek apakah kembalian adalah true
    }

    @Test
    public void getAllTest() {
        List<Todo> todos = todoRepository.getAll(); // Mengambil seluruh data yang ada dengan todoRepository

        Assert.assertThat(todos, Matchers.notNullValue()); // Mengecek kembalian tidak null
    }
}
