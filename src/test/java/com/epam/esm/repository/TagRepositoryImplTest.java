package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TagRepositoryImplTest {


    private static JdbcTemplate jdbcTemplate;

    private TagRepositoryImpl tagRepository;

    @BeforeEach
    public void initDatabase() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        DataSource dataSource = builder
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db/schema.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        tagRepository = new TagRepositoryImpl(jdbcTemplate);
    }


    @Test
    public void findTagByNameTest() {
        Optional<Tag> expected = Optional.of(new Tag(1L, "test"));

        Optional<Tag> actual = tagRepository.findById(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findTagByNameTestShouldReturnEmptyResult() {
        Optional<Tag> expected = Optional.empty();

        Optional<Tag> actual = tagRepository.findById(3L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void deleteGiftCertificateTagsTest() {
        String sql = "SELECT * FROM tag t JOIN certificate_tag ct ON t.id=ct.tag_id WHERE certificate_id=1";
        List<Tag> expected = new ArrayList<>();
        GiftCertificate giftCertificate = new GiftCertificate(1L, "TEST-first",
                "first", new BigDecimal("10.10"), 30,
                LocalDateTime.of(2021, 8, 26, 10, 10, 10),
                LocalDateTime.of(2021, 8, 26, 10, 10, 10));
        tagRepository.deleteGiftCertificateTags(giftCertificate);

        List<Tag> actual = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tag.class));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void deleteTagTest() {
        String sql = "SELECT * FROM tag WHERE id = 1";
        Long id = 1L;

        tagRepository.delete(id);

        Tag actual = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tag.class))
                .stream().findAny().orElse(null);

        Assertions.assertNull(actual);
    }

    @Test
    public void findGiftCertificateTagsTest() {
        List<Tag> expected = Arrays.asList(new Tag(2L, "test2"));
        Long giftCertificateId = 1L;

        List<Tag> actual = tagRepository.findGiftCertificateTags(giftCertificateId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findGiftCertificateTagsTestIfNotExist() {
        List<Tag> expected = new ArrayList<>();
        Long giftCertificateId = 3L;

        List<Tag> actual = tagRepository.findGiftCertificateTags(giftCertificateId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void saveGiftCertificateTest() {
        String sqlSelectTagById = "SELECT * FROM tag WHERE id = ";
        Tag test = new Tag("save");

        Long createdId = tagRepository.save(test);

        Tag actual = jdbcTemplate.query(sqlSelectTagById + createdId,
                new BeanPropertyRowMapper<>(Tag.class)).stream().findAny().orElse(null);

        Assertions.assertEquals(test, actual);
    }

}
