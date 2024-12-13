package com.example.spring_boot.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_boot.entities.SmsMedia;
import com.example.spring_boot.repository.MediaRepository;

@Service
public class MediaService {
    
    @Autowired
    private MediaRepository mediaRepository;

    public void saveMedia(String mediaUrl, String contentType) throws IOException {
        try {   
            String uploadedFilePath = uploadMediaFile(mediaUrl, contentType);

            SmsMedia smsMedia = new SmsMedia();
            smsMedia.setMediaContentType(contentType);
            smsMedia.setMediaUrl(uploadedFilePath);
            
            mediaRepository.save(smsMedia);
        } catch (IOException e) {
            throw e;
        }
    }

    // Takes in the file data and file name; sets a path and saves the file data to that path
    // Returns uploaded file url
    // Throws error if fails
    private String saveToLocalFile(byte[] fileData, String fileName) throws IOException {
        try {
            Path path = Paths.get("/upload", fileName);
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
            String fileName = UUID.randomUUID().toString();

            // Save the file
            String newMediaUrl = saveToLocalFile(mediaData, fileName);
            
            // Return the new url to find the uploaded file
            return newMediaUrl;
        } catch (IOException e) {
            throw e;
        }
    }

    private byte[] downloadFile(String fileUrl) throws IOException {
        URI uri = URI.create(fileUrl);

        URL url = uri.toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);

        InputStream inputStream = connection.getInputStream();

        byte[] fileData = inputStream.readAllBytes();

        inputStream.close();

        return fileData;
    }
}
