package com.epam.esm.services.service;

import com.epam.esm.entities.User;
import com.epam.esm.services.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getById(Long id);

    List<User> getAllUsers(int pageNumber, int pageSize) throws ResourceNotFoundException;

    long getUsersPaginationInfo(int pageNumber, int pageSize);

}
