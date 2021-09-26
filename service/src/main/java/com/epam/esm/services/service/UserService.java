package com.epam.esm.services.service;

import com.epam.esm.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    Optional<User> getById(Long id);

    List<User> getAllUsers(int pageNumber, int pageSize);

    Map<String, Integer> getUsersPaginationInfo(int pageNumber, int pageSize);

}
