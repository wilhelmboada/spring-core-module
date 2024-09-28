package com.booking.dao;

import com.booking.model.Event;
import com.booking.model.Ticket;
import com.booking.model.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class TicketDao {

    private final StorageBean storageBean;

    public TicketDao(StorageBean storageBean) {
        this.storageBean = storageBean;
    }

    public Ticket save(Ticket ticket) {
        storageBean.addToStorage("ticket", ticket.getId(), ticket);
        return ticket;
    }

    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        Collection<Object> collection = storageBean.getStorage("ticket").values();
        return collection.stream()
                .map(Ticket.class::cast)
                .filter(ticket -> ticket.getUserId() == user.getId())
                .toList();
    }

    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        Collection<Object> userCollection = storageBean.getStorage("ticket").values();
        return userCollection.stream()
                .map(Ticket.class::cast)
                .filter(ticket -> ticket.getEventId() == event.getId())
                .toList();
    }

    public boolean delete(long id) {
        storageBean.deleteFromStorage("ticket", id);
        return Optional.ofNullable(findById(id)).isEmpty();
    }

    private Ticket findById(long id) {
        return (Ticket) storageBean.getStorage("ticket").get(id);
    }
}
