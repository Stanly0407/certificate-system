package com.epam.esm.services.service;

import com.epam.esm.entities.Order;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.requests.OrderCreateRequest;
import com.epam.esm.services.requests.PaymentRequest;

import java.util.List;

/**
 * An interface {@code OrderService} defines the service layer for a order entity with business logic
 * methods (fetching data, deleting, etc.) that access the data access layer
 * and prepares the data to the users if required.
 *
 * @author Sviatlana Shelestava
 * @since 2.0
 */
public interface OrderService {

    /**
     * Creates a new order;
     *
     * @param orderCreateRequest is request body form with user id and ordered giftCertificate id;
     * @return <code>Long</code> id of new created tag
     * @throws ResourceNotFoundException if could not find user or giftCertificate;
     */
    Long createOrder(OrderCreateRequest orderCreateRequest) throws ResourceNotFoundException;

    /**
     * Changes order payment status as paid;
     *
     * @param paymentRequest contains the id of the order;
     * @throws ResourceNotFoundException if user or order not found;
     * @throws BadRequestException       if invalid parameters input for order payment;
     */
    void payOrder(PaymentRequest paymentRequest) throws ResourceNotFoundException, BadRequestException;

    /**
     * Finds orders by user id;
     *
     * @param userId     is a unique field of the user;
     * @param pageNumber is a requested number of page with search result;
     * @param pageSize   is a number of request result displayed
     * @return a collection <code>List</code> contains the orders or empty collection <code>List</code>;
     * @throws ResourceNotFoundException if the requested tag is not found;
     */
    List<Order> getUserOrders(Long userId, Boolean isPaid, int pageNumber, int pageSize) throws ResourceNotFoundException;

    /**
     * Finds order by id;
     *
     * @param orderId is a unique field of the order;
     * @param isPaid  is an order payment status;
     * @throws ResourceNotFoundException if the resource is not found;
     */
    Object getOrderById(Long orderId, Boolean isPaid) throws ResourceNotFoundException;

    /**
     * Determines, based on the parameters of the request, the number of result pages for pagination purposes;
     *
     * @param pageNumber is a requested number of page with search result;
     * @param pageSize   is a number of request result displayed;
     * @param userId     is a unique field of the user;
     * @param isPaid     is an order payment status;
     * @return a number of <code>Long</code> request result pages;
     * @throws ResourceNotFoundException if the  requested page does not found;
     */
    long getOrdersPaginationInfo(Integer pageSize, Boolean isPaid, Integer pageNumber, Long userId)
            throws ResourceNotFoundException;

}
