package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.service.GiftCertificateServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest {

    private static final Long TEST_ID = 1L;
    private static final Long NEW_TAG_ID = 2L;
    private static final Tag TAG_FIRST = new Tag(1L, "tagFirst");
    private static final Tag TAG_SECOND = new Tag(2L, "tagSecond");
    private static final String FIRST_TAG_NAME = "tagFirst";
    private static final String SECOND_TAG_NAME = "tagSecond";
    private static final List<Tag> TAGS = Arrays.asList(TAG_FIRST, TAG_SECOND);
    private static final GiftCertificate GIFT_CERTIFICATE_FIRST = new GiftCertificate(1L, "name", "description",
            new BigDecimal("1.00"), 1, LocalDateTime.of(2021, 8, 26, 10, 10, 10),
            LocalDateTime.of(2021, 8, 26, 10, 10, 10));
    private static final GiftCertificate GIFT_CERTIFICATE_SECOND = new GiftCertificate(2L, "second", "description",
            new BigDecimal("1.00"), 1, LocalDateTime.of(2021, 8, 25, 10, 10, 10),
            LocalDateTime.of(2021, 8, 25, 10, 10, 10));
    private static final GiftCertificateDto GIFT_CERTIFICATE_FIRST_DTO = new GiftCertificateDto.Builder()
            .id(TEST_ID).name("name").description("description").duration(1).price(new BigDecimal("1.00"))
            .createDate(LocalDateTime.of(2021, 8, 26, 10, 10, 10))
            .lastUpdateDate(LocalDateTime.of(2021, 8, 26, 10, 10, 10))
            .tags(TAGS)
            .build();
    private static final GiftCertificateDto GIFT_CERTIFICATE_SECOND_DTO = new GiftCertificateDto.Builder()
            .id(2L).name("second").description("description").duration(1).price(new BigDecimal("1.00"))
            .createDate(LocalDateTime.of(2021, 8, 25, 10, 10, 10))
            .lastUpdateDate(LocalDateTime.of(2021, 8, 25, 10, 10, 10))
            .tags(TAGS)
            .build();

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(giftCertificateRepository, tagRepository);
    }

    @Test
    public void findByIdTestShouldReturnGiftCertificateDto() {
        Optional<GiftCertificate> giftCertificate = Optional.of(GIFT_CERTIFICATE_FIRST);
        List<Tag> tags = Arrays.asList(TAG_FIRST, TAG_SECOND);
        when(giftCertificateRepository.findById(TEST_ID)).thenReturn(giftCertificate);
        when(tagRepository.findGiftCertificateTags(TEST_ID)).thenReturn(tags);

        Optional<GiftCertificateDto> actualOptional = giftCertificateService.findById(TEST_ID);
        GiftCertificateDto actual = actualOptional.get();

        Assertions.assertEquals(GIFT_CERTIFICATE_FIRST_DTO, actual);

        Mockito.verify(giftCertificateRepository).findById(TEST_ID);
        Mockito.verify(tagRepository).findGiftCertificateTags(TEST_ID);
    }

    @Test
    public void saveNewGiftCertificateTest() {
        List<Tag> tags = Arrays.asList(TAG_FIRST, TAG_SECOND);
        Optional<Tag> tagFirstOptional = Optional.of(TAG_FIRST);
        Optional<Tag> tagSecondOptional = Optional.of(TAG_SECOND);
        when(giftCertificateRepository.save(GIFT_CERTIFICATE_FIRST)).thenReturn(TEST_ID);
        when(giftCertificateRepository.findById(TEST_ID)).thenReturn(Optional.of(GIFT_CERTIFICATE_FIRST));
        when(tagRepository.findTagByName(FIRST_TAG_NAME)).thenReturn(tagFirstOptional);
        when(tagRepository.findTagByName(SECOND_TAG_NAME)).thenReturn(Optional.empty());
        when(tagRepository.save(TAG_SECOND)).thenReturn(NEW_TAG_ID);
        when(tagRepository.findById(NEW_TAG_ID)).thenReturn(tagSecondOptional);
        doNothing().when(giftCertificateRepository).addTagToGiftCertificate(GIFT_CERTIFICATE_FIRST, TAG_FIRST);
        doNothing().when(giftCertificateRepository).addTagToGiftCertificate(GIFT_CERTIFICATE_FIRST, TAG_SECOND);

        giftCertificateService.saveNewGiftCertificate(GIFT_CERTIFICATE_FIRST, tags);

        Mockito.verify(giftCertificateRepository).save(any(GiftCertificate.class));
        Mockito.verify(giftCertificateRepository).findById(TEST_ID);
        Mockito.verify(giftCertificateRepository, times(2))
                .addTagToGiftCertificate(any(GiftCertificate.class), any(Tag.class));
        Mockito.verify(tagRepository, times(3)).findTagByName(anyString());
        Mockito.verify(tagRepository).save(any(Tag.class));
    }

    @Test
    public void updateGiftCertificateTest() {
        List<Tag> tags = Arrays.asList(TAG_FIRST, TAG_SECOND);
        Optional<Tag> tagFirstOptional = Optional.of(TAG_FIRST);
        Optional<Tag> tagSecondOptional = Optional.of(TAG_SECOND);
        doNothing().when(giftCertificateRepository).update(GIFT_CERTIFICATE_FIRST);
        doNothing().when(tagRepository).deleteGiftCertificateTags(GIFT_CERTIFICATE_FIRST);
        when(tagRepository.findTagByName(FIRST_TAG_NAME)).thenReturn(tagFirstOptional);
        when(tagRepository.findTagByName(SECOND_TAG_NAME)).thenReturn(Optional.empty()).thenReturn(tagSecondOptional);
        when(tagRepository.save(TAG_SECOND)).thenReturn(NEW_TAG_ID);
        doNothing().when(giftCertificateRepository).addTagToGiftCertificate(GIFT_CERTIFICATE_FIRST, TAG_FIRST);
        doNothing().when(giftCertificateRepository).addTagToGiftCertificate(GIFT_CERTIFICATE_FIRST, TAG_SECOND);

        giftCertificateService.updateGiftCertificate(GIFT_CERTIFICATE_FIRST, tags);

        verify(giftCertificateRepository).update(any(GiftCertificate.class));
        verify(tagRepository).deleteGiftCertificateTags(any(GiftCertificate.class));
        verify(tagRepository, times(1)).save(any(Tag.class));
        verify(tagRepository, times(4)).findTagByName(anyString());
        verify(giftCertificateRepository, times(2))
                .addTagToGiftCertificate(any(GiftCertificate.class), any(Tag.class));
    }

    @Test
    public void findGiftCertificatesByTagTestShouldReturnGiftCertificateDtoList() {
        String givenTagName = "test";
        List<GiftCertificate> expectedGiftCertificates = Arrays.asList(GIFT_CERTIFICATE_FIRST, GIFT_CERTIFICATE_SECOND);
        List<Tag> tagsFirstCertificate = Arrays.asList(TAG_FIRST, TAG_SECOND);
        List<Tag> tagsSecondCertificate = Arrays.asList(TAG_FIRST, TAG_SECOND);
        List<GiftCertificateDto> expected = Arrays.asList(GIFT_CERTIFICATE_FIRST_DTO, GIFT_CERTIFICATE_SECOND_DTO);
        when(giftCertificateRepository.findGiftCertificatesByTag(givenTagName)).thenReturn(expectedGiftCertificates);
        when(tagRepository.findGiftCertificateTags(TEST_ID)).thenReturn(tagsFirstCertificate);
        when(tagRepository.findGiftCertificateTags(2L)).thenReturn(tagsSecondCertificate);

        List<GiftCertificateDto> actual = giftCertificateService.findGiftCertificatesByTag(givenTagName);

        Assertions.assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).findGiftCertificatesByTag(anyString());
        Mockito.verify(tagRepository, times(2)).findGiftCertificateTags(anyLong());
    }

    @Test
    public void getGiftCertificatesSortedByConditionTestSortByDate() {
        String sortCondition = "date";
        List<GiftCertificate> expectedGiftCertificates = Arrays.asList(GIFT_CERTIFICATE_FIRST, GIFT_CERTIFICATE_SECOND);
        List<Tag> tags = Arrays.asList(TAG_FIRST, TAG_SECOND);
        List<GiftCertificateDto> expected = Arrays.asList(GIFT_CERTIFICATE_FIRST_DTO, GIFT_CERTIFICATE_SECOND_DTO);
        when(giftCertificateRepository.findAllSortedByDate()).thenReturn(expectedGiftCertificates);
        when(tagRepository.findGiftCertificateTags(TEST_ID)).thenReturn(tags);
        when(tagRepository.findGiftCertificateTags(2L)).thenReturn(tags);

        List<GiftCertificateDto> actual = giftCertificateService.getGiftCertificatesSortedByCondition(sortCondition);

        Assertions.assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).findAllSortedByDate();
        Mockito.verify(tagRepository, times(2)).findGiftCertificateTags(anyLong());
    }

    @Test
    public void getGiftCertificatesSortedByConditionTestSortByNameDesc() {
        String sortCondition = "name-desc";
        List<GiftCertificate> expectedGiftCertificates = Arrays.asList(GIFT_CERTIFICATE_FIRST, GIFT_CERTIFICATE_SECOND);
        List<Tag> tags = Arrays.asList(TAG_FIRST, TAG_SECOND);
        List<GiftCertificateDto> expected = Arrays.asList(GIFT_CERTIFICATE_FIRST_DTO, GIFT_CERTIFICATE_SECOND_DTO);
        when(giftCertificateRepository.findAllSortedByNameDesc()).thenReturn(expectedGiftCertificates);
        when(tagRepository.findGiftCertificateTags(TEST_ID)).thenReturn(tags);
        when(tagRepository.findGiftCertificateTags(2L)).thenReturn(tags);

        List<GiftCertificateDto> actual = giftCertificateService.getGiftCertificatesSortedByCondition(sortCondition);

        Assertions.assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).findAllSortedByNameDesc();
        Mockito.verify(tagRepository, times(2)).findGiftCertificateTags(anyLong());
    }

    @Test
    public void getGiftCertificatesSortedByConditionTestSortByDateDesc() {
        String sortCondition = "date-desc";
        List<GiftCertificate> expectedGiftCertificates = Arrays.asList(GIFT_CERTIFICATE_SECOND, GIFT_CERTIFICATE_FIRST);
        List<Tag> tags = Arrays.asList(TAG_FIRST, TAG_SECOND);
        List<GiftCertificateDto> expected = Arrays.asList(GIFT_CERTIFICATE_SECOND_DTO, GIFT_CERTIFICATE_FIRST_DTO);
        when(giftCertificateRepository.findAllSortedByDateDesc()).thenReturn(expectedGiftCertificates);
        when(tagRepository.findGiftCertificateTags(TEST_ID)).thenReturn(tags);
        when(tagRepository.findGiftCertificateTags(2L)).thenReturn(tags);

        List<GiftCertificateDto> actual = giftCertificateService.getGiftCertificatesSortedByCondition(sortCondition);

        Assertions.assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).findAllSortedByDateDesc();
        Mockito.verify(tagRepository, times(2)).findGiftCertificateTags(anyLong());
    }

    @Test
    public void findGiftCertificatesByNameOrDescriptionTest() {
        String givenSearchCondition = "second";
        List<GiftCertificate> expectedGiftCertificates = Arrays.asList(GIFT_CERTIFICATE_SECOND);
        List<Tag> tagsSecondCertificate = Arrays.asList(TAG_FIRST, TAG_SECOND);
        List<GiftCertificateDto> expected = Arrays.asList(GIFT_CERTIFICATE_SECOND_DTO);
        when(giftCertificateRepository.findByMatch(givenSearchCondition)).thenReturn(expectedGiftCertificates);
        when(tagRepository.findGiftCertificateTags(2L)).thenReturn(tagsSecondCertificate);

        List<GiftCertificateDto> actual = giftCertificateService.findGiftCertificatesByNameOrDescription(givenSearchCondition);

        Assertions.assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).findByMatch(anyString());
        Mockito.verify(tagRepository).findGiftCertificateTags(anyLong());
    }

}
