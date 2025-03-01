package com.reachout.ReachoutSystem.configurations;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import com.google.auth.oauth2.GoogleCredentials;
import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private Firestore firestore;

    @PostConstruct
    public void FirestoreInitializer() throws IOException {

        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/firebaseServiceAccount.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("reachout-2.appspot.com")
                .build();

        FirebaseApp.initializeApp(options);
    }

}
