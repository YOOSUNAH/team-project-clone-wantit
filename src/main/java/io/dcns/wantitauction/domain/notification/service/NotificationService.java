package io.dcns.wantitauction.domain.notification.service;

import io.dcns.wantitauction.domain.notification.entity.Notification;
import io.dcns.wantitauction.domain.notification.repository.NotificationRepository;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.domain.user.repository.UserRepository;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Notification")
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveNotification(String token, UserDetailsImpl userDetails) {
        User user = getUser(userDetails);
        Notification notification = Notification.builder()
            .token(token)
            .userId(user.getUserId())
            .build();

        notificationRepository.save(notification);
    }

    public String getNotificationToken(UserDetailsImpl userDetails) {
        User user = getUser(userDetails);
        Notification notification = notificationRepository.findByUserId(user.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저"));

        return notification.getToken();
    }

    private User getUser(UserDetailsImpl userDetails) {
        return userRepository.findByEmail(userDetails.getUser().getEmail())
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저"));
    }
}
