package com.epam.esm.repository;

import com.epam.esm.entities.User;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@NoArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private static final String SELECT_USERS = "select u from User u";
    private static final String COUNT_USERS = "Select count(u.id) from User u";
    private static final String SELECT_USER_BY_ID = "select u from User u where u.id = :id";
    private static final String SELECT_USER_BY_LOGIN = "select u from User u where u.login = :login";
    private static final String INSERT_USER_ROLE = "insert into user_role (user_id, role_id) values (:newUserId, :roleUserId);";
    private static final String ROLE_USER_ID_ = "1";

    @PersistenceContext
    EntityManager entityManager;

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

    public Optional<User> findByLogin(String login) {
        Query query = entityManager.createQuery(SELECT_USER_BY_LOGIN, User.class);
        query.setParameter("login", login);
        List<User> users = query.getResultList();
        if (users.isEmpty()) {
            return Optional.empty();
        } else {
            User user = users.get(0);
            return Optional.of(user);
        }
    }

    public boolean existsByLogin(String login) {
        Query query = entityManager.createQuery(SELECT_USER_BY_LOGIN, User.class);
        query.setParameter("login", login);
        List<User> users = query.getResultList();
        return !users.isEmpty();
    }

    public Long save(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user.getId();
    }

    public void saveRoleUser(Long userId) {
        Query query = entityManager.createNativeQuery(INSERT_USER_ROLE);
        query.setParameter("newUserId", userId);
        query.setParameter("roleUserId", ROLE_USER_ID_);
        query.executeUpdate();
    }

}
