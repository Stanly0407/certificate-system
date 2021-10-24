package com.epam.esm.repository;

import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepository {

    private static final String UPDATE_ORDER_IS_PAID_FIELD = "UPDATE Order o SET o.isPaid = true WHERE o.id = :orderId";
    private static final String SELECT_USER_ORDERS = "SELECT o FROM Order o WHERE o.user.id = :userId AND o.isPaid = :isPaid";
    private static final String COUNT_USER_ORDERS = "SELECT count (o) FROM Order o WHERE o.user.id = :userId AND o.isPaid = :isPaid";
    private static final String SELECT_ORDER_BY_ID_AND_USER_ID = "select o from Order o where o.id = :id AND  o.user = : user";
    private static final String SELECT_ALL_USER_ORDERS = "SELECT o FROM Order o WHERE o.user.id = :userId";
    private static final String COUNT_ALL_USER_ORDERS = "SELECT count (o) FROM Order o WHERE o.user.id = :userId";


    @PersistenceContext
    EntityManager entityManager;

    @Override
    public long saveOrder(Order order) {
        entityManager.persist(order);
        entityManager.flush();
        return order.getId();
    }

    @Override
    public void updateOrderStatus(Long orderId) {
        Query query = entityManager.createQuery(UPDATE_ORDER_IS_PAID_FIELD);
        query.setParameter("orderId", orderId);
        query.executeUpdate();
    }

    @Override
    public Optional<Order> findById(Long orderId, User user) {
        Query query = entityManager.createQuery(SELECT_ORDER_BY_ID_AND_USER_ID, Order.class);
        query.setParameter("id", orderId);
        query.setParameter("user", user);
        // if use getSingleResult(); - need try/catch NoResultException
        List<Order> orders = query.getResultList();
        if (orders.isEmpty()) {
            return Optional.empty();
        } else {
            Order order = orders.get(0);
            return Optional.of(order);
        }
    }

    @Override
    public long getUserOrdersQuantity(Long userId, boolean isPaid) {
        Query query = entityManager.createQuery(COUNT_USER_ORDERS);
        query.setParameter("userId", userId);
        query.setParameter("isPaid", isPaid);
        return (long) query.getSingleResult();
    }

    @Override
    public long getAllUserOrdersQuantity(Long userId) {
        Query query = entityManager.createQuery(COUNT_ALL_USER_ORDERS);
        query.setParameter("userId", userId);
        return (long) query.getSingleResult();
    }

    @Override
    public List<Order> getUserOrders(Long userId, boolean isPaid, int pageNumber, int pageSize) {
        Query query = entityManager.createQuery(SELECT_USER_ORDERS, Order.class);
        query.setParameter("userId", userId);
        query.setParameter("isPaid", isPaid);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<Order> getAllUserOrders(Long userId, int pageNumber, int pageSize) {
        Query query = entityManager.createQuery(SELECT_ALL_USER_ORDERS, Order.class);
        query.setParameter("userId", userId);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

}
