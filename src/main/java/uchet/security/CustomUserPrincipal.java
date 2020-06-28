package uchet.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uchet.models.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserPrincipal implements UserDetails {

    private User user;

    public CustomUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        //Extract list of permissions
        this.user.getPosition().getPermissions().forEach(p -> {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(p.getPermission());
            authorities.add(grantedAuthority);
        });
        //Extract list of positions (roles)
      String role =  this.user.getPosition().getPosition();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role);
        authorities.add(grantedAuthority);
        return authorities;
    }

    public User getUser() {
        return this.user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }


}
