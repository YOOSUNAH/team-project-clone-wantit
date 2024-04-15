package io.dcns.wantitauction.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import io.dcns.wantitauction.domain.notification.dto.NotificationRequestDto;
import io.dcns.wantitauction.domain.notification.entity.Notification;
import io.dcns.wantitauction.domain.notification.repository.NotificationRepository;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.domain.user.repository.UserRepository;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Notification")
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveNotification(String token) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            String email = ((UserDetailsImpl) principal).getUser().getEmail();
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저"));
            Notification notification = Notification.builder()
                .token(token)
                .userId(user.getUserId())
                .build();

            notificationRepository.save(notification);
        } else {
            throw new IllegalArgumentException("현재 인증된 사용자를 찾을 수 없습니다.");
        }

    }

    public String getNotificationToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            String email = ((UserDetailsImpl) principal).getUser().getEmail();
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저"));

            Notification notification = notificationRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저"));

            return notification.getToken();
        } else {
            throw new IllegalArgumentException("현재 인증된 사용자를 찾을 수 없습니다.");
        }
    }

    public void sendNotification(NotificationRequestDto notificationRequestDto)
        throws ExecutionException, InterruptedException {
        String token = getNotificationToken();
        Message message = Message.builder()
            .setWebpushConfig(WebpushConfig.builder()
                .setNotification(WebpushNotification.builder()
                    .setTitle(notificationRequestDto.getTitle())
                    .setBody(notificationRequestDto.getMessage())
                    .build())
                .build())
            .setToken(token)
            .build();

        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        log.info("메시지 전송 성공 : " + response);
    }
}
