package com.gws.crm.core.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class NotificationInitializer {

    @Value("${firebase.config.path}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() throws IOException {
        InputStream serviceAccount = new ClassPathResource(firebaseConfigPath).getInputStream();

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        FirebaseApp.initializeApp(options);

        log.info("Firebase initialized successfully with service account: {}", credentials.getClass().getName());
        log.info("Firebase Apps count = {}", FirebaseApp.getApps().size());
        log.info("Default FirebaseApp name = {}", FirebaseApp.getInstance().getName());
    }
}
