package com.epam.esm.repository;

import com.epam.esm.entities.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TagRepositoryImpl implements TagRepository {

    private static final String SELECT_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name= :name";
    private static final String SELECT_GIFT_CERTIFICATE_TAGS = "SELECT t FROM Tag t JOIN GiftCertificate g WHERE g.id= ?1";
    private static final String DELETE_GIFT_CERTIFICATE_TAGS = "DELETE FROM certificate_tag WHERE certificate_id= :giftCertificateId";
    private static final String SELECT_WIDELY_USED_TAG = "select t.id, t.name from tag t where t.id = " +
            "(select id from (select t.id, count(*) as used_count from tag t JOIN certificate_tag ct ON t.id=ct.tag_id \n" +
            "JOIN certificate c ON c.id=ct.certificate_id JOIN purchase_order po ON c.id=po.certificate_id \n" +
            "JOIN user u ON po.user_id=u.id WHERE u.id = (select id from(select u.id from user u where id =(SELECT id from " +
            "(SELECT u.id, SUM(order_price) AS total_cost FROM purchase_order po JOIN user u ON po.user_id=u.id " +
            "GROUP BY u.id order by total_cost desc limit 1) as res)) as r) group by t.id order by t.id  limit 1) as res)";

    @PersistenceContext
    EntityManager entityManager;

    public TagRepositoryImpl() {
    }

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

    public List<Tag> findGiftCertificateTags(Long giftCertificateId) { // todo DELETE
        return entityManager.createQuery(SELECT_GIFT_CERTIFICATE_TAGS, Tag.class).getResultList();
    }

    @Override
    public Optional<Tag> getMostWidelyUsedTagOfUserWithHighestCostOrders() {
        Tag tag = (Tag) entityManager.createNativeQuery(SELECT_WIDELY_USED_TAG, Tag.class).getSingleResult();
        return Optional.of(tag);
    }

    @Override
    public Optional<Tag> findById(Long tagId) {
        Query query = entityManager.createQuery("select t from Tag t where t.id = :id", Tag.class);
        query.setParameter("id", tagId);
        List<Tag> tags = query.getResultList();  // if use getSingleResult(); - need try/catch NoResultException
        if(tags.isEmpty()){
            return Optional.empty();
        } else {
            Tag tag = tags.get(0);
            return Optional.of(tag);
        }
     //   Tag tag = entityManager.find(Tag.class, id);
      //  return Optional.of(entityManager.find(Tag.class, id));
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
