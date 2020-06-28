package uchet.service.users;

import uchet.models.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> getAll();

    Map<String, String> addUser(User user);

    Map<String, String> updateUser(User user);

    Map<String, String> deleteUser(String id);

    List<User> findByFilter(Map<String, String> filterParams);

}
