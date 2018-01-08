package service;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import springboot.model.Todo;
import springboot.model.constants.TodoPriority;
import springboot.repository.TodoRepository;
import springboot.service.TodoService;

import java.util.Collections;
import java.util.List;

/**
 * created at 8 January 2018
 */
public class TodoServiceTest {

    @InjectMocks
    private TodoService todoService; // Membuat obyek TodoService sekaligus menginjectkan mock TodoRepository

    @Mock
    private TodoRepository todoRepository; // Membuat mock dari TodoRepository

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

    @After
    public void tearDown() {
        BDDMockito.verifyNoMoreInteractions(this.todoRepository); // Memastikan bahwa penggunaan todoRepository hanya sekali didalam method getAll() todoService
    }

    @Test
    public void saveTodoTest() {
        /*
            Membuat agar saat method store di todoRepository dipanggil,
            akan mengembalikan true
         */
        BDDMockito.given(todoRepository.store(todo)).willReturn(true);

        boolean result = todoService.saveTodo(name, priority); // Menyimpan data dengan todoService

        Assert.assertThat(result, Matchers.equalTo(true)); // Memastikan return value bernilai true

        /*
            Memastikan method getAll() pada todoRepository dipanggil di dalam method getAll() todoService
         */
//        BDDMockito.verify(todoRepository).store(todo);
        BDDMockito.then(todoRepository).should().store(todo);
    }

    @Test
    public void getAllTest() {
        /*
            Membuat saat method getAll() pada todoRepository dipanggil,
            akan mengembalikan list todos
         */
        BDDMockito.given(todoRepository.getAll()).willReturn(Collections.singletonList(todo));

        List<Todo> todos = todoService.getAll(); // Mengambil data dengan method getAll() pada todoService

        Assert.assertThat(todos, Matchers.notNullValue()); // Memastikan return value tidak null
        Assert.assertThat(todos.isEmpty(), Matchers.equalTo(false)); // Memastikan return value tidak kosong

        /*
            Memastikan method getAll() pada todoRepository dipanggil di dalam method getAll() todoService
         */
//        BDDMockito.verify(todoRepository).getAll();
        BDDMockito.then(todoRepository).should().getAll();
    }
}
