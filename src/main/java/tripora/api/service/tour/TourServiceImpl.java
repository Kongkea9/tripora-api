package tripora.api.service.tour;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tripora.api.domain.*;
import tripora.api.dto.*;
import tripora.api.exception.ConflictException;
import tripora.api.exception.ResourceNotFoundException;
import tripora.api.mapper.CategoryMapper;
import tripora.api.mapper.TourMapper;
import tripora.api.repository.*;
import tripora.api.service.cloudinary.CloudinaryService;

import java.time.LocalDate;
import java.util.List;


@Service
@Slf4j
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final CategoryRepository categoryRepository;
    private final TourImageRepository tourImageRepository;
    private final ItineraryRepository itineraryRepository;
    private final TourMapper tourMapper;
    private final CloudinaryService cloudinaryService;
    private final CategoryMapper categoryMapper;
    private final TransportOptionRepository transportOptionRepository;


    public TourServiceImpl(
            TourRepository tourRepository,
            CategoryRepository categoryRepository,
            TourImageRepository tourImageRepository,
            ItineraryRepository itineraryRepository,
            TourMapper tourMapper,
            CloudinaryService cloudinaryService,
            CategoryMapper categoryMapper, TransportOptionRepository transportOptionRepository) {
        this.tourRepository = tourRepository;
        this.categoryRepository = categoryRepository;
        this.tourImageRepository = tourImageRepository;
        this.itineraryRepository = itineraryRepository;
        this.tourMapper = tourMapper;
        this.cloudinaryService = cloudinaryService;
        this.categoryMapper = categoryMapper;
        this.transportOptionRepository = transportOptionRepository;
    }


    @Override
    public Page<TourFlatResponse> getAll(int pageNum, int pageSize,
                                         String categorySlug,
                                         String province,
                                         String city) {

        Pageable pageable = PageRequest.of(pageNum, pageSize);

        return tourRepository.findAllWithFilters(
                categorySlug,
                province,
                city,
                pageable
        );
    }


    @Override
    @Transactional(readOnly = true)
    public TourResponse getById(Integer id) {

        Tour tour = tourRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found"));

        return tourMapper.mapToTourResponse(tour);
    }


    @Override
    public TourResponse createTour(TourRequest req) {

        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Tour tour = new Tour();
        tour.setTitle(req.title());
        tour.setDescription(req.description());
        tour.setDurationDay(req.durationDay());
        tour.setDurationNight(req.durationNight());
        tour.setWhatsIncluded(req.whatsIncluded());
        tour.setWhatExcluded(req.whatExcluded());
        tour.setProvince(req.province());
        tour.setCity(req.city());

        tour.setCoverImage(req.coverImage());
        tour.setIsActive(req.isActive());
        tour.setCategory(category);
        tour.setCreatedAt(LocalDate.now());

        return tourMapper.mapToTourResponse(tourRepository.save(tour));
    }
    @Override
    public TourResponse updateTour(Integer id, UpdateTourRequest req) {

        Tour tour = findTourById(id);

        if (req.title() != null) tour.setTitle(req.title());
        if (req.description() != null) tour.setDescription(req.description());
        if (req.durationDay() != null) tour.setDurationDay(req.durationDay());
        if (req.durationNight() != null) tour.setDurationNight(req.durationNight());
        if (req.whatsIncluded() != null) tour.setWhatsIncluded(req.whatsIncluded());
        if (req.whatExcluded() != null) tour.setWhatExcluded(req.whatExcluded());
        if (req.province() != null) tour.setProvince(req.province());
        if (req.city() != null) tour.setCity(req.city());
        if (req.coverImage() != null) tour.setCoverImage(req.coverImage());
        if (req.isActive() != null) tour.setIsActive(req.isActive());

        if (req.categoryId() != null) {
            Category category = categoryRepository.findById(req.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found"));
            tour.setCategory(category);
        }

        return tourMapper.mapToTourResponse(tourRepository.save(tour));
    }

    @Override
    public void deleteTour(Integer id) {
        Tour tour = findTourById(id);
        tour.setIsActive(false);
        tourRepository.save(tour);
    }
    @Override
    public TourImageResponse addImage(Integer tourId, TourImageRequest req) {

        Tour tour = findTourById(tourId);

        if (tourImageRepository.existsByTourIdAndSortOrder(tourId, req.sortOrder())) {
            throw new ConflictException("Sort order already exists");
        }


        TourImage image = new TourImage();
        image.setTour(tour);
        image.setImageUrl(req.imageUrl());
        image.setSortOrder(req.sortOrder());

        TourImage saved = tourImageRepository.save(image);

        return TourImageResponse.builder()
                .id(saved.getId())
                .imageUrl(saved.getImageUrl())
                .sortOrder(saved.getSortOrder())
                .tourId(tourId)
                .build();
    }


    // IMAGES (CLOUDINARY)

    @Override
    public TourImageResponse uploadImage(Integer tourId,
                                         MultipartFile file,
                                         Integer sortOrder) {

        Tour tour = findTourById(tourId);

        if (!tour.getIsActive()) {
            throw new ConflictException("Cannot upload image in inactive tour");
        }

        if (tourImageRepository.existsByTourIdAndSortOrder(tourId, sortOrder)) {
            throw new ConflictException("Sort order already exists");
        }

        String imageUrl = cloudinaryService.uploadFile(file);

        TourImage image = new TourImage();
        image.setTour(tour);
        image.setImageUrl(imageUrl);
        image.setSortOrder(sortOrder);

        TourImage saved = tourImageRepository.save(image);

        return TourImageResponse.builder()
                .id(saved.getId())
                .imageUrl(saved.getImageUrl())
                .sortOrder(saved.getSortOrder())
                .tourId(tourId)
                .build();
    }

    @Override
    public void removeImage(Integer tourId, Integer imageId) {

        Tour tour = findTourById(tourId);
        if (!tour.getIsActive()) {
            throw new ConflictException("Cannot remove image in inactive tour");
        }

        TourImage image = tourImageRepository.findByIdAndTourId(imageId, tourId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Image not found"));


        cloudinaryService.deleteFile(image.getImageUrl());


        tourImageRepository.delete(image);
    }

    @Override
    public TourResponse updateCoverImage(Integer id, MultipartFile file) {

        Tour tour = findTourById(id);
        if (!tour.getIsActive()) {
            throw new ConflictException("Cannot update cover image in inactive tour");
        }

        if (tour.getCoverImage() != null) {
            cloudinaryService.deleteFile(tour.getCoverImage());
        }

        String newUrl = cloudinaryService.uploadFile(file);
        tour.setCoverImage(newUrl);

        return tourMapper.mapToTourResponse(tourRepository.save(tour));
    }


    // ITINERARY

    @Override
    public ItineraryResponse addItineraryDay(Integer tourId, ItineraryRequest req) {

        Tour tour = findTourById(tourId);
        if (!tour.getIsActive()) {
            throw new ConflictException("Cannot add itinerary day in inactive tour");
        }


        if (req.dayNumber() > tour.getDurationDay()) {
            throw new ConflictException(
                    "Day number cannot exceed tour duration (" + tour.getDurationDay() + ")"
            );
        }

        if (itineraryRepository.existsByTourIdAndDayNumber(tourId, req.dayNumber())) {
            throw new ConflictException("Day already exists");
        }


        Itinerary itinerary = new Itinerary();
        itinerary.setTour(tour);
        itinerary.setDayNumber(req.dayNumber());
        itinerary.setTitle(req.title());
        itinerary.setDescription(req.description());

        Itinerary saved = itineraryRepository.save(itinerary);

        return ItineraryResponse.builder()
                .id(saved.getId())
                .dayNumber(saved.getDayNumber())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .build();
    }

    @Override
    public void reorderImages(Integer tourId, TourImageReorderBatchRequest request) {

        Tour tour = findTourById(tourId);
        if (!tour.getIsActive()) {
            throw new ConflictException("Cannot reorder images in inactive tour");
        }

        List<TourImage> updated = request.items().stream()
                .map(item -> {
                    TourImage image = (TourImage) tourImageRepository.findByIdAndTourId(item.imageId(), tourId)
                            .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

                    image.setSortOrder(item.sortOrder());
                    return image;
                })
                .toList();

        tourImageRepository.saveAll(updated);
    }


    @Override
    public ItineraryResponse updateItineraryDay(Integer tourId,
                                                Integer dayId,
                                                UpdateItineraryRequest req) {

        Tour tour = findTourById(tourId);
        if (!tour.getIsActive()) {
            throw new ConflictException("Cannot update itinerary day in inactive tour");
        }

        Itinerary itinerary = itineraryRepository.findByIdAndTourId(dayId, tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        if (req.dayNumber() != null &&
                !req.dayNumber().equals(itinerary.getDayNumber()) &&
                itineraryRepository.existsByTourIdAndDayNumber(tourId, req.dayNumber())) {
            throw new ConflictException("Another itinerary already exists for day " + req.dayNumber());
        }

        if (req.dayNumber() != null) itinerary.setDayNumber(req.dayNumber());
        if (req.title() != null) itinerary.setTitle(req.title());
        if (req.description() != null) itinerary.setDescription(req.description());

        Itinerary updated = itineraryRepository.save(itinerary);

        return ItineraryResponse.builder()
                .id(updated.getId())
                .dayNumber(updated.getDayNumber())
                .title(updated.getTitle())
                .description(updated.getDescription())
                .build();
    }


    @Override
    public void removeItineraryDay(Integer tourId, Integer dayId) {

        Tour tour = findTourById(tourId);
        if (!tour.getIsActive()) {
            throw new ConflictException("Cannot remove itinerary day in inactive tour");
        }

        Itinerary itinerary = itineraryRepository.findByIdAndTourId(dayId, tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));


        itineraryRepository.delete(itinerary);

    }




    @Override
    public TransportOptionResponse addTransportOption(Integer tourId, TransportOptionRequest req) {

        Tour tour = findTourById(tourId);
        if (!tour.getIsActive()) {
            throw new ConflictException("Cannot add transport option in inactive tour");
        }


        TransportOption option = new TransportOption();
        option.setTour(tour);
        option.setVehicleType(req.vehicleType());
        option.setMinGuests(req.minGuests());
        option.setMaxGuests(req.maxGuests());
        option.setGroupPrice(req.groupPrice());
        option.setNote(req.note());

        TransportOption saved = transportOptionRepository.save(option);

        return tourMapper.mapTransport(saved);
    }


    @Override
    public List<TransportOptionResponse> getTransportOptions(Integer tourId) {

        Tour tour = findTourById(tourId);
        if (!tour.getIsActive()) {
            throw new ConflictException("Cannot get get transport options inactive tour");
        }
        return transportOptionRepository.findByTourId(tourId)
                .stream()
                .map(tourMapper::mapTransport)
                .toList();
    }


    @Override
    public TransportOptionResponse updateTransportOption(Integer tourId, Integer optId, TransportOptionRequest req) {

        Tour tour = findTourById(tourId);
        if (!tour.getIsActive()) {
            throw new ConflictException("Cannot update transport option in inactive tour");
        }

        TransportOption option = transportOptionRepository
                .findByIdAndTourId(optId, tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Transport not found"));

        option.setVehicleType(req.vehicleType());
        option.setMinGuests(req.minGuests());
        option.setMaxGuests(req.maxGuests());
        option.setGroupPrice(req.groupPrice());
        option.setNote(req.note());

        return tourMapper.mapTransport(transportOptionRepository.save(option));
    }


    @Override
    public void deleteTransportOption(Integer tourId, Integer optId) {

        Tour tour = findTourById(tourId);
        if (!tour.getIsActive()) {
            throw new ConflictException("Cannot delete transport option in inactive tour");
        }

        TransportOption option = transportOptionRepository
                .findByIdAndTourId(optId, tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Transport not found"));

        transportOptionRepository.delete(option);
    }



    private Tour findTourById(Integer id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tour with id '" + id + "' not found"));
    }
}