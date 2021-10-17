package com.epam.esm.repository;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@NoArgsConstructor
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String SORT_BY_NAME = "name";
    private static final String SORT_BY_DATE = "date";
    private static final String SORT_ORDER_DESC = "desc";
    private static final String SEARCH_BY_TAG = "BY_TAG";
    private static final String SEARCH_BY_TAGS = "BY_SEVERAL_TAGS";
    private static final String SEARCH_MATCH = "MATCH";
    private static final String INSERT_CERTIFICATE_TAG = "insert into certificate_tag (certificate_id, tag_id) " +
            "values ( :giftCertificateId, :tagId );";
    private static final String UPDATE_CERTIFICATE = "UPDATE GiftCertificate c SET c.name= :name, c.description= :description, " +
            "c.price= :price, c.duration= :duration, c.lastUpdateDate= current_timestamp WHERE c.id= :id";
    private static final String SELECT_FROM_CERTIFICATES = "SELECT c FROM GiftCertificate c ";
    private static final String SELECT_COUNT_CERTIFICATES = "SELECT count(c) FROM GiftCertificate c ";
    private static final String SELECT_BY_TAG = " JOIN c.tags t WHERE t.name= :tagName ";
    private static final String SELECT_BY_SEVERAL_TAGS = " JOIN c.tags t where t.name in (:tags) group by c.id having COUNT(DISTINCT t.name) = :tagsCount";
    private static final String SELECT_WHERE_MATCH = " WHERE c.name like :searchCondition OR c.description LIKE :searchCondition";
    private static final String SELECT_BY_ID = "SELECT c FROM GiftCertificate c WHERE c.id = :id";
    private static final String PARTIAL_UPDATE_CERTIFICATE_FIRST_PART = "UPDATE certificate c SET c.";
    private static final String PARTIAL_UPDATE_CERTIFICATE_SECOND_PART = "= :updatedField WHERE c.id= :id";

    @PersistenceContext
    EntityManager entityManager;

    public void addTagToGiftCertificate(GiftCertificate giftCertificate, Tag tag) {
        Query query = entityManager.createNativeQuery(INSERT_CERTIFICATE_TAG);
        query.setParameter("giftCertificateId", giftCertificate.getId());
        query.setParameter("tagId", tag.getId());
        query.executeUpdate();
    }

    public List<GiftCertificate> findByMatch(List<String> sortParams, String order, String searchCondition, int pageNumber, int pageSize) {
        String sortQueryPart = getSortQueryPart(sortParams, order);
        Query query = entityManager.createQuery(SELECT_FROM_CERTIFICATES + SELECT_WHERE_MATCH + sortQueryPart, GiftCertificate.class);
        //This sign "%" means any number of characters or no characters at the beginning and at the end of the search condition.
        String condition = "%" + searchCondition + "%";
        query.setParameter("searchCondition", condition);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    public List<GiftCertificate> findAllGiftCertificates(List<String> sortParams, String order, int pageNumber, int pageSize) {
        String sortQueryPart = getSortQueryPart(sortParams, order);
        Query query = entityManager.createQuery(SELECT_FROM_CERTIFICATES + sortQueryPart, GiftCertificate.class);

        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    public List<GiftCertificate> findGiftCertificatesByTag(List<String> sortParams, String order, String tagName,
                                                           int pageNumber, int pageSize) {
        String sortQueryPart = getSortQueryPart(sortParams, order);
        String queryString = SELECT_FROM_CERTIFICATES + SELECT_BY_TAG + sortQueryPart;
        Query query = entityManager.createQuery(queryString, GiftCertificate.class);
        query.setParameter("tagName", tagName);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public List<GiftCertificate> findGiftCertificateBySeveralTags(List<String> sortParams, String order, List<String> tags,
                                                                  int pageNumber, int pageSize) {
        String sortQueryPart = getSortQueryPart(sortParams, order);
        long tagsCount = tags.size();
        String queryString = SELECT_FROM_CERTIFICATES + SELECT_BY_SEVERAL_TAGS + sortQueryPart;
        Query query = entityManager.createQuery(queryString, GiftCertificate.class);
        query.setParameter("tags", tags);
        query.setParameter("tagsCount", tagsCount);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public long countGiftCertificateSelect(String type, List<String> tags, String searchCondition, String tagName) {
        String selectQuery = SELECT_COUNT_CERTIFICATES;
        if (SEARCH_BY_TAGS.equals(type)) {
            selectQuery = SELECT_COUNT_CERTIFICATES + SELECT_BY_SEVERAL_TAGS;
        } else if (SEARCH_BY_TAG.equals(type)) {
            selectQuery = SELECT_COUNT_CERTIFICATES + SELECT_BY_TAG;
        } else if (SEARCH_MATCH.equals(type)) {
            selectQuery = SELECT_COUNT_CERTIFICATES + SELECT_WHERE_MATCH;
        }
        Query query = entityManager.createQuery(selectQuery);

        if (searchCondition != null) {
            String condition = "%" + searchCondition + "%";
            query.setParameter("searchCondition", condition);
        } else if (tagName != null) {
            query.setParameter("tagName", tagName);
        } else if (tags != null) {
            query.setParameter("tags", tags);
            long tagsCount = tags.size();
            query.setParameter("tagsCount", tagsCount);
        }
        return (long) query.getSingleResult();
    }

    public Optional<GiftCertificate> findById(Long giftRepositoryId) {
        Query query = entityManager.createQuery(SELECT_BY_ID, GiftCertificate.class);
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

    @Override
    public void partialGiftCertificateUpdate(String parameterName, String parameter, Long giftCertificateId) {
        String UPDATE_CERTIFICATE = PARTIAL_UPDATE_CERTIFICATE_FIRST_PART + parameterName + PARTIAL_UPDATE_CERTIFICATE_SECOND_PART;
        Query query = entityManager.createNativeQuery(UPDATE_CERTIFICATE);
        query.setParameter("updatedField", parameter);
        query.setParameter("id", giftCertificateId);
        query.executeUpdate();
    }

    public Long save(GiftCertificate giftCertificate) {
        entityManager.persist(giftCertificate);
        entityManager.flush();
        return giftCertificate.getId();
    }

    public void update(GiftCertificate giftCertificate) {
        Query query = entityManager.createQuery(UPDATE_CERTIFICATE);
        query.setParameter("name", giftCertificate.getName());
        query.setParameter("description", giftCertificate.getDescription());
        query.setParameter("price", giftCertificate.getPrice());
        query.setParameter("duration", giftCertificate.getDuration());
        query.setParameter("id", giftCertificate.getId());
        query.executeUpdate();
    }

    public void delete(Long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        entityManager.remove(giftCertificate);
    }

    private String getSortQueryPart(List<String> sortParams, String order) {
        StringBuilder sortQueryPart = new StringBuilder();
        if (sortParams != null) {
            if (sortParams.contains(SORT_BY_NAME)) {
                sortQueryPart.append(" order by c.name");
            } else if (sortParams.size() >= 2) {
                sortQueryPart.append(" and ");
            } else if (sortParams.contains(SORT_BY_DATE)) {
                sortQueryPart.append(" order by c.lastUpdateDate ");
            }
            if (SORT_ORDER_DESC.equals(order)) {
                sortQueryPart.append(" DESC ");
            }
        }
        return new String(sortQueryPart);
    }

}
