package ploton.SpringMVCJsonView.model.service;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ploton.SpringMVCJsonView.exception.UserNotFoundException;
import ploton.SpringMVCJsonView.exception.WrongUserFieldsException;
import ploton.SpringMVCJsonView.model.entity.Order;
import ploton.SpringMVCJsonView.model.entity.User;
import ploton.SpringMVCJsonView.model.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserService implements Serviceable<User> {
    private Pattern emailPattern = Pattern.compile("^[\\w-\\.\\_]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private Pattern namePattern = Pattern.compile("^([a-zA-Z]+\\s?)+$");
    private Pattern agePattern = Pattern.compile("^\\d{1,3}$");
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User entity) {
        validateFields(entity);
        return userRepository.save(entity);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findByIdWithLock(id)
                .orElseThrow(() -> new UserNotFoundException("User ID - " + id));
    }

    @Override
    public boolean exist(User entity) {
        return userRepository.exists(Example.of(entity));
    }

    @Override
    public boolean deleteById(Integer id) {
        userRepository.findByIdWithLock(id)
                .orElseThrow(() -> new UserNotFoundException("User ID - " + id));
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public User updateById(Integer id, Map<String, Object> updates) {
        User temp = userRepository.findByIdWithLock(id)
                .orElseThrow(() -> new UserNotFoundException("User ID - " + id));
        System.out.println("==========\n"+updates.keySet());

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            switch (entry.getKey()) {
                case "id":
                    temp.setId((Integer) entry.getValue());
                    break;
                case "name":
                    temp.setName((String) entry.getValue());
                    break;
                case "age":
                    temp.setAge((Integer) entry.getValue());
                    break;
                case "email":
                    temp.setEmail((String) entry.getValue());
                    break;
                case "orders":
                    temp.setOrders((List<Order>) entry.getValue());
                    break;
            }
        }
        return validateFields(temp) ? temp : new User();
    }

    private boolean validateFields(User entity) {
        if (!namePattern.matcher(entity.getName()).matches()) {
            throw new WrongUserFieldsException(
                    "Validation Error: User ID - " + entity.getId() + " Wrong Name - " + entity.getName());
        }
        if (!emailPattern.matcher(entity.getEmail()).matches()) {
            throw new WrongUserFieldsException(
                    "Validation Error: User ID - " + entity.getId() + " Wrong Email - " + entity.getEmail());
        }
        if (!agePattern.matcher(String.valueOf(entity.getAge())).matches()) {
            throw new WrongUserFieldsException(
                    "Validation Error: User ID - " + entity.getId() + " Wrong Age - " + entity.getAge());
        }
        return true;
    }

}
