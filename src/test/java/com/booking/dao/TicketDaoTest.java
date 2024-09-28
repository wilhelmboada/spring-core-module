package com.booking.dao;

import com.booking.dao.TicketDao;
import com.booking.model.Event;
import com.booking.model.Ticket;
import com.booking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketDaoTest {

    @Mock
    private StorageBean storageBean;

    @InjectMocks
    private TicketDao ticketDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Ticket mockTicket = new Ticket();
        mockTicket.setId(1L);

        ticketDao.save(mockTicket);

        verify(storageBean, times(1)).addToStorage("ticket", mockTicket.getId(), mockTicket);
    }

    @Test
    void testGetBookedTicketsByUser() {
        User mockUser = new User();
        mockUser.setId(1L);
        Ticket mockTicket1 = new Ticket();
        mockTicket1.setUserId(1L);
        Ticket mockTicket2 = new Ticket();
        mockTicket2.setUserId(2L);

        when(storageBean.getStorage("ticket")).thenReturn(new HashMap<>() {{
            put(1L, mockTicket1);
            put(2L, mockTicket2);
        }});

        List<Ticket> result = ticketDao.getBookedTickets(mockUser, 10, 1);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
        verify(storageBean, times(1)).getStorage("ticket");
    }

    @Test
    void testGetBookedTicketsByEvent() {
        Event mockEvent = new Event();
        mockEvent.setId(1L);
        Ticket mockTicket1 = new Ticket();
        mockTicket1.setEventId(1L);
        Ticket mockTicket2 = new Ticket();
        mockTicket2.setEventId(2L);

        when(storageBean.getStorage("ticket")).thenReturn(new HashMap<>() {{
            put(1L, mockTicket1);
            put(2L, mockTicket2);
        }});

        List<Ticket> result = ticketDao.getBookedTickets(mockEvent, 10, 1);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getEventId());
        verify(storageBean, times(1)).getStorage("ticket");
    }

    @Test
    void testDelete() {
        long ticketId = 1L;
        Ticket mockTicket = new Ticket();
        mockTicket.setId(ticketId);

        when(storageBean.getStorage("ticket")).thenReturn(new HashMap<>() {{
            put(ticketId, mockTicket);
        }});

        boolean result = ticketDao.delete(ticketId);

        assertFalse(result);
        verify(storageBean, times(1)).deleteFromStorage("ticket", ticketId);
    }

    @Test
    void testDeleteTicketNotFound() {
        long ticketId = 1L;

        when(storageBean.getStorage("ticket")).thenReturn(Collections.emptyMap());

        boolean result = ticketDao.delete(ticketId);

        assertTrue(result);
        verify(storageBean, times(1)).deleteFromStorage("ticket", ticketId);
    }

    @Test
    void testFindById() {
        long ticketId = 1L;
        Ticket mockTicket = new Ticket();
        mockTicket.setId(ticketId);

        when(storageBean.getStorage("ticket")).thenReturn(new HashMap<>() {{
            put(ticketId, mockTicket);
        }});

        Ticket result = ticketDao.save(mockTicket);

        assertEquals(ticketId, result.getId());
        verify(storageBean, times(1)).addToStorage("ticket", ticketId, mockTicket);
    }

    @Test
    void testFindByIdNotFound() {
        long ticketId = 1L;
        when(storageBean.getStorage("ticket")).thenReturn(Collections.emptyMap());

        Ticket result = ticketDao.save(new Ticket());

        assertNotEquals(ticketId, result.getId());
        verify(storageBean, times(1)).addToStorage("ticket", result.getId(), result);
    }
}
