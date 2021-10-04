package com.epam.esm.repository;

import com.epam.esm.entities.Order;
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
    private static final String SELECT_USER_PAID_ORDERS = "SELECT o FROM Order o WHERE o.user.id = :userId AND o.isPaid = true";
    private static final String COUNT_USER_PAID_ORDERS = "SELECT count (o) FROM Order o WHERE o.user.id = :userId AND o.isPaid = true";
    private static final String SELECT_USER_BY_ID = "select o from Order o where o.id = :id";

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public long saveOrder(Order order) {
        entityManager.persist(order);
        return order.getId();
    }

    @Override
    public void updateOrderStatus(Long orderId) {
        Query query = entityManager.createQuery(UPDATE_ORDER_IS_PAID_FIELD);
        query.setParameter("orderId", orderId);
        query.executeUpdate();
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        Query query = entityManager.createQuery(SELECT_USER_BY_ID, Order.class);
        query.setParameter("id", orderId);
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
    public long getPaidUserOrdersQuantity(Long userId) {
        Query query = entityManager.createQuery(COUNT_USER_PAID_ORDERS);
        query.setParameter("userId", userId);
        return (long) query.getSingleResult();
    }

    @Override
    public List<Order> getPaidUserOrders(Long userId, int pageNumber, int pageSize) {
        Query query = entityManager.createQuery(SELECT_USER_PAID_ORDERS, Order.class);
        query.setParameter("userId", userId);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

}
