package tripora.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tripora.api.service.cloudinary.CloudinaryService;

import java.util.Map;

@RestController
@RequestMapping("v1/api/files")
@RequiredArgsConstructor

@Slf4j
public class FileUploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {

        try {
            log.info("Uploading file: name={}, size={}",
                    file.getOriginalFilename(), file.getSize());

            String url = cloudinaryService.uploadFile(file);

            return ResponseEntity.ok(Map.of("url", url));

        } catch (Exception e) {
            log.error("Cloudinary upload failed", e);

            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }
}