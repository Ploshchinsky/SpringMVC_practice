package ploton.SpringMVCJsonView.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ploton.SpringMVCJsonView.exception.UserNotFoundException;
import ploton.SpringMVCJsonView.model.entity.User;
import ploton.SpringMVCJsonView.model.entity.Views;
import ploton.SpringMVCJsonView.model.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String helloWorld() {
        return "Hello World!";
    }

    //Сохранение пользователя
    @PostMapping("/")
    public ResponseEntity<User> save(@RequestBody User user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    //Получение всех пользователей без деталей
    @GetMapping("/")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<User>> getAll() {
        Optional<List<User>> userList = Optional.ofNullable(userService.getAll());
        return userList.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Получение пользователя по ID с деталями
    @GetMapping("/{id}")
    @JsonView(Views.Details.class)
    public ResponseEntity<User> getById(@PathVariable("id") Integer id) {
        Optional<User> user = Optional.ofNullable(userService.getById(id));
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Обнавление информации о пользователе
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateById(@PathVariable("id") Integer id, @RequestBody Map<String, Object> updates) {
        Optional<User> user = Optional.of(userService.updateById(id, updates));
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Удаление пользователя по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteById(@PathVariable("id") Integer id) {
        userService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
