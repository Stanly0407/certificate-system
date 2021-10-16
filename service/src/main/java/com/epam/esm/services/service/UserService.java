package com.epam.esm.services.service;

import com.epam.esm.entities.User;
import com.epam.esm.services.dto.JwtResponse;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.forms.LoginForm;
import com.epam.esm.services.forms.SignupForm;

import java.util.List;

/**
 * An interface {@code UserService} defines the service layer for a tag entity with business logic
 * methods (fetching data, deleting, etc.) that access the data access layer
 * and prepares the data to the users if required.
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
public interface UserService {

    /**
     * Finds user by id;
     *
     * @param id is a unique field of the user in the database;
     * @return user if exists;
     * @throws ResourceNotFoundException if the resource being deleted does not found;
     */
    User getById(Long id) throws ResourceNotFoundException;

    /**
     * Returns all users;
     *
     * @param pageNumber is a requested number of page with search result;
     * @param pageSize   is a number of request result displayed
     * @return a collection <code>List</code> contains users or empty collection <code>List</code>;
     * @throws ResourceNotFoundException if the requested resource is not found;
     */
    List<User> getAllUsers(int pageNumber, int pageSize) throws ResourceNotFoundException;

    /**
     * Determines, based on the parameters of the request, the number of result pages for pagination purposes;
     *
     * @param pageNumber is one or more unique name of the tag;
     * @param pageSize   is a part and whole word that may appear in the name
     *                   or description of giftCertificate;
     * @return a <code>Long</code> number of  request result pages;
     */
    long getUsersPaginationInfo(int pageNumber, int pageSize);

    Long saveNewUser(SignupForm signupForm) throws BadRequestException;

    JwtResponse login(LoginForm form);

}
