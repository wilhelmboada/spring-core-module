package com.booking.config;

import com.booking.dao.StorageBean;
import com.booking.model.Event;
import com.booking.model.Ticket;
import com.booking.model.User;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageInitializer implements InitializingBean {

    @Value("${storage.init.file}")
    private String storageFilePath;

    private ObjectMapper objectMapper;

    private StorageBean storageBean;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setStorageBean(StorageBean storageBean) {
        this.storageBean = storageBean;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String jsonContent = new String(Files.readAllBytes(Paths.get(storageFilePath)));
        JsonNode rootNode = objectMapper.readTree(jsonContent);

        if (rootNode.has("users")) {
            JsonNode usersNode = rootNode.get("users");
            for (JsonNode userNode : usersNode) {
                User user = objectMapper.treeToValue(userNode, User.class);
                storageBean.addToStorage("user", user.getId(), user);
            }
        }

        if (rootNode.has("events")) {
            JsonNode eventsNode = rootNode.get("events");
            for (JsonNode eventNode : eventsNode) {
                Event event = objectMapper.treeToValue(eventNode, Event.class);
                storageBean.addToStorage("event", event.getId(), event);
            }
        }

        if (rootNode.has("tickets")) {
            JsonNode ticketsNode = rootNode.get("tickets");
            for (JsonNode ticketNode : ticketsNode) {
                Ticket ticket = objectMapper.treeToValue(ticketNode, Ticket.class);
                storageBean.addToStorage("ticket", ticket.getId(), ticket);
            }
        }
    }


}