package co.edu.unicauca.notification.repository;

import co.edu.unicauca.notification.model.Notification;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class NotificationRepository {

    private final Map<Long, Notification> store = new ConcurrentHashMap<>();

    public Notification save(Notification notification) {
        store.put(notification.getId(), notification);
        return notification;
    }

    public Collection<Notification> findAll() {
        return store.values();
    }

    public Collection<Notification> findByRecipient(Long recipientId) {
        return store.values().stream().filter(n -> n.getRecipientId().equals(recipientId)).toList();
    }
}
