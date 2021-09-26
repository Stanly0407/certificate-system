package com.epam.esm.services.service;

import com.epam.esm.entities.User;
import com.epam.esm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final String PREVIOUS_PAGE = "previousPage";
    private static final String NEXT_PAGE = "nextPage";

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getById(Long id) {
        return userRepository.getById(id);
    }

    public List<User> getAllUsers(int pageNumber, int pageSize) {
        long pageQuantity = getUsersPageQuantity(pageSize);
        if (pageNumber <= pageQuantity) {
            return userRepository.getAllUsers(pageNumber, pageSize);
        } else {
            return new ArrayList<>();
        }
    }

    private long getUsersPageQuantity(int pageSize) {
        long countResult = userRepository.getUsersCommonQuantity();
        if ((countResult % pageSize) != 0){
            return (countResult / pageSize) + 1;
        } else {
            return (countResult / pageSize);
        }
    }

    public Map<String, Integer> getUsersPaginationInfo(int pageNumber, int pageSize) {

        long pageQuantity = getUsersPageQuantity(pageSize);

        //static common interface method
        Map<String, Integer> pages = new HashMap<>();
        Integer previousPage = null;
        Integer nextPage = null;

        if ((pageNumber - 1) > 0) {
            previousPage = pageNumber - 1;
        }
        if ((pageNumber + 1) <= pageQuantity) {
            nextPage = pageNumber + 1;
        }

        if (previousPage != null) {
            pages.put(PREVIOUS_PAGE, previousPage);
        }
        if (nextPage != null) {
            pages.put(NEXT_PAGE, nextPage);
        }
        return pages;
    }

}
