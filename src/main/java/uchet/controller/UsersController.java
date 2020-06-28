package uchet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uchet.models.User;
import uchet.service.users.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/users_controller")
public class UsersController {
    private UserService usersService;


    @GetMapping
    @RequestMapping("/all")
    public List<User> listOfAllUsers() {
        return usersService.getAll();
    }

    @PostMapping
    @RequestMapping("/local_filter")
    public List<User> listOfFilteredSku(@RequestBody Map<String, String> params) {
        List<User> result = usersService.findByFilter(params);
        return result;
    }

    @PostMapping
    public Map<String, String> create(@Valid @RequestBody User user){
        return usersService.addUser(user);
    }

    @PutMapping
    public Map<String, String> update(@Valid @RequestBody User user){
        return usersService.updateUser(user);
    }

    @DeleteMapping("{userId}") //id передается в заголовке запроса
    public Map<String, String>  delete(@PathVariable String userId) {
        return usersService.deleteUser(userId);
    }


    @Autowired
    public void setUsersService(UserService usersService) {
        this.usersService = usersService;
    }


}
