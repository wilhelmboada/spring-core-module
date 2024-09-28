package com.booking.dao;

import com.booking.dao.EventDao;
import com.booking.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventDaoTest {

    @Mock
    private StorageBean storageBean;

    @InjectMocks
    private EventDao eventDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        long eventId = 1L;
        Event mockEvent = new Event();
        Map<Long, Object> storage = new HashMap<>();
        storage.put(eventId, mockEvent);

        when(storageBean.getStorage("event")).thenReturn(storage);

        Event result = eventDao.findById(eventId);

        assertNotNull(result);
        assertEquals(mockEvent, result);
        verify(storageBean, times(1)).getStorage("event");
    }

    @Test
    void testGetEventsByTitle() {
        String title = "Concert";
        Event mockEvent1 = new Event();
        mockEvent1.setTitle(title);
        Event mockEvent2 = new Event();
        mockEvent2.setTitle("Other");
        Collection<Object> storage = List.of(mockEvent1, mockEvent2);

        when(storageBean.getStorage("event")).thenReturn(new HashMap<>() {{
            put(1L, mockEvent1);
            put(2L, mockEvent2);
        }});

        List<Event> result = eventDao.getEventsByTitle(title, 10, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(title, result.get(0).getTitle());
        verify(storageBean, times(1)).getStorage("event");
    }

    @Test
    void testGetEventsForDay() {
        Date today = new Date();
        Event mockEvent1 = new Event();
        mockEvent1.setDate(today);
        Event mockEvent2 = new Event();
        mockEvent2.setDate(new Date(today.getTime() + 86400000)); // Add one day
        Collection<Object> storage = List.of(mockEvent1, mockEvent2);

        when(storageBean.getStorage("event")).thenReturn(new HashMap<>() {{
            put(1L, mockEvent1);
            put(2L, mockEvent2);
        }});

        List<Event> result = eventDao.getEventsForDay(today, 10, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(today, result.get(0).getDate());
        verify(storageBean, times(1)).getStorage("event");
    }

    @Test
    void testSave() {
        Event mockEvent = new Event();
        mockEvent.setId(1L);

        eventDao.save(mockEvent);

        verify(storageBean, times(1)).addToStorage("event", mockEvent.getId(), mockEvent);
    }

    @Test
    void testUpdate() {
        Event mockEvent = new Event();
        mockEvent.setId(1L);
        when(storageBean.getStorage("event")).thenReturn(new HashMap<>() {{
            put(mockEvent.getId(), mockEvent);
        }});

        Event result = eventDao.update(mockEvent);

        assertNotNull(result);
        verify(storageBean, times(1)).addToStorage("event", mockEvent.getId(), mockEvent);
    }

    @Test
    void testUpdateEventNotFound() {
        Event mockEvent = new Event();
        mockEvent.setId(1L);
        when(storageBean.getStorage("event")).thenReturn(Collections.emptyMap());

        Event result = eventDao.update(mockEvent);

        assertNull(result);
        verify(storageBean, times(0)).addToStorage(anyString(), anyLong(), any(Event.class));
    }

    @Test
    void testDelete() {
        long eventId = 1L;
        Event mockEvent = new Event();
        Map<Long, Object> storage = new HashMap<>();
        storage.put(eventId, mockEvent);

        when(storageBean.getStorage("event")).thenReturn(storage);

        boolean result = eventDao.delete(eventId);

        assertFalse(result);
        verify(storageBean).deleteFromStorage("event", eventId);
    }

    @Test
    void testDeleteEventNotFound() {
        long eventId = 1L;
        when(storageBean.getStorage("event")).thenReturn(Collections.emptyMap());

        boolean result = eventDao.delete(eventId);

        assertTrue(result);
        verify(storageBean, times(1)).deleteFromStorage("event", eventId);
    }
}

