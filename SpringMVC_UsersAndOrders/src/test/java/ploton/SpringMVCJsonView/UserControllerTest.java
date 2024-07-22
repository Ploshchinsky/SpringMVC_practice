package ploton.SpringMVCJsonView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ploton.SpringMVCJsonView.controller.UserController;
import ploton.SpringMVCJsonView.exception.GlobalExceptionHandler;
import ploton.SpringMVCJsonView.exception.UserNotFoundException;
import ploton.SpringMVCJsonView.model.entity.User;
import ploton.SpringMVCJsonView.model.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    private List<User> users;
    private Map<String, Object> updates;
    private ObjectWriter writer;

    @BeforeEach
    public void setUp() {
        users = List.of(
                new User(1, "Ploshchinsky Anton", 28, "made_of_fire@mail.ru", null),
                new User(1, "Ivanov Ivan", 29, "ivanov@yandex.ru", null));
        updates = new HashMap<>();
        updates.put("name", "Alexey Petrov");
        updates.put("age", 666);
        writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
    }

    @Test
    public void testAddUser() throws Exception {
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(users.get(0));

        String userToJson = writer.writeValueAsString(users.get(0));
        //Проверяем, что получаем объект в виде JSON и проверяем все поля на соотвествие
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(users.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(users.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(users.get(0).getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orders").value(users.get(0).getOrders()));

    }

    @Test
    public void testGetAllUsers() throws Exception {
        Mockito.when(userService.getAll()).thenReturn(users);

        //Проверяем на одном из элементов возращаемого списка,
        //что получаем объект в виде Summary View Json
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(users.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(users.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(users.get(0).getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].orders").doesNotExist());
    }

    @Test
    public void testGetUserById() throws Exception {
        Mockito.when(userService.getById(2)).thenReturn(users.get(1));

        //Проверям, что возвращаем ответ в виде Details View Json
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{2}", 2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(users.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(users.get(1).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(users.get(1).getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(users.get(1).getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orders").value(users.get(1).getOrders()));
    }

    @Test
    public void testGetUserById_wrongId() throws Exception {
        Mockito.when(userService.getById(5)).thenThrow(new UserNotFoundException("User ID - " + 5));
        GlobalExceptionHandler.Error error =
                new GlobalExceptionHandler.Error("User Not Found Exception", "User ID - " + 5);

        //Проверяем сценирий с некорректным ID и что запрос возвращает объект Error типа Json и его поля корректны
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{5}", 5))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(error.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(error.getMessage()));
    }

    @Test
    public void testUpdateUserById() throws Exception {
        User expected = new User(1, "Alexey Petrov", 666, "made_of_fire@mail.ru", null);

        Mockito.when(userService.updateById(Mockito.eq(1), Mockito.any(Map.class))).thenReturn(expected);

        String updatesToJson = writer.writeValueAsString(updates);
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatesToJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expected.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(expected.getAge()));
    }

    @Test
    public void testDeleteUserById() throws Exception {
        Mockito.when(userService.deleteById(Mockito.any(Integer.class))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(1));
    }

    public void testDeleteUserById_wrongId() throws Exception {
        Mockito.when(userService.deleteById(Mockito.any(Integer.class))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{id}", 500))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }
}
