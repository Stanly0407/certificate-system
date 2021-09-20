package com.epam.esm.repository;

import com.epam.esm.entities.Tag;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private static final String SELECT_TAG_BY_NAME = "SELECT * FROM tag WHERE name=?";
    private static final String SELECT_TAG_BY_ID = "SELECT * FROM tag WHERE id=?";
    private static final String SELECT_GIFT_CERTIFICATE_TAGS = "SELECT * FROM tag t JOIN certificate_tag ct ON t.id=ct.tag_id WHERE certificate_id=?";
    private static final String INSERT_TAG = "INSERT INTO tag (name) values (?)";
    private static final String DELETE_TAG_BY_ID = "DELETE FROM tag WHERE id=?";
    private static final String DELETE_GIFT_CERTIFICATE_TAGS = "DELETE FROM certificate_tag WHERE certificate_id=?";


    private final JdbcTemplate jdbcTemplate;

    public TagRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Tag> findTagByName(String name) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TAG_BY_NAME);
            preparedStatement.setObject(1, name);
            return preparedStatement;
        }, new BeanPropertyRowMapper<>(Tag.class))
                .stream().findAny();
    }

    public void deleteGiftCertificateTags(Long id) {
        jdbcTemplate.update(DELETE_GIFT_CERTIFICATE_TAGS, id);
    }

    public List<Tag> findGiftCertificateTags(Long giftCertificateId) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GIFT_CERTIFICATE_TAGS);
            preparedStatement.setObject(1, giftCertificateId);
            return preparedStatement;
        }, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TAG_BY_ID);
            preparedStatement.setObject(1, id);
            return preparedStatement;
        }, new BeanPropertyRowMapper<>(Tag.class))
                .stream().findAny();
    }

    @Override
    public Long save(String tagName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TAG,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, tagName);
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_TAG_BY_ID, id);
    }

}
