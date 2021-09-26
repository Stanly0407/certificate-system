package com.epam.esm.repository;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String SELECT_CERTIFICATES = "SELECT c FROM GiftCertificate c ";
    private static final String SELECT_CERTIFICATES_BY_TAG = "SELECT c FROM GiftCertificate c " +
            "JOIN c.tags t WHERE t.name= :tagName ";
    private static final String SELECT_CERTIFICATES_WHERE_MATCH = "SELECT c FROM GiftCertificate c " +
            "WHERE c.name like :searchCondition OR c.description LIKE :searchCondition";
    private static final String INSERT_CERTIFICATE_TAG = "insert into certificate_tag (certificate_id, tag_id) " +
            "values ( :giftCertificateId, :tagId );";
    private static final String UPDATE_CERTIFICATE = "UPDATE GiftCertificate c SET c.name= :name, c.description= :description, " +
            "c.price= :price, c.duration= :duration, c.lastUpdateDate= current_timestamp WHERE c.id= :id";
    private static final String SELECT_CERTIFICATES_BY_SEVERAL_TAGS = "SELECT c FROM GiftCertificate c " +
            "JOIN c.tags t where t.name in (:tags) group by c.id having COUNT(DISTINCT t.name) = :tagsCount";


    @PersistenceContext
    EntityManager entityManager;

    public GiftCertificateRepositoryImpl() {
    }

    @Modifying
    public void addTagToGiftCertificate(GiftCertificate giftCertificate, Tag tag) {
        Query query = entityManager.createNativeQuery(INSERT_CERTIFICATE_TAG);
        query.setParameter("giftCertificateId", giftCertificate.getId());
        query.setParameter("tagId", tag.getId());
        query.executeUpdate();
    }

    public List<GiftCertificate> findByMatch(String queryPart, String searchCondition, int pageNumber, int pageSize) {
        //This sign "%" means any number of characters or no characters at the beginning and at the end of the search condition.
        String condition = "%" + searchCondition + "%";
        Query query = entityManager.createQuery(SELECT_CERTIFICATES_WHERE_MATCH + queryPart, GiftCertificate.class);
        query.setParameter("searchCondition", condition);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }


    public List<GiftCertificate> findAllGiftCertificates(String queryPart, int pageNumber, int pageSize) {
        Query query = entityManager.createQuery(SELECT_CERTIFICATES + queryPart, GiftCertificate.class);
        if (pageNumber != 0 && pageSize != 0) {
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
        }
        return query.getResultList();
    }

    @Override
    public void partialGiftCertificateUpdate(String parameterName, String parameter, Long giftCertificateId) {
        String UPDATE_CERTIFICATE = "UPDATE certificate c SET c." + parameterName + "= :updatedField WHERE c.id= :id";
        Query query = entityManager.createNativeQuery(UPDATE_CERTIFICATE);
        query.setParameter("updatedField", parameter);
        query.setParameter("id", giftCertificateId);
        query.executeUpdate();
    }

    public List<GiftCertificate> findGiftCertificatesByTag(String queryPart, String tagName, int pageNumber, int pageSize) {
        String queryString = SELECT_CERTIFICATES_BY_TAG + queryPart;
        Query query = entityManager.createQuery(queryString, GiftCertificate.class);
        query.setParameter("tagName", tagName);
        if (pageNumber != 0 && pageSize != 0) {
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
        }
        return query.getResultList();
    }

    public List<GiftCertificate> findGiftCertificateBySeveralTags(String queryPart, List<String> tags, int pageNumber, int pageSize) {
        long tagsCount = tags.size();
        String queryString = SELECT_CERTIFICATES_BY_SEVERAL_TAGS + queryPart;
        Query query = entityManager.createQuery(queryString, GiftCertificate.class);
        query.setParameter("tags", tags);
        query.setParameter("tagsCount", tagsCount);
        if (pageNumber != 0 && pageSize != 0) {
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
        }
        return query.getResultList();

    }

    public Long save(GiftCertificate giftCertificate) {
        entityManager.persist(giftCertificate);
        entityManager.flush();
        return giftCertificate.getId();
    }

    public Optional<GiftCertificate> findById(Long giftRepositoryId) {
        Query query = entityManager.createQuery("select c from GiftCertificate c where c.id = :id", GiftCertificate.class);
        query.setParameter("id", giftRepositoryId);
        // if use getSingleResult(); - need try/catch NoResultException
        List<GiftCertificate> giftCertificates = query.getResultList();
        if (giftCertificates.isEmpty()) {
            return Optional.empty();
        } else {
            GiftCertificate giftCertificate = giftCertificates.get(0);
            return Optional.of(giftCertificate);
        }
    }

    public void update(GiftCertificate giftCertificate) {
        Query query = entityManager.createQuery(UPDATE_CERTIFICATE);
        query.setParameter("name", giftCertificate.getName());
        query.setParameter("description", giftCertificate.getDescription());
        query.setParameter("price", giftCertificate.getPrice());
        query.setParameter("duration", giftCertificate.getDuration());
        query.setParameter("id", giftCertificate.getId()); // todo change
        query.executeUpdate();
    }

    public void delete(Long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        entityManager.remove(giftCertificate);
    }

    public long getGiftCertificateCommonQuantity() {
        Query queryTotal = entityManager.createQuery("Select count(c.id) from GiftCertificate c"); //todo const
        return (long) queryTotal.getSingleResult();
    }


}
