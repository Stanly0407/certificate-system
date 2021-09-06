package com.epam.esm.services;

import com.epam.esm.entities.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.services.service.TagServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    private static final Tag TEST_TAG = new Tag(1L, "newTag");

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    public void saveNewTagTest() {
        when(tagRepository.findTagByName(anyString())).thenReturn(Optional.empty());
        when(tagRepository.save(TEST_TAG)).thenReturn(anyLong());

        tagService.saveNewTag(TEST_TAG);

        Mockito.verify(tagRepository).findTagByName(anyString());
        Mockito.verify(tagRepository).save(any(Tag.class));
    }

    @Test
    public void saveNewTagTestExistingTag() {
        when(tagRepository.findTagByName(anyString())).thenReturn(Optional.of(TEST_TAG));

        tagService.saveNewTag(TEST_TAG);

        Mockito.verify(tagRepository).findTagByName(anyString());
        Mockito.verify(tagRepository, times(0)).save(any(Tag.class));
    }

    @Test
    public void findTagByIdTest() {
        Optional<Tag> actual = Optional.of(TEST_TAG);
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(TEST_TAG));

        Optional<Tag> expected = tagService.findTagById(1L);

        Assertions.assertEquals(expected, actual);
        Mockito.verify(tagRepository).findById(anyLong());

    }

}
