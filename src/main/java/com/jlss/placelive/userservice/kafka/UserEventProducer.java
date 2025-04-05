package com.jlss.placelive.userservice.kafka;

import com.jlss.placelive.userservice.entity.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    private final KafkaTemplate<String, User> userKafkaTemplate;
    private final KafkaTemplate<String, Long> deleteKafkaTemplate;

    private static final String USER_TOPIC = "user-events";
    private static final String DELETE_TOPIC = "user-delete-events";

    public UserEventProducer(KafkaTemplate<String, User> userKafkaTemplate,
                              KafkaTemplate<String, Long> deleteKafkaTemplate) {
        this.userKafkaTemplate = userKafkaTemplate;
        this.deleteKafkaTemplate = deleteKafkaTemplate;
    }

    // 🔹 Send event when creating or updating a place
    public void sendUserEvent(User user) {
        userKafkaTemplate.send(USER_TOPIC, user);
    }

    // 🔹 Send event when deleting a user
    public void sendDeleteUserEvent(Long userId) {
        deleteKafkaTemplate.send(DELETE_TOPIC, userId);
    }
}
