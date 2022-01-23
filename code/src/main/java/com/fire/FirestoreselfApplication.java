package com.fire;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@SpringBootApplication
public class FirestoreselfApplication {

	public static final String UserProfile = "FmlUserProfiles";
	public static final String Lease = "LeaseInfo";
	public static final String UserLeases = "UserLeases";
	public static final String Wishlist = "Wishlist";
	public static final String LRO = "LRO";
	public static final String LRU = "LRU";
	public static final String UserIdCookie = "fmlUname";
	
	public static void main(String[] args) throws IOException {
		ClassLoader cl = FirestoreselfApplication.class.getClassLoader();
		Path temp = Files.createTempFile("fsfbkey-", ".json");
		Files.copy(cl.getResourceAsStream("fsfbkey.json"), temp, StandardCopyOption.REPLACE_EXISTING);
		FileInputStream input = new FileInputStream(temp.toString());
		FirebaseOptions options = new FirebaseOptions.Builder()
				  .setCredentials(GoogleCredentials.fromStream(input))
				  .build();
		
		if(FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
		
		SpringApplication.run(FirestoreselfApplication.class, args);
	}

}
