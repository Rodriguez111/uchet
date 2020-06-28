package uchet.initdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uchet.models.Position;
import uchet.models.User;
import uchet.repository.PositionRepository;
import uchet.repository.UserRepository;
import uchet.utils.Util;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//
//@Service
//public class InitUsers {
//
//    private static final String ADMIN = "admin";
//
//    private UserRepository userRepository;
//
//    private PositionRepository positionRepository;
//
//    private PasswordEncoder passwordEncoder;

//    @PostConstruct
//    private void initUsers() {
//        User user = userRepository.findByLogin(ADMIN);
//        if (user == null) {
//            userRepository.save(createAdmin());
//        }
//    }

//    private User createAdmin() {
//        User admin = new User();
//        Position position = positionRepository.findByPosition(ADMIN);
//        String password = passwordEncoder.encode(ADMIN);
//        String adminName = "ИмяАдминистратора";
//        String adminSurname = "ФамилияАдминистратора";
//        boolean active = true;
//        admin.setLogin(ADMIN);
//        admin.setPassword(password);
//        admin.setPosition(position);
//        admin.setName(adminName);
//        admin.setSurname(adminSurname);
//        admin.setActive(active);
//        admin.setCreateDate(Util.generateCurrentTimeStamp());
//        return admin;
//    }
//
//
//
//    @Autowired
//    public void setUserRepository(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Autowired
//    public void setPositionRepository(PositionRepository positionRepository) {
//        this.positionRepository = positionRepository;
//    }
//
//    @Autowired
//    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }
//}
