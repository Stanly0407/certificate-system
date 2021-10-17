package com.epam.esm.services;

import com.epam.esm.entities.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.services.exceptions.BadRequestException;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.service.TagServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    private static final Long TEST_ID = 1L;
    private static final Tag TEST_TAG = new Tag(1L, "newTag", new ArrayList<>());
    private static final String TAG_NAME = "newTag";

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    public void saveNewTagTest() throws BadRequestException {
        Tag newTag = Tag.builder().name("newTag").build();
        when(tagRepository.findTagByName(TAG_NAME)).thenReturn(Optional.empty());
        when(tagRepository.save(newTag)).thenReturn(1L);

        tagService.saveNewTag(TAG_NAME);

        Mockito.verify(tagRepository).save(any(Tag.class));
    }

    @Test
    public void saveNewTagTestShouldThrowException() {
        Tag newTag = Tag.builder().name("newTag").build();
        when(tagRepository.findTagByName(TAG_NAME)).thenReturn(Optional.of(newTag));

        Executable executable = () -> tagService.saveNewTag(TAG_NAME);

        Assertions.assertThrows(BadRequestException.class, executable);
    }

    @Test
    public void findTagByIdTest() throws ResourceNotFoundException {
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(TEST_TAG));

        Tag actual = tagService.findTagById(TEST_ID);

        Assertions.assertEquals(TEST_TAG, actual);

        Mockito.verify(tagRepository).findById(anyLong());
    }

    @Test
    public void findTagByIdTestShouldThrowException() {
        when(tagRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        Executable executable = () -> tagService.findTagById(TEST_ID);

        Assertions.assertThrows(ResourceNotFoundException.class, executable);

        Mockito.verify(tagRepository).findById(anyLong());
    }

    @Test
    public void deleteTagTest() throws ResourceNotFoundException {
        when(tagRepository.findById(TEST_ID)).thenReturn(Optional.of(TEST_TAG));
        doNothing().when(tagRepository).delete(TEST_TAG);

        tagService.deleteTag(TEST_ID);

        Mockito.verify(tagRepository).findById(anyLong());
    }

    @Test
    public void deleteTagTestShouldThrowException() {
        when(tagRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        Executable executable = () -> tagService.findTagById(TEST_ID);

        Assertions.assertThrows(ResourceNotFoundException.class, executable);

        Mockito.verify(tagRepository).findById(anyLong());
    }

}
