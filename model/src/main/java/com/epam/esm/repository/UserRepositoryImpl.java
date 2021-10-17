package com.epam.esm.repository;

import com.epam.esm.entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    private static final String SELECT_USERS = "select u from User u";
    private static final String COUNT_USERS = "Select count(u.id) from User u";
    private static final String SELECT_USER_BY_ID = "select u from User u where u.id = :id";

    @PersistenceContext
    EntityManager entityManager;

    public UserRepositoryImpl() {
    }

    public Optional<User> getById(Long userId) {
        Query query = entityManager.createQuery(SELECT_USER_BY_ID, User.class);
        query.setParameter("id", userId);
        List<User> users = query.getResultList();  // if use getSingleResult(); - need try/catch NoResultException
        if (users.isEmpty()) {
            return Optional.empty();
        } else {
            User user = users.get(0);
            return Optional.of(user);
        }
    }

    public List<User> getAllUsers(int pageNumber, int pageSize) {
        Query query = entityManager.createQuery(SELECT_USERS, User.class);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public long getUsersCommonQuantity() {
        Query queryTotal = entityManager.createQuery(COUNT_USERS);
        return (long) queryTotal.getSingleResult();
    }



}
