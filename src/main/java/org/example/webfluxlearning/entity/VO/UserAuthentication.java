package org.example.webfluxlearning.entity.VO;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.NoArgsConstructor;
import org.example.webfluxlearning.entity.PO.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class UserAuthentication implements UserDetails {

    private User user;

    @JSONField(serialize = false)
    private Collection<? extends GrantedAuthority> authorities;

    public UserAuthentication(User user, List<String> roles) {
        this.user = user;
        this.authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPasswordHashHex();
    }

    @Override
    public String getUsername() {
        return user.getUuid();
    }
}
