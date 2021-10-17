package com.epam.esm.repository;

import com.epam.esm.entities.Tag;
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
public class TagRepositoryImpl implements TagRepository {

    private static final String SELECT_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name= :name";
    private static final String DELETE_GIFT_CERTIFICATE_TAGS = "DELETE FROM certificate_tag WHERE certificate_id= :giftCertificateId";
    private static final String SELECT_WIDELY_USED_TAG = " select id, name, used_count, rn_used_count from\n" +
            "(select t.id, t.name, count(*) as used_count, dense_rank() over (order by count(*) desc) AS rn_used_count from tag t \n" +
            " JOIN certificate_tag ct ON t.id=ct.tag_id JOIN certificate c ON c.id=ct.certificate_id JOIN purchase_order po " +
            "ON c.id=po.certificate_id JOIN (SELECT u.id, SUM(order_price), dense_rank() over (order by SUM(order_price) desc) " +
            "AS rn_total_cost FROM purchase_order po JOIN user u ON po.user_id=u.id GROUP BY u.id) as res ON po.user_id=res.id \n" +
            "where res.rn_total_cost = 1 group by t.id , t.name) as sub_t where rn_used_count = 1;";

    @PersistenceContext
    EntityManager entityManager;

    public Optional<Tag> findTagByName(String name) {
        Query query = entityManager.createQuery(SELECT_TAG_BY_NAME, Tag.class);
        query.setParameter("name", name);
        List<Tag> tag = query.getResultList();
        if (tag.size() > 0) {
            return Optional.of(tag.get(0));
        } else {
            return Optional.empty();
        }
    }

    public void deleteGiftCertificateTags(Long id) {
        Query query = entityManager.createNativeQuery(DELETE_GIFT_CERTIFICATE_TAGS);
        query.setParameter("giftCertificateId", id);
        query.executeUpdate();
    }

    @Override
    public List<Tag> getMostWidelyUsedTagOfUserWithHighestCostOrders() {
        return entityManager.createNativeQuery(SELECT_WIDELY_USED_TAG, Tag.class).getResultList();
    }

    @Override
    public Optional<Tag> findById(Long tagId) {
        Query query = entityManager.createQuery("select t from Tag t where t.id = :id", Tag.class);
        query.setParameter("id", tagId);
        List<Tag> tags = query.getResultList();  // if use getSingleResult(); - need try/catch NoResultException
        if (tags.isEmpty()) {
            return Optional.empty();
        } else {
            Tag tag = tags.get(0);
            return Optional.of(tag);
        }
    }

    @Override
    public Long save(Tag tag) {
        entityManager.persist(tag);
        entityManager.flush();
        return tag.getId();
    }

    @Override
    public void delete(Tag tag) {
        entityManager.remove(tag);
    }

}
