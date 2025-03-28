package ge.giorgi.springbootdemo.car.cloudinary;


import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import ge.giorgi.springbootdemo.car.error.ImageAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);
    private static final String IMAGE_URL_PATTERN = "https?://.*\\.(jpg|jpeg|png|gif|bmp)$";  // Regex for valid image URL format
    private final RestTemplate restTemplate;
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {
        logger.info("Starting image upload for file: {}", file.getOriginalFilename());

        // Generate a unique ID for the file based on the file name
        String publicId = generatePublicIdFromFile(file);

        // Check if the image already exists in Cloudinary by public ID
        if (imageExistsInCloudinary(publicId)) {
            logger.info("Image already exists in Cloudinary. Skipping upload.");
            throw new ImageAlreadyExistsException(
                    "Image already exists in Cloudinary: " + publicId); // Return the existing image ID
        }

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("public_id", publicId));
            String imageUrl = uploadResult.get("secure_url").toString();

            logger.info("Image upload successful. Image URL: {}", imageUrl);
            return imageUrl;
        } catch (IOException e) {
            logger.error("Image upload failed for file: {}. Error: {}", file.getOriginalFilename(), e.getMessage());
            throw new RuntimeException("Image upload failed: " + e.getMessage(), e);
        }
    }

    private String generatePublicIdFromFile(MultipartFile file) {
        // Create a unique public ID based on the file name
        return "car_images/" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9]", "_");
    }

    private boolean imageExistsInCloudinary(String publicId) {
        try {
            // Check if the image exists in Cloudinary by public ID
            Map result = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
            if (result != null && !result.isEmpty()) {
                return true; // Image Exists
            } else
                return false;
        } catch (Exception e) {
            logger.warn("Image with public ID {} not found in Cloudinary. Proceeding with upload.", publicId);
            return false;  // Image does not exist
        }
    }

    public String imageValidation(String imageUrl) {
        isValidImageUrlFormat(imageUrl);
        isImageUrlValid(imageUrl);
        return imageUrl;
    }

    private void isValidImageUrlFormat(String url) {
        try {
            Pattern pattern = Pattern.compile(IMAGE_URL_PATTERN);
            Matcher matcher = pattern.matcher(url);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Invalid image URL format. It must be a valid image URL (jpg, png, etc.).");
            }
        } catch (PatternSyntaxException e) {
            logger.error("Invalid regex pattern for image URL format: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid regex pattern for image URL format.", e);
        }
    }

    private void isImageUrlValid(String url) {
        try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.HEAD, null, Void.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Image URL does not exist or is not accessible.");
            }
        } catch (Exception e) {
            logger.error("Error while validating image URL: {}", e.getMessage());
            throw new IllegalArgumentException("Image URL does not exist or is not accessible.", e);
        }
    }
}
