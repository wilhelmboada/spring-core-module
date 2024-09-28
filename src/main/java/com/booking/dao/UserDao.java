package com.booking.dao;

import com.booking.model.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {

    private final StorageBean storageBean;

    public UserDao(StorageBean storageBean) {
        this.storageBean = storageBean;
    }

    public User findById(long id) {
        return (User) storageBean.getStorage("user").get(id);
    }

    public Optional<User> getUserByEmail(String email) {
        Collection<Object> userCollection = storageBean.getStorage("user").values();
        return userCollection.stream()
                .map(User.class::cast)
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }

    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        Collection<Object> userCollection = storageBean.getStorage("user").values();
        return userCollection.stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .filter(user -> name.equals(user.getName()))
                .toList();
    }

    public User save(User user) {
        storageBean.addToStorage("user", user.getId(), user);
        return user;
    }

    public User update(User user) {
        return Optional.ofNullable(findById(user.getId()))
                .map(this::save)
                .orElse(null);
    }

    public boolean delete(long id) {
        storageBean.deleteFromStorage("user", id);
        return Optional.ofNullable(findById(id)).isEmpty();
    }

}