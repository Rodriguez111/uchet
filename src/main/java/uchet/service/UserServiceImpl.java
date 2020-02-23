package uchet.service;

import org.springframework.stereotype.Service;
import uchet.models.User;
import uchet.repository.UserRepository;


import java.util.List;

@Service
public class UserServiceImpl implements UserService {

   private UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }
}
