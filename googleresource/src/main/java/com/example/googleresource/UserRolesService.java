package com.example.googleresource;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * Created by mult-e on 24/07/2017.
 */
public interface UserRolesService {
    List<GrantedAuthority> loadAuthoritiesFromWebGroups(String username);
}
