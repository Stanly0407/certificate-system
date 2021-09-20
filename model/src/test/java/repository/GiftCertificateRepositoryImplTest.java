package repository;


import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.repository.GiftCertificateRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

public class GiftCertificateRepositoryImplTest {

    private static final GiftCertificate TEST_GIFT_CERTIFICATE_FIRST = new GiftCertificate(1L, "TEST-first",
            "first", new BigDecimal("10.10"), 30,
            LocalDateTime.of(2021, 8, 24, 10, 10, 10),
            LocalDateTime.of(2021, 8, 24, 10, 10, 10));
    private static final GiftCertificate TEST_GIFT_CERTIFICATE_SECOND = new GiftCertificate(1L, "TEST-second",
            "second", new BigDecimal("20.20"), 15,
            LocalDateTime.of(2021, 8, 25, 10, 10, 10),
            LocalDateTime.of(2021, 8, 25, 10, 10, 10));
    private static final GiftCertificate TEST_GIFT_CERTIFICATE_THIRD = new GiftCertificate(1L, "TEST-second-third",
            "third", new BigDecimal("20.20"), 15,
            LocalDateTime.of(2021, 8, 26, 10, 10, 10),
            LocalDateTime.of(2021, 8, 26, 10, 10, 10));
    private static final String EMPTY_SORT_QUERY_PARAMETERS = "";
    private JdbcTemplate jdbcTemplate;
    private GiftCertificateRepositoryImpl giftCertificateRepository;

    @BeforeEach
    public void initDatabase() {
        // A builder that provides a convenient API for constructing an embedded database.
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        DataSource dataSource = builder
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db/schema.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        giftCertificateRepository = new GiftCertificateRepositoryImpl(jdbcTemplate);
    }

    @Test
    public void saveGiftCertificateTest() {
        LocalDateTime currentDatetime = LocalDateTime.now();
        String sqlSelectGiftCertificateById = "SELECT * FROM certificate WHERE id = ";
        GiftCertificate test = new GiftCertificate("save-test", "save", new BigDecimal("20.20"), 50);

        Long createdId = giftCertificateRepository.save(test);

        GiftCertificate actual = jdbcTemplate.query(sqlSelectGiftCertificateById + createdId,
                new BeanPropertyRowMapper<>(GiftCertificate.class)).stream().findAny().orElse(null);

        LocalDateTime createDate = actual.getCreateDate();
        LocalDateTime lastUpdateDate = actual.getLastUpdateDate();

        Assertions.assertEquals(test.getName(), actual.getName());
        Assertions.assertEquals(test.getDescription(), actual.getDescription());
        Assertions.assertEquals(test.getPrice(), actual.getPrice());
        Assertions.assertEquals(test.getDuration(), actual.getDuration());
        Assertions.assertTrue(createDate.isAfter(currentDatetime));
        Assertions.assertFalse(lastUpdateDate.isBefore(currentDatetime)); //
        Assertions.assertEquals(createDate, lastUpdateDate);
    }

    @Test
    public void updateGiftCertificateTest() {
        GiftCertificate test = new GiftCertificate(2L, "Update-first", "update", new BigDecimal("20.20"), 50);
        LocalDateTime currentDatetime = LocalDateTime.now();
        String findById = "SELECT * FROM certificate WHERE id = 2";
        giftCertificateRepository.update(test);

        GiftCertificate actual = jdbcTemplate.query(findById, new BeanPropertyRowMapper<>(GiftCertificate.class))
                .stream().findAny().orElse(null);

        LocalDateTime lastUpdateDate = actual.getLastUpdateDate();

        Assertions.assertEquals(test.getName(), actual.getName());
        Assertions.assertEquals(test.getDescription(), actual.getDescription());
        Assertions.assertEquals(test.getPrice(), actual.getPrice());
        Assertions.assertEquals(test.getDuration(), actual.getDuration());
        Assertions.assertFalse(lastUpdateDate.isBefore(currentDatetime));
    }

    @Test
    public void findByIdTest() {
        Optional<GiftCertificate> expected = Optional.of(TEST_GIFT_CERTIFICATE_FIRST);

        Optional<GiftCertificate> actual = giftCertificateRepository.findById(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findByIdTestShouldReturnEmptyResult() {
        Optional<GiftCertificate> expected = Optional.empty();

        Optional<GiftCertificate> actual = giftCertificateRepository.findById(4L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findByMatchTest() {
        List<GiftCertificate> expected = Arrays.asList(TEST_GIFT_CERTIFICATE_FIRST);
        String searchCondition = "fir";

        List<GiftCertificate> actual = giftCertificateRepository.findByMatch(EMPTY_SORT_QUERY_PARAMETERS, searchCondition);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findByMatchWithSortByNameTest() {
        List<GiftCertificate> expected = Arrays.asList(TEST_GIFT_CERTIFICATE_SECOND,
                TEST_GIFT_CERTIFICATE_THIRD);
        String query = " ORDER BY name ";
        String searchCondition = "second";

        List<GiftCertificate> actual = giftCertificateRepository.findByMatch(query, searchCondition);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findByMatchWithSortByNameInDescendingOrderTest() {
        List<GiftCertificate> expected = Arrays.asList(TEST_GIFT_CERTIFICATE_THIRD, TEST_GIFT_CERTIFICATE_SECOND);
        String query = " ORDER BY name DESC";
        String searchCondition = "second";

        List<GiftCertificate> actual = giftCertificateRepository.findByMatch(query, searchCondition);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findByMatchTestEmptyResult() {
        List<GiftCertificate> expected = new ArrayList<>();
        String searchCondition = "fourth";

        List<GiftCertificate> actual = giftCertificateRepository.findByMatch(EMPTY_SORT_QUERY_PARAMETERS, searchCondition);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findGiftCertificatesByTagTest() {
        List<GiftCertificate> expected = Arrays.asList(TEST_GIFT_CERTIFICATE_FIRST);
        String tagName = "test2";

        List<GiftCertificate> actual = giftCertificateRepository
                .findGiftCertificatesByTag(EMPTY_SORT_QUERY_PARAMETERS, tagName);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findGiftCertificatesByTagTestIfNotExist() {
        List<GiftCertificate> expected = new ArrayList<>();
        String tagName = "NotExist";

        List<GiftCertificate> actual = giftCertificateRepository
                .findGiftCertificatesByTag(EMPTY_SORT_QUERY_PARAMETERS, tagName);

        Assertions.assertEquals(expected, actual);
    }


    @Test
    public void findAllSortedByDateTest() {
        List<GiftCertificate> expected = Arrays.asList(TEST_GIFT_CERTIFICATE_FIRST,
                TEST_GIFT_CERTIFICATE_SECOND, TEST_GIFT_CERTIFICATE_THIRD);

        List<GiftCertificate> actual = giftCertificateRepository.findAllGiftCertificates(EMPTY_SORT_QUERY_PARAMETERS);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAllSortedByDateDescTest() {
        List<GiftCertificate> expected = Arrays.asList(TEST_GIFT_CERTIFICATE_THIRD,
                TEST_GIFT_CERTIFICATE_SECOND, TEST_GIFT_CERTIFICATE_FIRST);
        String query = " ORDER BY last_update_date DESC";

        List<GiftCertificate> actual = giftCertificateRepository.findAllGiftCertificates(query);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAllSortedByNameDescTest() {
        List<GiftCertificate> expected = Arrays.asList(TEST_GIFT_CERTIFICATE_THIRD,
                TEST_GIFT_CERTIFICATE_SECOND, TEST_GIFT_CERTIFICATE_FIRST);
        String query = " ORDER BY name DESC";

        List<GiftCertificate> actual = giftCertificateRepository.findAllGiftCertificates(query);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void addTagToGiftCertificateTest() {
        Tag testTag = new Tag(1L, "test");
        String selectGiftCertificateId = "SELECT c.id FROM certificate c JOIN certificate_tag ct " +
                "ON c.id=ct.certificate_id JOIN tag t ON t.id=ct.tag_id ";
        String selectTagId = "SELECT t.id FROM certificate c JOIN certificate_tag ct " +
                "ON c.id=ct.certificate_id JOIN tag t ON t.id=ct.tag_id ";
        Long expectedGiftCertificateId = 1L;
        Long expectedTagId = 1L;
        giftCertificateRepository.addTagToGiftCertificate(TEST_GIFT_CERTIFICATE_FIRST, testTag);

        GiftCertificate expectedGiftCertificate = jdbcTemplate.query(selectGiftCertificateId,
                new BeanPropertyRowMapper<>(GiftCertificate.class))
                .stream().findAny().orElse(null);
        Long actualGiftCertificateId = expectedGiftCertificate.getId();
        Tag actualTag = jdbcTemplate.query(selectTagId, new BeanPropertyRowMapper<>(Tag.class))
                .stream().findAny().orElse(null);
        Long actualTagId = actualTag.getId();

        Assertions.assertEquals(expectedGiftCertificateId, actualGiftCertificateId);
        Assertions.assertEquals(expectedTagId, actualTagId);
    }

    @Test
    public void deleteGiftCertificateTest() {
        String sql = "SELECT * FROM certificate WHERE id = 1";
        Long id = 1L;

        giftCertificateRepository.delete(id);

        GiftCertificate actual = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(GiftCertificate.class))
                .stream().findAny().orElse(null);

        Assertions.assertNull(actual);
    }

}
