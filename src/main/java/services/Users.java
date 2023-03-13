package services;

import models.User;

public interface Users {
    User findByUsername(String username);
    User findByEmail(String email);
    Long insert(User user);
}
