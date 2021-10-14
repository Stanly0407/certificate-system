package com.epam.esm.services;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.forms.GiftCertificateTagsWrapper;
import com.epam.esm.services.service.GiftCertificateServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest {

    private static final Long TEST_ID = 1L;
    private static final Long NEW_TAG_ID = 2L;
    private static final Tag DEFAULT_TAG = new Tag(1L, "test", new ArrayList<>());
    private static final Tag DEFAULT_TAG_SECOND = new Tag(2L, "rest", new ArrayList<>());
    private static final Tag TAG_FIRST = new Tag(1L, "tagFirst", new ArrayList<>());
    private static final Tag TAG_SECOND = Tag.builder().name("tagSecond").build();
    private static final String NULL_STRING_PARAM = null;
    private static final List<String> EMPTY_LIST = null;
    private static final String FIRST_TAG_NAME = "tagFirst";
    private static final String SECOND_TAG_NAME = "tagSecond";
    private static final int PAGE = 1;
    private static final int SIZE = 5;
    private static final GiftCertificate GIFT_CERTIFICATE_FIRST = new GiftCertificate(1L, "name", "description",
            new BigDecimal("1.00"), 1, LocalDateTime.of(2021, 8, 24, 10, 10, 10),
            LocalDateTime.of(2021, 8, 24, 10, 10, 10),
            new ArrayList<>(Arrays.asList(DEFAULT_TAG, DEFAULT_TAG_SECOND)), new ArrayList<>());
    private static final GiftCertificate GIFT_CERTIFICATE_SECOND = new GiftCertificate(2L, "second-third", "description",
            new BigDecimal("1.00"), 1, LocalDateTime.of(2021, 8, 25, 10, 10, 10),
            LocalDateTime.of(2021, 8, 25, 10, 10, 10),
            new ArrayList<>(Arrays.asList(DEFAULT_TAG, DEFAULT_TAG_SECOND)), new ArrayList<>());
    private static final GiftCertificate GIFT_CERTIFICATE_THIRD = new GiftCertificate(3L, "third", "description",
            new BigDecimal("1.00"), 1, LocalDateTime.of(2021, 8, 26, 10, 10, 10),
            LocalDateTime.of(2021, 8, 26, 10, 10, 10),
            new ArrayList<>(Arrays.asList(DEFAULT_TAG)), new ArrayList<>());

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
    public void saveNewGiftCertificateTest() {
        List<Tag> tags = Arrays.asList(TAG_FIRST, TAG_SECOND);
        Optional<Tag> tagFirstOptional = Optional.of(TAG_FIRST);
        Optional<Tag> tagSecondOptional = Optional.of(TAG_SECOND);
        when(giftCertificateRepository.save(GIFT_CERTIFICATE_FIRST)).thenReturn(TEST_ID);
        when(giftCertificateRepository.findById(TEST_ID)).thenReturn(Optional.of(GIFT_CERTIFICATE_FIRST));
        when(tagRepository.findTagByName(FIRST_TAG_NAME)).thenReturn(tagFirstOptional);
        when(tagRepository.findTagByName(SECOND_TAG_NAME)).thenReturn(Optional.empty()).thenReturn(tagSecondOptional);
        when(tagRepository.findById(NEW_TAG_ID)).thenReturn(tagSecondOptional);
        when(tagRepository.save(TAG_SECOND)).thenReturn(NEW_TAG_ID);
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
    public void updateGiftCertificateTest() throws ResourceNotFoundException {
        GiftCertificate giftCertificate = GiftCertificate.builder().id(1L).name("name").description("description")
                .price(new BigDecimal("1.00")).duration(1).tags(Arrays.asList(TAG_FIRST, TAG_SECOND)).build();
        List<Tag> tags = Arrays.asList(TAG_FIRST, TAG_SECOND);
        Optional<Tag> tagFirstOptional = Optional.of(TAG_FIRST);
        Optional<Tag> tagSecondOptional = Optional.of(TAG_SECOND);
        GiftCertificateTagsWrapper giftCertificateTagsWrapper = GiftCertificateTagsWrapper.builder()
                .name("name").description("description").duration(1).price(new BigDecimal("1.00")).tags(tags).build();
        when(giftCertificateRepository.findById(TEST_ID)).thenReturn(Optional.of(giftCertificate));
        doNothing().when(giftCertificateRepository).update(giftCertificate);
        doNothing().when(tagRepository).deleteGiftCertificateTags(TEST_ID);
        when(tagRepository.findTagByName(FIRST_TAG_NAME)).thenReturn(tagFirstOptional);
        when(tagRepository.findTagByName(SECOND_TAG_NAME)).thenReturn(Optional.empty());
        when(tagRepository.findById(NEW_TAG_ID)).thenReturn(tagSecondOptional);
        when(tagRepository.save(TAG_SECOND)).thenReturn(NEW_TAG_ID);
        doNothing().when(giftCertificateRepository).addTagToGiftCertificate(giftCertificate, TAG_FIRST);
        doNothing().when(giftCertificateRepository).addTagToGiftCertificate(giftCertificate, TAG_SECOND);

        giftCertificateService.updateGiftCertificate(TEST_ID, giftCertificateTagsWrapper);

        verify(giftCertificateRepository).findById(anyLong());
        verify(giftCertificateRepository).update(any(GiftCertificate.class));
        verify(tagRepository).deleteGiftCertificateTags(anyLong());
        verify(tagRepository).save(any(Tag.class));
        verify(tagRepository).findById(anyLong());
        verify(tagRepository, times(3)).findTagByName(anyString());
        verify(giftCertificateRepository, times(2))
                .addTagToGiftCertificate(any(GiftCertificate.class), any(Tag.class));
    }

    @Test
    public void findGiftCertificatesByTagTestShouldReturnGiftCertificateList() throws BadRequestException {
        String givenTagName = "test";
        List<String> tagNames = Arrays.asList(givenTagName);
        List<GiftCertificate> expected = Arrays.asList(GIFT_CERTIFICATE_FIRST, GIFT_CERTIFICATE_SECOND, GIFT_CERTIFICATE_THIRD);
        when(giftCertificateRepository.findGiftCertificatesByTag(EMPTY_LIST, NULL_STRING_PARAM,
                givenTagName, PAGE, SIZE)).thenReturn(expected);
        List<GiftCertificate> actual = giftCertificateService.findGiftCertificates(tagNames, EMPTY_LIST,
                NULL_STRING_PARAM, null, PAGE, SIZE);

        assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).findGiftCertificatesByTag(any(), any(), anyString(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void findGiftCertificatesBySeveralTagsTestShouldReturnGiftCertificateList() throws BadRequestException {
        String givenTagName = "test";
        String givenTagNameSecond = "rest";
        List<String> tagNames = Arrays.asList(givenTagName, givenTagNameSecond);

        List<GiftCertificate> expected = Arrays.asList(GIFT_CERTIFICATE_FIRST, GIFT_CERTIFICATE_SECOND);
        when(giftCertificateRepository.findGiftCertificateBySeveralTags(EMPTY_LIST, NULL_STRING_PARAM,
                tagNames, PAGE, SIZE)).thenReturn(expected);

        List<GiftCertificate> actual = giftCertificateService.findGiftCertificates(tagNames, EMPTY_LIST,
                NULL_STRING_PARAM, null, PAGE, SIZE);

        assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).findGiftCertificateBySeveralTags(any(), any(), Mockito.anyList(),
                Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void findByMatchTestShouldReturnGiftCertificateList() throws BadRequestException {
        String searchCondition = "third";
        List<GiftCertificate> expected = Arrays.asList(GIFT_CERTIFICATE_SECOND, GIFT_CERTIFICATE_THIRD);
        when(giftCertificateRepository.findByMatch(EMPTY_LIST, NULL_STRING_PARAM, searchCondition, PAGE, SIZE))
                .thenReturn(expected);

        List<GiftCertificate> actual = giftCertificateService.findGiftCertificates(EMPTY_LIST, EMPTY_LIST,
                NULL_STRING_PARAM, searchCondition, PAGE, SIZE);

        assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).findByMatch(any(), any(), anyString(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void findGiftCertificatesBySeveralTagsWithSortByNameOrderDesc() throws BadRequestException {
        String givenTagName = "test";
        String givenTagNameSecond = "rest";
        String sortOrder = "desc";
        List<String> sortParams = Arrays.asList("name");
        List<String> tagNames = Arrays.asList(givenTagName, givenTagNameSecond);
        List<GiftCertificate> expected = Arrays.asList(GIFT_CERTIFICATE_SECOND, GIFT_CERTIFICATE_FIRST);
        when(giftCertificateRepository.findGiftCertificateBySeveralTags(sortParams, sortOrder, tagNames, PAGE, SIZE))
                .thenReturn(expected);

        List<GiftCertificate> actual = giftCertificateService.findGiftCertificates(tagNames, sortParams,
                sortOrder, null, PAGE, SIZE);

        assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).findGiftCertificateBySeveralTags(Mockito.anyList(), any(),
                any(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void findGiftCertificatesByTagWithSortByDateOrderAsc() throws BadRequestException {
        String givenTagName = "test";
        List<String> tagNames = Arrays.asList(givenTagName);
        String sortOrder = "asc";
        List<String> sortParams = Arrays.asList("date");
        List<GiftCertificate> expected = Arrays.asList(GIFT_CERTIFICATE_THIRD, GIFT_CERTIFICATE_SECOND, GIFT_CERTIFICATE_FIRST);
        when(giftCertificateRepository.findGiftCertificatesByTag(sortParams, sortOrder,
                givenTagName, PAGE, SIZE)).thenReturn(expected);

        List<GiftCertificate> actual = giftCertificateService.findGiftCertificates(tagNames, sortParams,
                sortOrder, null, PAGE, SIZE);

        assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).findGiftCertificatesByTag(Mockito.anyList(), anyString(),
                anyString(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void findGiftCertificatesByTagWithSortByDateAndByNameOrderAsc() throws BadRequestException {
        String givenTagName = "test";
        List<String> tagNames = Arrays.asList(givenTagName);
        String sortOrder = "asc";
        List<String> sortParams = Arrays.asList("date", "name");
        List<GiftCertificate> expected = Arrays.asList(GIFT_CERTIFICATE_FIRST, GIFT_CERTIFICATE_THIRD, GIFT_CERTIFICATE_SECOND);
        when(giftCertificateRepository.findGiftCertificatesByTag(sortParams, sortOrder, givenTagName, PAGE, SIZE))
                .thenReturn(expected);

        List<GiftCertificate> actual = giftCertificateService.findGiftCertificates(tagNames, sortParams,
                sortOrder, null, PAGE, SIZE);

        assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).findGiftCertificatesByTag(Mockito.anyList(), anyString(),
                anyString(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void findAllGiftCertificatesTestWithSortByName() throws BadRequestException {
        List<String> sortParams = Arrays.asList("name");
        List<GiftCertificate> expected = Arrays.asList(GIFT_CERTIFICATE_FIRST, GIFT_CERTIFICATE_SECOND, GIFT_CERTIFICATE_THIRD);
        when(giftCertificateRepository.findAllGiftCertificates(sortParams, NULL_STRING_PARAM, PAGE, SIZE))
                .thenReturn(expected);

        List<GiftCertificate> actual = giftCertificateService.findGiftCertificates(EMPTY_LIST, sortParams, NULL_STRING_PARAM,
                NULL_STRING_PARAM, PAGE, SIZE);

        assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).findAllGiftCertificates(Mockito.anyList(), any(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void findAllGiftCertificatesTestShouldReturnGiftCertificateList() throws BadRequestException {
        List<GiftCertificate> expected = Arrays.asList(GIFT_CERTIFICATE_FIRST, GIFT_CERTIFICATE_SECOND, GIFT_CERTIFICATE_THIRD);
        when(giftCertificateRepository.findAllGiftCertificates(EMPTY_LIST, NULL_STRING_PARAM, PAGE, SIZE))
                .thenReturn(expected);

        List<GiftCertificate> actual = giftCertificateService.findGiftCertificates(EMPTY_LIST, EMPTY_LIST,
                NULL_STRING_PARAM, NULL_STRING_PARAM, PAGE, SIZE);

        assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).findAllGiftCertificates(any(), any(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void findGiftCertificatesByTagAndByNameShouldThrowException() {
        String givenTagName = "test";
        List<String> tagNames = Arrays.asList(givenTagName);
        String sortOrder = "asc";
        String searchCondition = "third";
        List<String> sortParams = Arrays.asList("date", "name");

        Executable executable = () -> giftCertificateService.findGiftCertificates(tagNames, sortParams,
                sortOrder, searchCondition, PAGE, SIZE);

        Assertions.assertThrows(BadRequestException.class, executable);
    }

    @Test
    public void findGiftCertificatesBySearchConditionWithSortNullAndOrderNotNullShouldThrowException() {
        String sortOrder = "asc";
        String searchCondition = "third";

        Executable executable = () -> giftCertificateService.findGiftCertificates(EMPTY_LIST, EMPTY_LIST,
                sortOrder, searchCondition, PAGE, SIZE);

        Assertions.assertThrows(BadRequestException.class, executable);
    }

    @Test
    public void getPaginationInfoTestShouldReturnPageQuantity() throws ResourceNotFoundException {
        Long countResult = 3L;
        Long expected = 2L;
        int pageNumber = 1;
        int pageSize = 2;

        when(giftCertificateRepository.countGiftCertificateSelect("ALL", EMPTY_LIST, NULL_STRING_PARAM, NULL_STRING_PARAM))
                .thenReturn(countResult);

        Long actual = giftCertificateService.getPaginationInfo(pageNumber, pageSize, EMPTY_LIST, NULL_STRING_PARAM);

        assertEquals(expected, actual);

        Mockito.verify(giftCertificateRepository).countGiftCertificateSelect(anyString(), any(), any(), any());
    }

    @Test
    public void getPaginationInfoTestShouldThrowExceptionIfRequestPageNonexistent() {
        Long countResult = 10L;
        int pageNumber = 3;
        String searchType = "ALL";

        when(giftCertificateRepository.countGiftCertificateSelect(searchType, EMPTY_LIST, NULL_STRING_PARAM, NULL_STRING_PARAM))
                .thenReturn(countResult);

        Executable executable = () -> giftCertificateService.getPaginationInfo(pageNumber, SIZE, EMPTY_LIST, NULL_STRING_PARAM);

        Assertions.assertThrows(ResourceNotFoundException.class, executable);
    }

    @Test
    public void partialGiftCertificateUpdateTest() throws ResourceNotFoundException {
        String newGiftCertificateName = "updated";
        when(giftCertificateRepository.findById(3L)).thenReturn(Optional.of(GIFT_CERTIFICATE_THIRD));
        doNothing().when(giftCertificateRepository)
                .partialGiftCertificateUpdate("name", newGiftCertificateName, 3L);

        boolean actual = giftCertificateService
                .partialGiftCertificateUpdate(newGiftCertificateName, NULL_STRING_PARAM, null, null, 3L);

        assertTrue(actual);

        Mockito.verify(giftCertificateRepository).findById(anyLong());
        Mockito.verify(giftCertificateRepository).partialGiftCertificateUpdate(anyString(), anyString(), anyLong());
    }

    @Test
    public void partialGiftCertificateUpdateTestShouldReturnFalseIfNotOneParam() throws ResourceNotFoundException {
        String newParam = "updated";
        when(giftCertificateRepository.findById(3L)).thenReturn(Optional.of(GIFT_CERTIFICATE_THIRD));

        boolean actual = giftCertificateService
                .partialGiftCertificateUpdate(newParam, newParam, null, null, 3L);

        assertFalse(actual);

        Mockito.verify(giftCertificateRepository).findById(anyLong());
    }

    @Test
    public void partialGiftCertificateUpdateTestShouldThrowException() {
        String newGiftCertificateName = "updated";
        when(giftCertificateRepository.findById(3L)).thenReturn(Optional.empty());

        Executable executable = () -> giftCertificateService
                .partialGiftCertificateUpdate(newGiftCertificateName, NULL_STRING_PARAM, null, null, 3L);

        Assertions.assertThrows(ResourceNotFoundException.class, executable);
    }


}
