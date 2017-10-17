package com.example.webapp;

import org.springframework.stereotype.Service;

@Service
public class MemoryUserRepository implements UserRepository {
    @Override
    public AuthenticatedUser loadByUsername(String username) {
        return new AuthenticatedUser("user","password");
    }
}
