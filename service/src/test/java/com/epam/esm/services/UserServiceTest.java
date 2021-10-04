package com.epam.esm.services;

import com.epam.esm.entities.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.services.exceptions.ResourceNotFoundException;
import com.epam.esm.services.service.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final int PAGE = 1;
    private static final int SIZE = 5;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getAllUsersTestShouldReturnUserList() throws ResourceNotFoundException {
        User userFirst = User.builder().name("First").lastname("LastnameFirst").login("test1").password("1111").build();
        User userSecond = User.builder().name("Second").lastname("LastnameSecond").login("test2").password("2222").build();
        List<User> expected = Arrays.asList(userFirst, userSecond);
        when(userRepository.getUsersCommonQuantity()).thenReturn(2L);
        when(userRepository.getAllUsers(PAGE, SIZE)).thenReturn(expected);

        List<User> actual = userService.getAllUsers(PAGE, SIZE);

        Assertions.assertEquals(expected, actual);

        Mockito.verify(userRepository).getUsersCommonQuantity();
        Mockito.verify(userRepository).getAllUsers(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void getAllUsersTestShouldThrowException() {
        int nonExistentPage = 3;
        when(userRepository.getUsersCommonQuantity()).thenReturn(2L);

        Executable executable = () -> userService.getAllUsers(nonExistentPage, SIZE);

        Assertions.assertThrows(ResourceNotFoundException.class, executable);
    }

    @Test
    public void getUsersPaginationInfoTest() {
        long expected = 3L;
        when(userRepository.getUsersCommonQuantity()).thenReturn(15L);

        long actual = userService.getUsersPaginationInfo(PAGE, SIZE);

        Assertions.assertEquals(expected, actual);

        Mockito.verify(userRepository).getUsersCommonQuantity();
    }


}
