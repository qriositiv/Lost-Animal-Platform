package com.straysafe.backend.service;

import com.straysafe.backend.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Base64;

@Service
public class ImageParserService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageParserService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    private static final byte[] PNG_SIGNATURE = new byte[] {
            (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
    };

    public void saveImage(byte[] byteImage, String petId) throws SQLException, IOException {
        String os = System.getProperty("os.name").toLowerCase();
        String user = System.getProperty("user.name");
        String userHome = System.getProperty("user.home");
        String baseDirectory;

        if (os.contains("win")) {
            // Windows OS
            baseDirectory = userHome + "\\Documents\\.straysafe\\";
        } else {
            // Assuming Linux or Unix-based OS
            baseDirectory = "/home/" + user + "/.straysafe/";
        }
        // Create the .straysafe directory if not exist
        File dir = new File(baseDirectory);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Failed to create directory: " + baseDirectory);
            }
        }

        // Construct the image path
        String imagePath = baseDirectory + petId + ".png";

        File tmpFile = new File(imagePath);
        try (FileOutputStream fos = new FileOutputStream(tmpFile)) {
            fos.write(byteImage);
        } catch (IOException e) {
            throw new IOException("Failed to save image to disk: " + e.getMessage(), e);
        }

        try {
            imageRepository.saveImage(imagePath, petId);
        } catch (Exception e) {
            if (!tmpFile.delete() && tmpFile.exists()) {
                throw new SQLException(
                        "Failed to save image in database and failed to delete temporary file: " + e.getMessage(), e);
            }
            throw new SQLException("Failed to save image in database: " + e.getMessage(), e);
        }
    }

    public boolean isPngImage(byte[] byteImage) {
        if (byteImage.length < PNG_SIGNATURE.length) {
            return false;
        }

        for (int i = 0; i < PNG_SIGNATURE.length; i++) {
            if (byteImage[i] != PNG_SIGNATURE[i]) {
                return false;
            }
        }

        return true;
    }

    public String convertPetImagePathToPetBase64(String imagePath) {
        Path path = Paths.get(imagePath);
        byte[] imageBytes = null;

        try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(path))) {
            imageBytes = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

}
