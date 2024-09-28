package com.booking.service;

import com.booking.dao.UserDao;
import com.booking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById() {
        long userId = 1L;
        User mockUser = new User();
        when(userDao.findById(userId)).thenReturn(mockUser);

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(userDao, times(1)).findById(userId);
    }

    @Test
    void testGetUserByEmail() {
        String email = "test@example.com";
        User mockUser = new User();
        when(userDao.getUserByEmail(email)).thenReturn(Optional.of(mockUser));

        User result = userService.getUserByEmail(email);

        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(userDao, times(1)).getUserByEmail(email);
    }

    @Test
    void testGetUserByEmailNotFound() {
        String email = "notfound@example.com";
        when(userDao.getUserByEmail(email)).thenReturn(Optional.empty());

        User result = userService.getUserByEmail(email);

        assertNull(result);
        verify(userDao, times(1)).getUserByEmail(email);
    }

    @Test
    void testGetUsersByName() {
        String name = "John";
        int pageSize = 10;
        int pageNum = 1;
        List<User> mockUsers = List.of(new User(), new User());
        when(userDao.getUsersByName(name, pageSize, pageNum)).thenReturn(mockUsers);

        List<User> result = userService.getUsersByName(name, pageSize, pageNum);

        assertNotNull(result);
        assertEquals(mockUsers.size(), result.size());
        verify(userDao, times(1)).getUsersByName(name, pageSize, pageNum);
    }

    @Test
    void testCreateUser() {
        User user = new User();
        when(userDao.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userDao, times(1)).save(user);
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        when(userDao.update(user)).thenReturn(user);

        User result = userService.updateUser(user);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userDao, times(1)).update(user);
    }

    @Test
    void testDeleteUser() {
        long userId = 1L;
        when(userDao.delete(userId)).thenReturn(true);

        boolean result = userService.deleteUser(userId);

        assertTrue(result);
        verify(userDao, times(1)).delete(userId);
    }
}