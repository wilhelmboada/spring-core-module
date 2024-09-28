package com.booking.service;

import com.booking.dao.TicketDao;
import com.booking.model.Category;
import com.booking.model.Event;
import com.booking.model.Ticket;
import com.booking.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketDao ticketDao;

    public Ticket bookTicket(long userId, long eventId, int place, Category category) {
        logger.info("booking ticket userId: {}, eventId: {}", userId, eventId);
        return ticketDao.save(new Ticket(new Date().getTime(), eventId, userId, category, place));
    }

    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        logger.info("get booked tickets userId: {}", user.getId());
        return ticketDao.getBookedTickets(user, pageSize, pageNum);
    }

    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        logger.info("get booked tickets eventId: {}", event.getId());
        return ticketDao.getBookedTickets(event, pageSize, pageNum);
    }

    public boolean cancelTicket(long id) {
        logger.info("cancel ticket id: {}", id);
        return ticketDao.delete(id);
    }
}
