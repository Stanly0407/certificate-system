package com.epam.esm.repository;

import com.epam.esm.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> getById(Long id);

    List<User> getAllUsers(int pageNumber, int pageSize);

    long getUsersCommonQuantity();

}
