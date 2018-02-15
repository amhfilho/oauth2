package com.example.googleresource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mult-e on 24/07/2017.
 */

public class MemoryUserRolesService implements UserRolesService{

    @Override
    public List<GrantedAuthority> loadAuthoritiesFromWebGroups(String username) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_WEBGROUP_USER");
        return Arrays.asList(grantedAuthority);
    }
}
