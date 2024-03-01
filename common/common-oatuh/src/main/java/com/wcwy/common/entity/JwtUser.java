package com.wcwy.common.entity;

import com.wcwy.common.config.session.GlobalConstant;
import com.wcwy.common.config.session.LoginUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * ClassName: JwtUser
 * Description:
 * date: 2022/9/1 15:41
 *
 * @author tangzhuo
 * @since JDK 1.8
 */
@Data
public class JwtUser implements UserDetails {

    private LoginUser loginUser=new LoginUser();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return loginUser.getAuthorities();
    }

    @Override
    public String getPassword() {
        return loginUser.getPassword();
    }

    @Override
    public String getUsername() {
        return loginUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return loginUser.getPasswordErrorNum() <= 5;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return loginUser.getValid().equals(GlobalConstant.IS_VALID.YES);
    }
}