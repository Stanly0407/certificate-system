package com.epam.esm.repository;

import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * An interface {@code OrderRepository} defines the methods for retrieving order data from the database;
 *
 * @author Sviatlana Shelestava
 * @since 2.0
 */
public interface OrderRepository {

    /**
     * Executes the SQL <code>INSERT</code> statement, which creates new order in the database;
     *
     * @param order is a new order to be created;
     * @return a <code>Long</code> generated id of the newly created order
     */
    long saveOrder(Order order);

    /**
     * Executes an SQL <code>UPDATE</code> statement, which updates order status on true;
     *
     * @param orderId is an order payment status to be updated;
     */
    void updateOrderStatus(Long orderId);

    /**
     * Executes an SQL <code>SELECT</code> statement, which searches for order by id;
     *
     * @param orderId is a unique field of order in database;
     * @param user    is the order owner in database;
     * @return an <code>Optional</code> contains the order with matching id
     * or <code>Optional</code> contain a null value;
     */
    Optional<Order> findById(Long orderId, User user);

    /**
     * Determines, based on the parameters of the search, the number of result pages for pagination purposes;
     *
     * @param userId is a user id;
     * @param isPaid is an order payment status;
     * @return a number of <code>long</code> request result pages;
     */
    long getUserOrdersQuantity(Long userId, boolean isPaid);

    /**
     * Executes the SQL <code>SELECT</code> statement, which returns
     * a collection of users;
     *
     * @param isPaid     is an order payment status;
     * @param userId     is a user id;
     * @param pageNumber is a requested number of page with search result;
     * @param pageSize   is a number of request result displayed
     * @return a collection <code>List</code> contains user orders or empty collection <code>List</code>;
     */
    List<Order> getUserOrders(Long userId, boolean isPaid, int pageNumber, int pageSize);

    /**
     * Executes the SQL <code>SELECT</code> statement, which returns
     * a collection of users;
     *
     * @param userId     is a user id;
     * @param pageNumber is a requested number of page with search result;
     * @param pageSize   is a number of request result displayed
     * @return a collection <code>List</code> contains user orders or empty collection <code>List</code>;
     */
    List<Order> getAllUserOrders(Long userId, int pageNumber, int pageSize);

    /**
     * Determines, based on the parameters of the search, the number of result pages for pagination purposes,
     * independent from order payment status;
     *
     * @param userId is a user id;
     * @return a number of <code>long</code> request result pages;
     */
    long getAllUserOrdersQuantity(Long userId);

}
