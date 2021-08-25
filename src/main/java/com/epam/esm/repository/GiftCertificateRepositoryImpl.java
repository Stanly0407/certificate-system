package com.epam.esm.repository;


import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String SELECT_CERTIFICATE_BY_ID = "SELECT * FROM certificate WHERE id =?";
    private static final String SELECT_CERTIFICATES_BY_TAG = "SELECT * FROM certificate c JOIN certificate_tag ct ON c.id=ct.certificate_id JOIN tag t ON t.id=ct.tag_id WHERE t.name=?";
    private static final String SELECT_CERTIFICATES_WHERE_MATCH = "SELECT * FROM certificate WHERE MATCH (name, description) AGAINST (?)";
    private static final String SELECT_CERTIFICATES_BY_DATE = "SELECT * FROM certificate ORDER BY last_update_date";
    private static final String SELECT_CERTIFICATES_BY_DATE_DESC = "SELECT * FROM certificate ORDER BY last_update_date DESC";
    private static final String SELECT_CERTIFICATES_BY_NAME = "SELECT * FROM certificate ORDER BY name";
    private static final String SELECT_CERTIFICATES_BY_NAME_DESC = "SELECT * FROM certificate ORDER BY name DESC";
    private static final String INSERT_CERTIFICATE = "INSERT INTO certificate (name, description, price, duration) value (?, ?, ?, ?)";
    private static final String INSERT_CERTIFICATE_TAG = "INSERT INTO certificate_tag (certificate_id, tag_id) value (?, ?)";
    private static final String UPDATE_CERTIFICATE = "UPDATE certificate SET name=?, description=?, price=?, duration=?, " +
            "last_update_date=default WHERE id=?";
    private static final String DELETE_CERTIFICATE_BY_ID = "DELETE FROM certificate WHERE id=?";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void update(GiftCertificate giftCertificate) {
        jdbcTemplate.update(UPDATE_CERTIFICATE, giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getId());
    }

    public void addTagToGiftCertificate(GiftCertificate giftCertificate, Tag tag) {
        jdbcTemplate.update(INSERT_CERTIFICATE_TAG, giftCertificate.getId(), tag.getId());
    }

    public List<GiftCertificate> findGiftCertificatesByTag(String tagName) {
        return jdbcTemplate.query(SELECT_CERTIFICATES_BY_TAG, new Object[]{tagName}, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    public List<GiftCertificate> findByMatch(String searchCondition) {
        return jdbcTemplate.query(SELECT_CERTIFICATES_WHERE_MATCH, new Object[]{searchCondition}, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    public List<GiftCertificate> findAllSortedByDate() {
        return jdbcTemplate.query(SELECT_CERTIFICATES_BY_DATE, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    public List<GiftCertificate> findAllSortedByDateDesc() {
        return jdbcTemplate.query(SELECT_CERTIFICATES_BY_DATE_DESC, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    public List<GiftCertificate> findAllSortedByName() {
        return jdbcTemplate.query(SELECT_CERTIFICATES_BY_NAME, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    public List<GiftCertificate> findAllSortedByNameDesc() {
        return jdbcTemplate.query(SELECT_CERTIFICATES_BY_NAME_DESC, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public GiftCertificate findById(Long id) {
        return jdbcTemplate.query(SELECT_CERTIFICATE_BY_ID, new Object[]{id}, new BeanPropertyRowMapper<>(GiftCertificate.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public Long save(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> { PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CERTIFICATE, PreparedStatement.RETURN_GENERATED_KEYS);
                    preparedStatement.setObject(1, giftCertificate.getName());
                    preparedStatement.setObject(2, giftCertificate.getDescription());
                    preparedStatement.setObject(3, giftCertificate.getPrice());
                    preparedStatement.setObject(4, giftCertificate.getDuration());
                    return preparedStatement; },
                keyHolder);

        return  Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_CERTIFICATE_BY_ID, id);
    }

}
