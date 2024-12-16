package com.example.spring_boot.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MediaService {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    public String saveMedia(String mediaUrl, String contentType) throws IOException {
        try {   
            String uploadedFilePath = uploadMediaFile(mediaUrl, contentType);
            return uploadedFilePath;
        } catch (IOException e) {
            throw e;
        }
    }

    // Takes in the file data and file name; sets a path and saves the file data to that path
    // Returns uploaded file url
    // Throws error if fails
    private String saveToLocalFile(byte[] fileData, String fileName) throws IOException {
        try {
            Path path = Paths.get("/opt/twilio_demo_api/uploads", fileName);
            Files.write(path, fileData);
            return path.toString();
        } catch (IOException e) {
            throw e;
        }
    }

    private String uploadMediaFile(String mediaUrl, String contentType) throws IOException {
        try {
            // Download the file
            byte[] mediaData = downloadFile(mediaUrl);

            // Generate a new file name
           String uuid = UUID.randomUUID().toString();
           String fileName = uuid + extractFileExtension(contentType);

            // Save the file
            String newMediaUrl = saveToLocalFile(mediaData, fileName);
            
            // Return the new url to find the uploaded file
            return newMediaUrl;
        } catch (IOException e) {
            throw e;
        }
    }

    private String extractFileExtension(String mimeType) {
        if (mimeType == null || !mimeType.contains("/")) {
            return ".bin";
        }
        String[] parts = mimeType.split("/");
        if (parts.length != 2) {
            return ".bin";
        }
        return "." + parts[1];
    }

    private byte[] downloadFile(String fileUrl) throws IOException {
        URI uri = URI.create(fileUrl);

        URL url = uri.toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);

        String authentication = accountSid + ":" + authToken;
        String encodeAuthentication = Base64.getEncoder().encodeToString(authentication.getBytes(StandardCharsets.UTF_8));

        connection.setRequestProperty("Authorization", "Basic " + encodeAuthentication);

        InputStream inputStream = connection.getInputStream();

        byte[] fileData = inputStream.readAllBytes();

        inputStream.close();

        return fileData;
    }
}
