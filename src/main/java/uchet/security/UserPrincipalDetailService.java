package uchet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uchet.models.User;
import uchet.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserPrincipalDetailService implements UserDetailsService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String login) {
        if (login.length() == 0) {
            throw new BadCredentialsException("Логин не может быть пустым");
        }
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new BadCredentialsException("Логин " + login + " не существует");
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String password = request.getParameter("password");
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Неверный пароль");
        }
        return new CustomUserPrincipal(user);
    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
