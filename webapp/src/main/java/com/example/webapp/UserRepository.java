package com.example.webapp;

public interface UserRepository {

    AuthenticatedUser loadByUsername(String username);
}
