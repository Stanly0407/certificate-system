package com.epam.esm.repository;

import com.epam.esm.domain.entities.GiftCertificate;
import com.epam.esm.domain.entities.Tag;
import com.epam.esm.domain.repository.GiftCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String SELECT_CERTIFICATE_BY_ID = "SELECT * FROM certificate WHERE id =?";
    private static final String SELECT_CERTIFICATES_BY_TAG = "SELECT c.id, c.name, c.description, c.price, c.duration, " +
            "c.create_date, c.last_update_date FROM certificate c JOIN certificate_tag ct ON c.id=ct.certificate_id " +
            "JOIN tag t ON t.id=ct.tag_id WHERE t.name=?";
    private static final String SELECT_CERTIFICATES_WHERE_MATCH = "SELECT * FROM certificate WHERE name like ? OR description LIKE ?";
    private static final String SELECT_CERTIFICATES_BY_DATE = "SELECT * FROM certificate ORDER BY last_update_date";
    private static final String SELECT_CERTIFICATES_BY_DATE_DESC = "SELECT * FROM certificate ORDER BY last_update_date DESC";
    private static final String SELECT_CERTIFICATES_BY_NAME = "SELECT * FROM certificate ORDER BY name";
    private static final String SELECT_CERTIFICATES_BY_NAME_DESC = "SELECT * FROM certificate ORDER BY name DESC";
    private static final String INSERT_CERTIFICATE = "INSERT INTO certificate (name, description, price, duration, create_date, " +
            "last_update_date) values (?, ?, ?, ?, default, default)";
    private static final String INSERT_CERTIFICATE_TAG = "INSERT INTO certificate_tag (certificate_id, tag_id) values (?, ?)";
    private static final String UPDATE_CERTIFICATE = "UPDATE certificate SET name=?, description=?, price=?, duration=?, " +
            "last_update_date=default WHERE id=?";
    private static final String DELETE_CERTIFICATE_BY_ID = "DELETE FROM certificate WHERE id=?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addTagToGiftCertificate(GiftCertificate giftCertificate, Tag tag) {
        jdbcTemplate.update(INSERT_CERTIFICATE_TAG, giftCertificate.getId(), tag.getId());
    }

    public List<GiftCertificate> findGiftCertificatesByTag(String tagName) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CERTIFICATES_BY_TAG);
            preparedStatement.setObject(1, tagName);
            return preparedStatement;
        }, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    public List<GiftCertificate> findByMatch(String searchCondition) {
        String condition = "%" + searchCondition + "%";
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CERTIFICATES_WHERE_MATCH);
            preparedStatement.setObject(1, condition);
            preparedStatement.setObject(2, condition);
            return preparedStatement;
        }, new BeanPropertyRowMapper<>(GiftCertificate.class));
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

    public Long save(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreatorFactory statementCreatorFactory = new PreparedStatementCreatorFactory(INSERT_CERTIFICATE,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER);
        statementCreatorFactory.setReturnGeneratedKeys(true);
        statementCreatorFactory.setGeneratedKeysColumnNames("id");
        PreparedStatementCreator preparedStatementCreator = statementCreatorFactory.newPreparedStatementCreator(
                new Object[]{giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(),
                        giftCertificate.getDuration()});
        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<GiftCertificate> findById(Long id) {
        return jdbcTemplate.query(SELECT_CERTIFICATE_BY_ID, new Object[]{id}, new BeanPropertyRowMapper<>(GiftCertificate.class))
                .stream().findAny();
    }

    public void update(GiftCertificate giftCertificate) {
        jdbcTemplate.update(UPDATE_CERTIFICATE, giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getId());
    }

    public void delete(Long id) {
        jdbcTemplate.update(DELETE_CERTIFICATE_BY_ID, id);
    }

}
