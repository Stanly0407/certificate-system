package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exceptions.ResourceNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.GiftCertificateRepositoryImpl;
import com.epam.esm.repository.TagRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

//@ExtendWith(SpringExtension.class)
public class GiftCertificateServiceTest {

    private static GiftCertificate mockGiftCertificate;
    private static Tag mockTagSecond;
    private static Tag mockTagFirst;
    private static List<Tag> tags;
    private static final Long MOCK_ID = 1L;
    private static GiftCertificateService giftCertificateService;
    private static GiftCertificateRepositoryImpl giftCertificateRepository;
    private static TagRepositoryImpl tagRepository;

    @BeforeAll
    public static void init() {
        giftCertificateRepository = Mockito.mock(GiftCertificateRepositoryImpl.class);
        tagRepository = Mockito.mock(TagRepositoryImpl.class);
        giftCertificateService = new GiftCertificateService(giftCertificateRepository, tagRepository);
        mockGiftCertificate = new GiftCertificate(1L, "name", "description", new BigDecimal("1.00"), 1, LocalDateTime.of(2021, 8, 26, 10, 10, 10), LocalDateTime.of(2021, 8, 26, 10, 10, 10));
        mockTagFirst = new Tag(1L, "mockTagFirst");
        mockTagSecond = new Tag(2L, "mockTagSecond");
        tags = new ArrayList<>();
        tags.add(mockTagFirst);
        tags.add(mockTagSecond);
        when(giftCertificateRepository.findById(MOCK_ID)).thenReturn(mockGiftCertificate);
        when(tagRepository.findGiftCertificateTags(MOCK_ID)).thenReturn(tags);
    }

    @Test
    public void findByIdTestShouldReturnGiftCertificateDto() throws ResourceNotFoundException {
        GiftCertificateDto expected = new GiftCertificateDto.Builder()
                .id(MOCK_ID)
                .name("name")
                .description("description")
                .duration(1)
                .price(new BigDecimal("1.00"))
                .createDate(LocalDateTime.of(2021, 8, 26, 10, 10, 10))
                .lastUpdateDate(LocalDateTime.of(2021, 8, 26, 10, 10, 10))
                .tags(tags)
                .build();
        GiftCertificateDto actual = giftCertificateService.findById(MOCK_ID);
        Assertions.assertEquals(expected, actual);
    }

}
