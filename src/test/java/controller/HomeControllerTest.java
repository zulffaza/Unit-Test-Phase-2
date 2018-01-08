package controller;

import com.jayway.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.Introduction;
import springboot.model.Todo;
import springboot.model.constants.TodoPriority;
import springboot.model.request.CreateTodoRequest;
import springboot.service.TodoService;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Introduction.class)
public class HomeControllerTest {

    @MockBean
    private TodoService todoService; // Bean TodoService di application context akan diubah dengan mock

    @LocalServerPort
    private int serverPort; // Menyimpan port yang dialokasikan saat test dijalankan

    /*
        Menset data awal yang akan disimpan
     */
    private final String name = "Mencuci Baju";
    private final TodoPriority priority = TodoPriority.HIGH;

    private final Todo todo = new Todo(name, priority); // Menset obyek todo yang akan disimpan

    @After
    public void tearDown() {
        BDDMockito.verifyNoMoreInteractions(todoService); // Mengecek apakah object todoService hanya dipanggil sekali saja
    }

    @Test
    public void allTest() {

        /*
            Merupakan contoh kembalian json dari API
         */
        String resultJson =
                "{" +
                    "\"code\":200," +
                    "\"message\":null," +
                    "\"value\":" +
                        "[" +
                            "{\"name\":\"Mencuci Baju\"," +
                            "\"priority\":\"HIGH\"}" +
                        "]" +
                "}";

        BDDMockito.when(todoService.getAll()).thenReturn(Collections.singletonList(todo)); // Membuat kembalian dari method todoService getAll()

        RestAssured.given() // Memberikan nilai pada saat akses api
                .contentType("application/json") // Menset content type response data
                .when()
                .port(serverPort) // Menset port dimana aplikasi berjalan
                .get("/todos") // Melakukan request dengan methode ke url
                .then()
                .body(Matchers.containsString("value")) // Mengecek kembalian apakah memiliki string value
                .body(Matchers.containsString(name)) // Mengecek kembalian apakah memiliki string name
                .body(Matchers.equalTo(resultJson)) // Mengecek kembalian apakah sama dengan kembalian yang telah diset diawal
                .statusCode(200); // Menset response status code

        BDDMockito.then(todoService).should().getAll(); // Mengecek apakah method getAll() pada todoService terpanggil atau tidak
    }

    @Test
    public void insertTest() {
        /*
            Merupakan contoh kembalian json dari API
         */
        String resultJson =
                "{" +
                        "\"code\":200," +
                        "\"message\":null," +
                        "\"value\":true" +
                "}";

        BDDMockito.when(todoService.saveTodo(name, priority)).thenReturn(true); // Membuat kembalian dari method todoService saveTodo()

        /*
            Membuat object yang akan dikirim saat melakuakn request
         */
        CreateTodoRequest createTodoRequest = new CreateTodoRequest();
        createTodoRequest.setName(name);
        createTodoRequest.setPriority(priority);

        RestAssured.given() // Memberikan nilai pada saat akses api
                .contentType("application/json") // Menset content type response data
                .when()
                .port(serverPort) // Menset port dimana aplikasi berjalan
                .body(createTodoRequest) // Menset parameter yang akan dikirim saat melakukan request
                .post("/todos") // Melakukan request dengan method ke url
                .then()
                .body(Matchers.containsString("value")) // Mengecek kembalian apakah memiliki string value
                .body(Matchers.containsString("true")) // Mengecek kembalian apakah memiliki string name
                .body(Matchers.equalTo(resultJson)) // Mengecek kembalian apakah sama dengan kembalian yang telah diset diawal
                .statusCode(200); // Menset response status code

        BDDMockito.then(todoService).should().saveTodo(name, priority); // Mengecek apakah method saveTodo() pada todoService terpanggil atau tidak
    }
}
