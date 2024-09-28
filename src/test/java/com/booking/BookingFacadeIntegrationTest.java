package com.booking;

import com.booking.facade.BookingFacade;
import com.booking.facade.BookingFacadeImpl;
import com.booking.model.Category;
import com.booking.model.Event;
import com.booking.model.Ticket;
import com.booking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingFacadeIntegrationTest {

    private BookingFacade bookingFacade;

    @BeforeEach
    void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        bookingFacade = context.getBean(BookingFacadeImpl.class);
    }

    @Test
    void testCreateUserAndGetUser() {
        User user = new User(111, "Pedro", "pedro@example.com");
        User createdUser = bookingFacade.createUser(user);
        assertNotNull(createdUser);

        User foundUser = bookingFacade.getUserByEmail("pedro@example.com");
        assertEquals(user.getId(), foundUser.getId());

    }

    @Test
    void testCreateEventAndBookTicket() {
        // Step 1: Create a user
        User user = new User(112, "Maria", "maria@example.com");
        bookingFacade.createUser(user);

        // Step 2: Create an event
        Event event = new Event(222, "Concert", new Date());
        Event createdEvent = bookingFacade.createEvent(event);
        assertNotNull(createdEvent);

        // Step 3: Book a ticket for the event
        Ticket ticket = bookingFacade.bookTicket(user.getId(), event.getId(), 1, new Category(1));
        assertNotNull(ticket);
        assertEquals(event.getId(), ticket.getEventId());
        assertEquals(user.getId(), ticket.getUserId());

        // Step 4: Verify booked tickets for the user
        List<Ticket> bookedTickets = bookingFacade.getBookedTickets(user, 10, 1);
        assertFalse(bookedTickets.isEmpty());
        assertEquals(ticket.getId(), bookedTickets.get(0).getId());
    }

    @Test
    void testGetEventsByTitle() {
        // Step 1: Create an event
        Event event1 = new Event(333, "Football Match", new Date());
        bookingFacade.createEvent(event1);

        Event event2 = new Event(334, "Football Match", new Date());
        bookingFacade.createEvent(event2);

        // Step 2: Get events by title
        List<Event> events = bookingFacade.getEventsByTitle("Football Match", 10, 1);
        assertEquals(2, events.size());
    }

    @Test
    void testUpdateAndDeleteUser() {
        // Step 1: Create a user
        User user = new User(113, "Juan", "juan@example.com");
        bookingFacade.createUser(user);

        // Step 2: Update the user
        user.setName("Juan Updated");
        User updatedUser = bookingFacade.updateUser(user);
        assertEquals("Juan Updated", updatedUser.getName());

        // Step 3: Delete the user
        boolean isDeleted = bookingFacade.deleteUser(user.getId());
        assertTrue(isDeleted);

        // Step 4: Verify the user no longer exists
        User deletedUser = bookingFacade.getUserById(user.getId());
        assertNull(deletedUser);
    }

    @Test
    void testCancelTicket() {
        // Step 1: Create a user
        User user = new User(114, "Carlos", "carlos@example.com");
        bookingFacade.createUser(user);

        // Step 2: Create an event
        Event event = new Event(335, "Theater Play", new Date());
        bookingFacade.createEvent(event);

        // Step 3: Book a ticket
        Ticket ticket = bookingFacade.bookTicket(user.getId(), event.getId(), 5, new Category(1));
        assertNotNull(ticket);

        // Step 4: Cancel the ticket
        boolean isCanceled = bookingFacade.cancelTicket(ticket.getId());
        assertTrue(isCanceled);
    }
}

