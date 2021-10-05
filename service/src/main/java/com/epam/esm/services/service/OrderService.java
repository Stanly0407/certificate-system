package com.epam.esm.services.service;

import com.epam.esm.services.dto.OrderDto;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;

import java.util.List;

/**
 * An interface {@code OrderService} defines the service layer for a order entity with business logic
 * methods (fetching data, deleting, etc.) that access the data access layer
 * and prepares the data to the users if required.
 *
 * @author Sviatlana Shelestava
 * @since 1.0
 */
public interface OrderService {

    /**
     * Creates a new order;
     *
     * @param giftCertificateId is a giftCertificate id to be ordered;
     * @param userId            is a id of the user who creates the order;
     * @return <code>Long</code> id of new created tag
     * @throws ResourceNotFoundException if could not find user or giftCertificate;
     */
    Long createOrder(Long giftCertificateId, Long userId) throws ResourceNotFoundException;

    /**
     * Changes order payment status as paid;
     *
     * @param orderId is an order id to be paid;
     * @param userId  is an id of the user who created and pays the order;
     * @throws ResourceNotFoundException if user or order not found;
     * @throws BadRequestException       if invalid parameters input for order payment;
     */
    void payOrder(Long orderId, Long userId) throws ResourceNotFoundException, BadRequestException;

    /**
     * Finds paid user orders by user id;
     *
     * @param userId     is a unique field of the user;
     * @param pageNumber is a requested number of page with search result;
     * @param pageSize   is a number of request result displayed
     * @return a collection <code>List</code> contains the orders or empty collection <code>List</code>;
     * @throws ResourceNotFoundException if the requested tag is not found;
     */
    List<OrderDto> getPaidUserOrders(Long userId, int pageNumber, int pageSize) throws ResourceNotFoundException;

    /**
     * Finds order by id;
     *
     * @param orderId is a unique field of the order;
     * @return paid user order in order DTO object;
     * @throws ResourceNotFoundException if the resource is not found;
     */
    OrderDto getPaidOrderById(Long orderId) throws ResourceNotFoundException;

    /**
     * Determines, based on the parameters of the request, the number of result pages for pagination purposes;
     *
     * @param pageNumber is a requested number of page with search result;
     * @param pageSize   is a number of request result displayed;
     * @param userId     is a unique field of the user;
     * @return a number of <code>Long</code> request result pages;
     * @throws ResourceNotFoundException if the  requested page does not found;
     */
    long getUsersPaginationInfo(Integer pageSize, Integer pageNumber, Long userId) throws ResourceNotFoundException;

}
