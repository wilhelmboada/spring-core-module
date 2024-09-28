package com.booking.dao;

import com.booking.model.Event;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class EventDao {

    private final StorageBean storageBean;

    public EventDao(StorageBean storageBean) {
        this.storageBean = storageBean;
    }

    public Event findById(long id) {
        return (Event) storageBean.getStorage("event").get(id);
    }

    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        Collection<Object> userCollection = storageBean.getStorage("event").values();
        return userCollection.stream()
                .map(Event.class::cast)
                .filter(event -> title.equals(event.getTitle()))
                .toList();
    }

    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        Collection<Object> collection = storageBean.getStorage("event").values();
        return collection.stream()
                .map(Event.class::cast)
                .filter(event -> day.equals(event.getDate()))
                .toList();
    }

    public Event save(Event event) {
        storageBean.addToStorage("event", event.getId(), event);
        return event;
    }

    public Event update(Event event) {
        return Optional.ofNullable(findById(event.getId()))
                .map(this::save)
                .orElse(null);
    }

    public boolean delete(long id) {
        storageBean.deleteFromStorage("event", id);
        return Optional.ofNullable(findById(id)).isEmpty();
    }

}
