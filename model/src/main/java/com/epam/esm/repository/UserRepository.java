package com.epam.esm.repository;

import com.epam.esm.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * An interface {@code UserRepository} defines the methods for retrieving user data from the database;
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
public interface UserRepository {

    /**
     * Executes an SQL <code>SELECT</code> statement, which searches for user by id;
     *
     * @param id is a unique field of user in database;
     * @return an <code>Optional</code> contains the user with matching id
     * or <code>Optional</code> contain a null value;
     */
    Optional<User> getById(Long id);

    /**
     * Executes the SQL <code>SELECT</code> statement, which returns
     * a collection of users;
     *
     * @param pageNumber is a requested number of page with search result;
     * @param pageSize   is a number of request result displayed
     * @return a collection <code>List</code> contains users or empty collection <code>List</code>;
     */
    List<User> getAllUsers(int pageNumber, int pageSize);

    /**
     * Counts common users quantity for pagination purposes;
     *
     * @return a number of <code>Long</code> request result pages;
     */
    long getUsersCommonQuantity();

    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);

    Long save(User user);

     void saveRoleUser(Long userId);

}
