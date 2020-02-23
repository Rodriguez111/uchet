package uchet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {




//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authProvider);
//    }

    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select user_login, user_password, TRUE from users where user_login=?")
                .authoritiesByUsernameQuery("select user_login, position FROM users JOIN positions_users ON users.id=positions_users.user_id JOIN positions ON positions_users.position_id=positions.id WHERE user_login=?");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/").anonymous()
                .antMatchers("/css/**", "/png/**").permitAll()
                .antMatchers("/**").hasAnyAuthority("root", "administrator")

                .and()
                .formLogin()

                //.loginPage("/login")
                .loginProcessingUrl("/login/process")
                .usernameParameter("login")
                .defaultSuccessUrl("/main")//авторизованный пользователь редиректится сюда
                .failureUrl("/login")
                .and().logout().logoutSuccessUrl("/login")
        ;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}

