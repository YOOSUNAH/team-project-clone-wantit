package io.dcns.wantitauction.domain.notification.repository;

import io.dcns.wantitauction.domain.notification.entity.Notification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByUserId(Long userId);
}
