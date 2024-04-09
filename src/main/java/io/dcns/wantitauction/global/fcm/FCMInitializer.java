package io.dcns.wantitauction.global.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "FCMInitializer")
public class FCMInitializer {

    private static final String FIREBASE_CONFIG_PATH = "firebase/firebase-adminSDK.json";

    @PostConstruct
    public void initialize() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream());
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            log.info("Error initializing Firebase : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
