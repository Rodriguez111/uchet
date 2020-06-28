package uchet.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uchet.models.Position;
import uchet.models.User;
import uchet.repository.PositionRepository;
import uchet.repository.UserRepository;
import uchet.service.daoutils.EntityFieldsUpdater;
import uchet.service.daoutils.FieldExistsChecker;
import uchet.service.daoutils.FieldExistsCheckerImpl;
import uchet.service.daoutils.FieldsUpdater;
import uchet.service.filter.FilterService;
import uchet.service.filter.FilterServiceImpl;
import uchet.service.filter.LocalFilterSpecifConstructor;
import uchet.service.filter.SpecificationConstructor;
import uchet.utils.Util;


import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private PositionRepository positionRepository;

    private PasswordEncoder passwordEncoder;

    private FieldExistsChecker<User> checker = new FieldExistsCheckerImpl<>();

    @Override
    public List<User> findByFilter(Map<String, String> filterParams) {
        Function<Specification<User>, List<User>> function = spc -> userRepository.findAll(spc);
        SpecificationConstructor<User> constructor = new LocalFilterSpecifConstructor<>();
        FilterService<User> filterService = new FilterServiceImpl<>(function, constructor);
        return filterService.findEntriesByFilter(filterParams);
    }

    private Map<String, BiConsumer<User,String>> getFieldsUpdateDispatcher() {
        Map<String, BiConsumer<User,String>> fieldsUpdateDispatcher = new HashMap<>();
        fieldsUpdateDispatcher.put("login", User::setLogin);
        fieldsUpdateDispatcher.put("password", (user, parameter) -> {
            String password = passwordEncoder.encode(parameter);
            user.setPassword(password);
        });
        fieldsUpdateDispatcher.put("name", User::setName);
        fieldsUpdateDispatcher.put("surname", User::setSurname);
        fieldsUpdateDispatcher.put("isActive", (user, parameter) -> {
            boolean isActive = parameter.equals("true");
            user.setActive(isActive);
        });
        fieldsUpdateDispatcher.put("position", (user, parameter) -> {
            Position position = positionRepository.findByPosition(parameter);
            user.setPosition(position);
        });
        return fieldsUpdateDispatcher;
    }

    @Override
    public List<User> getAll() {
        List<User> users = ((List<User>) userRepository.findAll());
        return sortUsers(users);
    }

    private List<User> sortUsers(List<User> users) {
       return  users.stream().sorted((o1, o2) -> {
           int result = 0;
           result = o1.getSurname().compareTo(o2.getSurname());
           if (result == 0) {
               result = o1.getName().compareTo(o2.getName());
           }
           if (result == 0) {
               result = o1.getLogin().compareTo(o2.getLogin());
           }
           return result;
       }).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> addUser(User user) {
        String resultMessage = "OK";
        Position position = positionRepository.findByPosition(user.getPosition().getPosition());
        boolean loginExists = checker.fieldExists(userRepository.findByLogin(user.getLogin()));
        if (loginExists) {
            resultMessage = "Логин " + user.getLogin() + " уже существует";
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setPosition(position);
            user = setCurrentTimeStampForCreateDate(user);
            userRepository.save(user);
        }
        Map<String, String> result = new HashMap<>();
        result.put("addResult", resultMessage);
        return result;
    }

    @Override
    public Map<String, String> updateUser(User user) {
        String resultMessage = "OK";
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            User updatedUser = getPreparedCandidateForUpdate(existingUser, user);
            boolean loginExists = checker.fieldExists(userRepository.findByLoginForUpdate(updatedUser.getLogin(),existingUser.getId()));
           if (existingUser.equals(updatedUser)) {
               resultMessage = "Нет полей для обновления";
           } else if (loginExists)   {
               resultMessage = "Пользователь с логином " + updatedUser.getLogin() + " уже существует";
           } else {
               User userToUpdate = setCurrentTimeStampForUpdateDate(updatedUser);
               userRepository.save(userToUpdate);
           }
        }
        Map<String, String> result = new HashMap<>();
        result.put("updateResult", resultMessage);
        return result;
    }

    @Transactional
    @Override
    public Map<String, String> deleteUser(String id) {
        String resultMessage = "Невозможно удалить пользователя, пока существуют зависимые записи";
        int userId = Integer.parseInt(id);
//        if (!checkDependency(userId)) {
            Optional<User> optionalUser = userRepository.findById(userId);
            optionalUser.ifPresent(user -> userRepository.delete(user));
            resultMessage = "OK";
//        }
        Map<String, String> result = new HashMap<>();
        result.put("deleteResult", resultMessage);
        return result;
    }

    private User getPreparedCandidateForUpdate(User existingUser, User userFromClient) {
        EntityFieldsUpdater<User> updater = new FieldsUpdater<>(getFieldsUpdateDispatcher());
        Map<String, String> fieldsToUpdate = updater.getMapOfFields(userFromClient);
        fieldsToUpdate.put("position", userFromClient.getPosition().getPosition());
        fieldsToUpdate.remove("createDate");
        fieldsToUpdate.remove("lastUpdateDate");
        fieldsToUpdate.remove("fullName");
        return updater.update(existingUser, fieldsToUpdate);
    }

    private User setCurrentTimeStampForCreateDate(User user) {
        String currentTimeStamp = Util.generateCurrentTimeStamp();
        user.setCreateDate(currentTimeStamp);
        return user;
    }

    private User setCurrentTimeStampForUpdateDate(User user) {
        String currentTimeStamp = Util.generateCurrentTimeStamp();
        user.setLastUpdateDate(currentTimeStamp);
        return user;
    }


    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setPositionRepository(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
