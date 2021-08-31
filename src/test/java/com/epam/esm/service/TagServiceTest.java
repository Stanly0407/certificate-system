package com.epam.esm.service;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
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
    private TagRepositoryImpl tagRepository;

    @InjectMocks
    private TagService tagService;

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

}
