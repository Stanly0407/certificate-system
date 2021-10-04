package com.epam.esm.services.service;

import com.epam.esm.entities.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getById(Long id) {
        return userRepository.getById(id);
    }

    public List<User> getAllUsers(int pageNumber, int pageSize) throws ResourceNotFoundException {
        long pageQuantity = getUsersPaginationInfo(pageNumber, pageSize);
        if (pageNumber <= pageQuantity) {
            return userRepository.getAllUsers(pageNumber, pageSize);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public long getUsersPaginationInfo(int pageNumber, int pageSize) {
        long countResult = userRepository.getUsersCommonQuantity();
        if ((countResult % pageSize) != 0) {
            return (countResult / pageSize) + 1;
        } else {
            return (countResult / pageSize);
        }
    }

}
