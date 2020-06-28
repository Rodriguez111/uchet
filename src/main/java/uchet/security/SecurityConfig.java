package uchet.security;

import liquibase.pro.packaged.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private DaoAuthProvider daoAuthProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthProvider.authenticationProvider());
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").anonymous()
                .antMatchers("/css/**", "/js/**", "/fonts/**", "/fontsawesome/**", "/materialicons/**").permitAll()
//                .antMatchers("/**").hasAnyAuthority("admin", "administrator")
                .antMatchers("/main", "/", "/p").authenticated()
                .antMatchers("/admin/users", "/admin/users_controller/**").hasAuthority("users_page")
                .antMatchers("/admin/positions", "/admin/positions_controller/**", "/permissions_controller/**").hasAuthority("positions_page")
                .antMatchers("/contractors", "/contractors_controller/**").hasAuthority("contractors_page")
                .antMatchers("/sku", "/sku_controller/**").hasAuthority("sku_page")
                .antMatchers("/units_controller/**").hasAuthority("sku_page")
                .antMatchers("/incoming_doc_type_controller/**").hasAnyAuthority("incoming_docs_page", "outgoing_docs_page")

                .antMatchers("/accounting/remains", "/remains_controller/**").hasAuthority("remains_page")
                .antMatchers("/accounting/incoming_docs", "/accounting/add_incoming_doc", "/accounting/incoming_doc_details/**", "/incoming_docs_controller/**").hasAuthority("incoming_docs_page")

                .antMatchers("/accounting/sku_in", "/sku_in_controller/**").hasAuthority("sku_in_page")
                .antMatchers("/accounting/outgoing_docs", "/outgoing_docs_controller/**").hasAuthority("outgoing_docs_page")
                .antMatchers("/accounting/sku_out", "/sku_out_controller/**").hasAuthority("sku_out_page")


                .anyRequest().denyAll()
                .and()
                .formLogin()


                .loginPage("/login")
                .loginProcessingUrl("/login/process")
                .usernameParameter("login")
                .defaultSuccessUrl("/main")//авторизованный пользователь редиректится сюда
                .failureUrl("/main")
                .and().logout().logoutSuccessUrl("/login")
        .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler())
        ;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }


    @Autowired
    public void setDaoAuthProvider(DaoAuthProvider daoAuthProvider) {
        this.daoAuthProvider = daoAuthProvider;
    }
}

