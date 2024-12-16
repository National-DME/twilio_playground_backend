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
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// Service class; handles business logic of the API
// Media service handles business logic around handling files (MME files sent from the user to twilio, then from twilio to the incoming endpoint)
@Service
public class MediaService {

    // Environment variables used to access twilio
    @Value("${twilio.accountSid}")
    private String accountSid;
    @Value("${twilio.authToken}")
    private String authToken;

    // Method used to combine other helper methods to download; rename and store MME files from user via twilio; currently storing MME files on DO droplet
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

    // Downloads the media file from twilios api; renames the file and returns the local url
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

    // When twilio pings your incoming webhook, instead of sending the files, they send you an endpoint that you can ping to get the file
    // This method calls that endpoint to GET the file from twilio
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
