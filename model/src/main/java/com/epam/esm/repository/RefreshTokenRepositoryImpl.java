package com.epam.esm.repository;

import com.epam.esm.entities.RefreshToken;
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
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private static final String SELECT_REFRESH_TOKEN_BY_ID = "select rt from RefreshToken rt where rt.id = :id";
    private static final String SELECT_REFRESH_TOKEN_BY_TOKEN = "select rt from RefreshToken rt where rt.token = :token";

    @PersistenceContext
    EntityManager entityManager;

    public Optional<RefreshToken> findById(Long id) {
        Query query = entityManager.createQuery(SELECT_REFRESH_TOKEN_BY_ID, RefreshToken.class);
        query.setParameter("id", id);
        List<RefreshToken> tokens = query.getResultList();  // if use getSingleResult(); - need try/catch NoResultException
        if (tokens.isEmpty()) {
            return Optional.empty();
        } else {
            RefreshToken token = tokens.get(0);
            return Optional.of(token);
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        Query query = entityManager.createQuery(SELECT_REFRESH_TOKEN_BY_TOKEN, RefreshToken.class);
        query.setParameter("token", token);
        List<RefreshToken> tokens = query.getResultList();  // if use getSingleResult(); - need try/catch NoResultException
        if (tokens.isEmpty()) {
            return Optional.empty();
        } else {
            RefreshToken refreshToken = tokens.get(0);
            return Optional.of(refreshToken);
        }
    }

    public RefreshToken save(RefreshToken refreshToken) {
        entityManager.persist(refreshToken);
        entityManager.flush();
        return refreshToken;
    }

    public void delete(RefreshToken refreshToken) {
        entityManager.remove(refreshToken);
    }

}
