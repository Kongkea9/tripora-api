package tripora.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tripora.api.dto.*;
import tripora.api.service.tour.TourServiceImpl;

import java.util.List;

@Tag(name = "Tours", description = "Tour management APIs")
@RestController
@RequestMapping("v1/api/tours")
@Slf4j
public class TourController {

    private final TourServiceImpl tourService;

    public TourController(TourServiceImpl tourService) {
        this.tourService = tourService;
    }


    @GetMapping
    public Page<TourFlatResponse> getAll(
            @RequestParam(required = false, defaultValue = "0") int pageNum,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String categorySlug,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city
    ) {
        log.info("GET /tours page={}, size={}, categorySlug={}, province={}, city={}",
                pageNum, pageSize, categorySlug, province, city);


        return tourService.getAll(pageNum, pageSize, categorySlug, province, city);

    }


    @GetMapping("/{id}")
    public TourResponse getById(@PathVariable Integer id) {
        log.info("Fetching tour with id: {}", id);
        return tourService.getById(id);
    }



    @PostMapping
    public ResponseEntity<TourResponse> createTour(
            @Valid @RequestBody TourRequest tourRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tourService.createTour(tourRequest));
    }

    @PutMapping("/{id}")
    public TourResponse updateTour(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateTourRequest updateTourRequest
    ) {
        log.info("Updating tour with id: {}", id);
        return tourService.updateTour(id, updateTourRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTour(@PathVariable Integer id) {
        log.info("Soft-deleting tour with id: {}", id);
        tourService.deleteTour(id);
        return ResponseEntity.ok("Tour deleted successfully");
    }

    // Images
    @Operation(
            summary = "Update cover image",
            description = "Replace existing cover image with new file"
    )
    @PutMapping(value = "/{id}/cover", consumes = "multipart/form-data")
    public TourResponse updateCoverImage(

            @Parameter(description = "Tour ID")
            @PathVariable Integer id,

            @Parameter(description = "New cover image file")
            @RequestParam("file") MultipartFile file
    ) {
        return tourService.updateCoverImage(id, file);
    }


    @PostMapping("/{id}/images/url")
    public ResponseEntity<TourImageResponse> addImage(
            @PathVariable Integer id,
            @Valid @RequestBody TourImageRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tourService.addImage(id, request));
    }

    @DeleteMapping("/{id}/images/{imgId}")
    public ResponseEntity<String> removeImage(
            @PathVariable Integer id,
            @PathVariable Integer imgId
    ) {
        log.info("Removing image id: {} from tour id: {}", imgId, id);
        tourService.removeImage(id, imgId);
        return ResponseEntity.ok("Image removed successfully");
    }

    @Operation(
            summary = "Upload tour image",
            description = "Upload image file to Cloudinary and attach to tour"
    )
    @PostMapping(value = "/{id}/images", consumes = "multipart/form-data")
    public ResponseEntity<TourImageResponse> uploadImage(

            @Parameter(description = "Tour ID")
            @PathVariable Integer id,

            @Parameter(description = "Image file", required = true)
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Sort order of image (1,2,3...)")
            @RequestParam Integer sortOrder
    ) {
        TourImageResponse response = tourService.uploadImage(id, file, sortOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(
            summary = "Reorder tour images (drag & drop)",
            description = "Update image sort order after drag and drop UI change"
    )
    @PutMapping("/{id}/images/reorder")
    public ResponseEntity<String> reorderImages(

            @PathVariable Integer id,

            @Valid @RequestBody TourImageReorderBatchRequest request
    ) {
        tourService.reorderImages(id, request);
        return ResponseEntity.ok("Images reordered successfully");
    }

    // Itinerary

    @PostMapping("/{id}/itineraries")
    public ResponseEntity<ItineraryResponse> addItineraryDay(
            @PathVariable Integer id,
            @Valid @RequestBody ItineraryRequest itineraryRequest
    ) {
        log.info("Adding itinerary day to tour id: {}", id);
        ItineraryResponse response = tourService.addItineraryDay(id, itineraryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/itineraries/{dayId}")
    public ItineraryResponse updateItineraryDay(
            @PathVariable Integer id,
            @PathVariable Integer dayId,
            @Valid @RequestBody UpdateItineraryRequest updateItineraryRequest
    ) {
        log.info("Updating itinerary day id: {} for tour id: {}", dayId, id);
        return tourService.updateItineraryDay(id, dayId, updateItineraryRequest);
    }


    @DeleteMapping("/{id}/itineraries/{dayId}")
    public ResponseEntity<String> deleteItineraryDay(
            @PathVariable Integer id,
            @PathVariable Integer dayId
    ){
        tourService.removeItineraryDay(id, dayId);
        return ResponseEntity.ok("Itinerary deleted successfully");


    }


    @GetMapping("/{id}/transport-options")
    public List<TransportOptionResponse> getTransportOptions(@PathVariable Integer id) {
        return tourService.getTransportOptions(id);
    }


    @PostMapping("/{id}/transport-options")
    public ResponseEntity<TransportOptionResponse> addTransportOption(
            @PathVariable Integer id,
            @Valid @RequestBody TransportOptionRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tourService.addTransportOption(id, request));
    }


    @PutMapping("/{id}/transport-options/{optId}")
    public TransportOptionResponse updateTransportOption(
            @PathVariable Integer id,
            @PathVariable Integer optId,
            @Valid @RequestBody TransportOptionRequest request
    ) {
        return tourService.updateTransportOption(id, optId, request);
    }


    @DeleteMapping("/{id}/transport-options/{optId}")
    public ResponseEntity<String> deleteTransportOption(
            @PathVariable Integer id,
            @PathVariable Integer optId
    ) {
        tourService.deleteTransportOption(id, optId);
        return ResponseEntity.ok("Transport option deleted successfully");
    }


}