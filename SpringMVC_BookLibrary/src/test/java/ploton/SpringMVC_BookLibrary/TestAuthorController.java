package ploton.SpringMVC_BookLibrary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ploton.SpringMVC_BookLibrary.controller.AuthorController;
import ploton.SpringMVC_BookLibrary.model.entity.Author;
import ploton.SpringMVC_BookLibrary.model.entity.Book;
import ploton.SpringMVC_BookLibrary.model.service.AuthorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@WebMvcTest(AuthorController.class)
class TestAuthorController {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthorService authorService;
    private List<Author> authorList;
    private Pageable pageable;
    private int wrongId;
    private Map<String, Object> updates;

    @BeforeEach
    public void setUp() {
        authorList = List.of(
                new Author(1, "Alexey Alexeev", 22, "Moscow", null),
                new Author(2, "Maria Petrova", 37, "Krasnodar", null)
        );
        pageable = PageRequest.of(0, 10);
        wrongId = 66;
        updates = Map.of("name", "Updated Name", "birthCity", "Updated City");
    }

    //1. Проверяем EndPoint сохранения новой сущности
    @Test
    public void testSaveAuthor() throws Exception {
        Mockito.when(authorService.save(authorList.get(0))).thenReturn(authorList.get(0));

        String authorToJson = new ObjectMapper().writeValueAsString(authorList.get(0));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authors/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorToJson))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorList.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorList.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(authorList.get(0).getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthCity").value(authorList.get(0).getBirthCity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.books").value(authorList.get(0).getBooks()));
    }

    //2. Проверяем EndPoint получения всех сущностей с Пагинацией `Pageable`
    @Test
    public void testGetAllPageable() throws Exception {
        Mockito.when(authorService.getAll(Mockito.any(Pageable.class))).thenReturn(
                new PageImpl<>(authorList, pageable, authorList.size())
        );

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authors/")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(authorList.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value(authorList.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(authorList.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value(authorList.get(1).getName()));
    }

    //3. Проверяем EndPoint получение сущности по ID
    @Test
    public void testGetById() throws Exception {
        Mockito.when(authorService.getById(Mockito.any(Integer.class))).thenReturn(authorList.get(1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authors/{id}", 2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorList.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorList.get(1).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(authorList.get(1).getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthCity").value(authorList.get(1).getBirthCity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.books").value(authorList.get(1).getBooks()));
    }

    //4. Проверяем EndPoint получение сущности по Wrong ID
    @Test
    public void testGetById_WrongId() throws Exception {
        Mockito.when(authorService.getById(Mockito.any(Integer.class)))
                .thenThrow(new NoSuchElementException("Author ID - " + wrongId));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/authors/{id}", wrongId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("No Such Element Exception"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Author ID - " + wrongId));
    }

    //5. Проверяем EndPoint обнавление полей сущности по ID
    @Test
    public void testUpdateById() throws Exception {
        Author updatedAuthor = authorList.get(1);
        updatedAuthor.setName((String) updates.get("name"));
        updatedAuthor.setBirthCity((String) updates.get("birthCity"));

        Mockito.when(authorService.updateById(Mockito.any(Integer.class), Mockito.any(Map.class)))
                .thenReturn(updatedAuthor);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/authors/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updates)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorList.get(1).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthCity").value(authorList.get(1).getBirthCity()));
    }

    //6. Проверяем EndPoint удаление сущности по Wrong ID
    @Test
    public void testDeleteById() throws Exception {
        Mockito.when(authorService.deleteById(Mockito.any(Integer.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/authors/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(1));
    }

    //7. Проверяем EndPoint удаления сущности по Wrong ID
    @Test
    public void testDeleteById_WrongId() throws Exception {
        Mockito.when(authorService.deleteById(Mockito.any(Integer.class)))
                .thenThrow(new NoSuchElementException("Author ID - " + wrongId));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/authors/{id}", wrongId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("No Such Element Exception"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Author ID - " + wrongId));
    }
}
