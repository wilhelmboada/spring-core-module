package com.booking.service;

import com.booking.dao.TicketDao;
import com.booking.model.Category;
import com.booking.model.Event;
import com.booking.model.Ticket;
import com.booking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private TicketDao ticketDao;

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookTicket() {
        long userId = 1L;
        long eventId = 100L;
        int place = 5;
        Category category = new Category(1);
        Ticket mockTicket = new Ticket(new Date().getTime(), eventId, userId, category, place);

        when(ticketDao.save(any(Ticket.class))).thenReturn(mockTicket);

        Ticket result = ticketService.bookTicket(userId, eventId, place, category);

        assertNotNull(result);
        assertEquals(mockTicket, result);
        verify(ticketDao, times(1)).save(any(Ticket.class));
    }

    @Test
    void testGetBookedTicketsByUser() {
        User user = new User();
        int pageSize = 10;
        int pageNum = 1;
        List<Ticket> mockTickets = List.of(new Ticket(), new Ticket());

        when(ticketDao.getBookedTickets(user, pageSize, pageNum)).thenReturn(mockTickets);

        List<Ticket> result = ticketService.getBookedTickets(user, pageSize, pageNum);

        assertNotNull(result);
        assertEquals(mockTickets.size(), result.size());
        verify(ticketDao, times(1)).getBookedTickets(user, pageSize, pageNum);
    }

    @Test
    void testGetBookedTicketsByEvent() {
        Event event = new Event();
        int pageSize = 10;
        int pageNum = 1;
        List<Ticket> mockTickets = List.of(new Ticket(), new Ticket());

        when(ticketDao.getBookedTickets(event, pageSize, pageNum)).thenReturn(mockTickets);

        List<Ticket> result = ticketService.getBookedTickets(event, pageSize, pageNum);

        assertNotNull(result);
        assertEquals(mockTickets.size(), result.size());
        verify(ticketDao, times(1)).getBookedTickets(event, pageSize, pageNum);
    }
}

