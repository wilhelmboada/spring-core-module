package com.booking.dao;

import com.booking.dao.StorageBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StorageBeanTest {

    private StorageBean storageBean;

    @BeforeEach
    void setUp() {
        storageBean = new StorageBean();
    }

    @Test
    void testGetStorage() {
        Map<Long, Object> userStorage = storageBean.getStorage("user");
        Map<Long, Object> eventStorage = storageBean.getStorage("event");
        Map<Long, Object> ticketStorage = storageBean.getStorage("ticket");

        assertNotNull(userStorage);
        assertTrue(userStorage instanceof HashMap);
        assertNotNull(eventStorage);
        assertTrue(eventStorage instanceof HashMap);
        assertNotNull(ticketStorage);
        assertTrue(ticketStorage instanceof HashMap);
    }

    @Test
    void testAddToStorage() {
        Long id = 1L;
        Object user = new Object();

        storageBean.addToStorage("user", id, user);

        Map<Long, Object> userStorage = storageBean.getStorage("user");
        assertEquals(1, userStorage.size());
        assertEquals(user, userStorage.get(id));
    }

    @Test
    void testDeleteFromStorage() {
        Long id = 1L;
        Object user = new Object();

        storageBean.addToStorage("user", id, user);
        storageBean.deleteFromStorage("user", id);

        Map<Long, Object> userStorage = storageBean.getStorage("user");
        assertNull(userStorage.get(id));
        assertTrue(userStorage.isEmpty());
    }

    @Test
    void testAddAndGetEvent() {
        Long eventId = 10L;
        Object event = new Object();

        storageBean.addToStorage("event", eventId, event);

        Map<Long, Object> eventStorage = storageBean.getStorage("event");
        assertEquals(1, eventStorage.size());
        assertEquals(event, eventStorage.get(eventId));
    }

    @Test
    void testAddAndGetTicket() {
        Long ticketId = 100L;
        Object ticket = new Object();

        storageBean.addToStorage("ticket", ticketId, ticket);

        Map<Long, Object> ticketStorage = storageBean.getStorage("ticket");
        assertEquals(1, ticketStorage.size());
        assertEquals(ticket, ticketStorage.get(ticketId));
    }

    @Test
    void testDeleteNonExistentEntity() {
        Long nonExistentId = 999L;

        storageBean.deleteFromStorage("user", nonExistentId);

        Map<Long, Object> userStorage = storageBean.getStorage("user");
        assertTrue(userStorage.isEmpty());
    }
}

