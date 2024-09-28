package com.booking.dao;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class StorageBean {

    private final Map<String, Map<Long, Object>> storage;

    public StorageBean() {
        storage = Map.of(
                "user", new HashMap<>(),
                "event", new HashMap<>(),
                "ticket", new HashMap<>()
        );
    }

    public Map<Long, Object> getStorage(String namespace) {
        return storage.get(namespace);
    }

    public void addToStorage(String namespace, Long id, Object entity) {
        storage.get(namespace).put(id, entity);
    }

    public void deleteFromStorage(String namespace, Long id) {
        storage.get(namespace).remove(id);
    }
}
