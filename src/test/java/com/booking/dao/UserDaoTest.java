package com.booking.dao;

import com.booking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDaoTest {

    @Mock
    private StorageBean storageBean;

    @InjectMocks
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        long userId = 1L;
        User mockUser = new User();
        Map<Long, Object> storage = new HashMap<>();
        storage.put(userId, mockUser);

        when(storageBean.getStorage("user")).thenReturn(storage);

        User result = userDao.findById(userId);

        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(storageBean, times(1)).getStorage("user");
    }

    @Test
    void testGetUserByEmail() {
        String email = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        Collection<Object> storage = List.of(mockUser);

        when(storageBean.getStorage("user")).thenReturn(new HashMap<>() {{
            put(1L, mockUser);
        }});

        Optional<User> result = userDao.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(storageBean, times(1)).getStorage("user");
    }

    @Test
    void testGetUserByEmailNotFound() {
        String email = "notfound@example.com";
        when(storageBean.getStorage("user")).thenReturn(Collections.emptyMap());

        Optional<User> result = userDao.getUserByEmail(email);

        assertFalse(result.isPresent());
        verify(storageBean, times(1)).getStorage("user");
    }

    @Test
    void testGetUsersByName() {
        String name = "John";
        User mockUser1 = new User();
        mockUser1.setName(name);
        User mockUser2 = new User();
        mockUser2.setName("Jane");
        Collection<Object> storage = List.of(mockUser1, mockUser2);

        when(storageBean.getStorage("user")).thenReturn(new HashMap<>() {{
            put(1L, mockUser1);
            put(2L, mockUser2);
        }});

        List<User> result = userDao.getUsersByName(name, 10, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(name, result.get(0).getName());
        verify(storageBean, times(1)).getStorage("user");
    }

    @Test
    void testSave() {
        User mockUser = new User();
        mockUser.setId(1L);

        userDao.save(mockUser);

        verify(storageBean, times(1)).addToStorage("user", mockUser.getId(), mockUser);
    }

    @Test
    void testUpdate() {
        User mockUser = new User();
        mockUser.setId(1L);
        when(storageBean.getStorage("user")).thenReturn(new HashMap<>() {{
            put(mockUser.getId(), mockUser);
        }});

        User result = userDao.update(mockUser);

        assertNotNull(result);
        verify(storageBean, times(1)).addToStorage("user", mockUser.getId(), mockUser);
    }

    @Test
    void testUpdateUserNotFound() {
        User mockUser = new User();
        mockUser.setId(1L);
        when(storageBean.getStorage("user")).thenReturn(Collections.emptyMap());

        User result = userDao.update(mockUser);

        assertNull(result);
        verify(storageBean, times(0)).addToStorage(anyString(), anyLong(), any(User.class));
    }

    @Test
    void testDelete() {
        long userId = 1L;
        User mockUser = new User();
        Map<Long, Object> storage = new HashMap<>();
        storage.put(userId, mockUser);

        when(storageBean.getStorage("user")).thenReturn(storage);

        boolean result = userDao.delete(userId);

        assertFalse(result);
        verify(storageBean, times(1)).deleteFromStorage("user", userId);
    }

    @Test
    void testDeleteUserNotFound() {
        long userId = 1L;
        when(storageBean.getStorage("user")).thenReturn(Collections.emptyMap());

        boolean result = userDao.delete(userId);

        assertTrue(result);
        verify(storageBean, times(1)).deleteFromStorage("user", userId);
    }
}

