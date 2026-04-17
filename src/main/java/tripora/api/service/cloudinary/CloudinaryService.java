package tripora.api.service.cloudinary;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadFile(MultipartFile file);
    void deleteFile(String imageUrl);
    String extractPublicId(String url);
}