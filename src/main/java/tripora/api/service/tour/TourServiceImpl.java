

package tripora.api.service.tour;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tripora.api.domain.*;
import tripora.api.dto.*;
import tripora.api.exception.BadRequestException;
import tripora.api.exception.ConflictException;
import tripora.api.exception.ResourceNotFoundException;
import tripora.api.mapper.TourMapper;
import tripora.api.repository.*;
import tripora.api.service.cloudinary.CloudinaryService;


import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final CategoryRepository categoryRepository;
    private final TourImageRepository tourImageRepository;
    private final ItineraryRepository itineraryRepository;
    private final TransportOptionRepository transportOptionRepository;
    private final TourMapper tourMapper;
    private final CloudinaryService cloudinaryService;

    public TourServiceImpl(
            TourRepository tourRepository,
            CategoryRepository categoryRepository,
            TourImageRepository tourImageRepository,
            ItineraryRepository itineraryRepository,
            TourMapper tourMapper,
            CloudinaryService cloudinaryService,
            TransportOptionRepository transportOptionRepository
    ) {
        this.tourRepository = tourRepository;
        this.categoryRepository = categoryRepository;
        this.tourImageRepository = tourImageRepository;
        this.itineraryRepository = itineraryRepository;
        this.tourMapper = tourMapper;
        this.cloudinaryService = cloudinaryService;
        this.transportOptionRepository = transportOptionRepository;
    }

    //TOUR

    @Override
    @Transactional(readOnly = true)
    public Page<TourFlatResponse> getAll(int pageNum, int pageSize,
                                         String categorySlug,
                                         String province,
                                         String city) {

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return tourRepository.findAllWithFilters(categorySlug, province, city, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public TourResponse getById(Integer id) {
        Tour tour = getTourOrThrow(id);
        return tourMapper.mapToTourResponse(tour);
    }

    @Override
    public TourResponse createTour(TourRequest req) {

        validateDuration(req.durationDay(), req.durationNight());

        Category category = getCategory(req.categoryId());

        if (tourRepository.existsDuplicateTitle(req.title(), req.city())) {
            throw new ConflictException("Duplicate tour title in city");
        }

        Tour tour = new Tour();
        tour.setTitle(req.title().trim());
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

        Tour tour = getTourOrThrow(id);
        if (tourRepository.existsDuplicateTitle(req.title(), req.city())) {
            throw new ConflictException("Duplicate tour title in city");
        }

        if (!tour.getIsActive())
            throw new ConflictException("Tour inactive");

        if (req.title() != null)
            tour.setTitle(req.title().trim());

        if (req.description() != null)
            tour.setDescription(req.description());

        if (req.durationDay() != null || req.durationNight() != null) {

            int day = req.durationDay() != null ? req.durationDay() : tour.getDurationDay();
            int night = req.durationNight() != null ? req.durationNight() : tour.getDurationNight();

            validateDuration(day, night);

            long maxDay = itineraryRepository.findMaxDayNumberByTourId(id).orElse(0L);

            if (day < maxDay)
                throw new ConflictException("Duration too small for itinerary");

            tour.setDurationDay(day);
            tour.setDurationNight(night);
        }

        if (req.city() != null) tour.setCity(req.city());
        if (req.province() != null) tour.setProvince(req.province());
        if (req.coverImage() != null) tour.setCoverImage(req.coverImage());
        if (req.isActive() != null) tour.setIsActive(req.isActive());

        if (req.categoryId() != null)
            tour.setCategory(getCategory(req.categoryId()));

        return tourMapper.mapToTourResponse(tourRepository.save(tour));
    }

    @Override
    public void deleteTour(Integer id) {
        Tour tour = getTourOrThrow(id);

        if (!tour.getIsActive())
            throw new ConflictException("Already inactive");

        tour.setIsActive(false);
        tourRepository.save(tour);
    }



    // IMAGES

    @Override
    public TourImageResponse addImage(Integer tourId, TourImageRequest req) {

        Tour tour = getActiveTour(tourId);

        if (tourImageRepository.existsByTourIdAndSortOrder(tourId, req.sortOrder()))
            throw new ConflictException("Sort order exists");

        TourImage image = new TourImage();
        image.setTour(tour);
        image.setImageUrl(req.imageUrl().trim());
        image.setSortOrder(req.sortOrder());

        return mapImage(tourImageRepository.save(image), tourId);
    }

    @Override
    public TourImageResponse uploadImage(Integer tourId, MultipartFile file, Integer sortOrder) {

        Tour tour = getActiveTour(tourId);

        if (tourImageRepository.existsByTourIdAndSortOrder(tourId, sortOrder))
            throw new ConflictException("Sort order exists");

        String url = cloudinaryService.uploadFile(file);

        TourImage image = new TourImage();
        image.setTour(tour);
        image.setImageUrl(url);
        image.setSortOrder(sortOrder);

        return mapImage(tourImageRepository.save(image), tourId);
    }

    @Override
    public void removeImage(Integer tourId, Integer imageId) {

        getActiveTour(tourId);

        TourImage image = tourImageRepository.findByIdAndTourId(imageId, tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        cloudinaryService.deleteFile(image.getImageUrl());
        tourImageRepository.delete(image);
    }

    @Override
    public void reorderImages(Integer tourId, TourImageReorderBatchRequest req) {

        getActiveTour(tourId);

        List<TourImage> images = req.items().stream()
                .map(item -> {
                    TourImage img = tourImageRepository
                            .findByIdAndTourId(item.imageId(), tourId)
                            .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

                    img.setSortOrder(item.sortOrder());
                    return img;
                })
                .toList();

        tourImageRepository.saveAll(images);
    }


    @Override
    @Transactional
    public TourResponse updateCoverImage(Integer id, MultipartFile file) {

        Tour tour = getActiveTour(id);

        if (file == null || file.isEmpty())
            throw new BadRequestException("File is required");

        String oldUrl = tour.getCoverImage();

        String newUrl = cloudinaryService.uploadFile(file);
        tour.setCoverImage(newUrl);

        Tour saved = tourRepository.save(tour);

        if (oldUrl != null && !oldUrl.isBlank()) {
            cloudinaryService.deleteFile(oldUrl);
        }

        return tourMapper.mapToTourResponse(saved);
    }
    // ITINERARY
    @Override
    public ItineraryResponse addItineraryDay(Integer tourId, ItineraryRequest req) {

        Tour tour = getActiveTour(tourId);

        if (req.dayNumber() > tour.getDurationDay())
            throw new ConflictException("Day exceeds duration");

        if (itineraryRepository.existsByTourIdAndDayNumber(tourId, req.dayNumber()))
            throw new ConflictException("Day already exists");

        Itinerary it = new Itinerary();
        it.setTour(tour);
        it.setDayNumber(req.dayNumber());
        it.setTitle(req.title().trim());
        it.setDescription(req.description());

        return mapItinerary(itineraryRepository.save(it));
    }

    @Override
    public ItineraryResponse updateItineraryDay(Integer tourId, Integer dayId, UpdateItineraryRequest req) {

        Tour tour = getActiveTour(tourId);
        if (req.dayNumber() > tour.getDurationDay())
            throw new ConflictException("Day exceeds duration");

        if (itineraryRepository.existsByTourIdAndDayNumber(tourId, req.dayNumber()))
            throw new ConflictException("Day already exists");

        Itinerary it = itineraryRepository.findByIdAndTourId(dayId, tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        if (req.dayNumber() != null &&
                itineraryRepository.existsByTourIdAndDayNumber(tourId, req.dayNumber())) {
            throw new ConflictException("Duplicate day");
        }

        if (req.dayNumber() != null) it.setDayNumber(req.dayNumber());
        if (req.title() != null) it.setTitle(req.title());
        if (req.description() != null) it.setDescription(req.description());

        return mapItinerary(itineraryRepository.save(it));
    }

    @Override
    public void removeItineraryDay(Integer tourId, Integer dayId) {

        getActiveTour(tourId);

        Itinerary it = itineraryRepository.findByIdAndTourId(dayId, tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        itineraryRepository.delete(it);
    }

    // TRANSPORT

    @Override
    public TransportOptionResponse addTransportOption(Integer tourId, TransportOptionRequest req) {

        Tour tour = getActiveTour(tourId);

        if (req.minGuests() > req.maxGuests()) {
            throw new BadRequestException("Min guests cannot be greater than max guests");
        }


        TransportOption opt = new TransportOption();
        opt.setTour(tour);
        opt.setVehicleType(req.vehicleType().trim());
        opt.setMinGuests(req.minGuests());
        opt.setMaxGuests(req.maxGuests());
        opt.setGroupPrice(req.groupPrice());
        opt.setNote(req.note());

        return tourMapper.mapTransport(transportOptionRepository.save(opt));
    }

    @Override
    public TransportOptionResponse updateTransportOption(Integer tourId, Integer optId, TransportOptionRequest req) {

        getActiveTour(tourId);

        TransportOption opt = transportOptionRepository.findByIdAndTourId(optId, tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        opt.setVehicleType(req.vehicleType());
        opt.setMinGuests(req.minGuests());
        opt.setMaxGuests(req.maxGuests());
        opt.setGroupPrice(req.groupPrice());
        opt.setNote(req.note());

        return tourMapper.mapTransport(transportOptionRepository.save(opt));
    }

    @Override
    public void deleteTransportOption(Integer tourId, Integer optId) {

        getActiveTour(tourId);

        TransportOption opt = transportOptionRepository.findByIdAndTourId(optId, tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        transportOptionRepository.delete(opt);
    }

    @Override
    public List<TransportOptionResponse> getTransportOptions(Integer tourId) {

        getActiveTour(tourId);

        return transportOptionRepository.findByTourId(tourId)
                .stream()
                .map(tourMapper::mapTransport)
                .toList();
    }

    // HELPERS

    private Tour getTourOrThrow(Integer id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found"));
    }

    private Tour getActiveTour(Integer id) {
        return tourRepository.findById(id)
                .map(tour -> {
                    if (!tour.getIsActive()) {
                        throw new ConflictException("Tour inactive");
                    }
                    return tour;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found"));
    }

    private Category getCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!Boolean.TRUE.equals(category.getIsActive())) {
            throw new ConflictException("Cannot create tour under inactive category");
        }

        return category;
    }


    private TourImageResponse mapImage(TourImage img, Integer tourId) {
        return TourImageResponse.builder()
                .id(img.getId())
                .imageUrl(img.getImageUrl())
                .sortOrder(img.getSortOrder())
                .tourId(tourId)
                .build();
    }

    private ItineraryResponse mapItinerary(Itinerary it) {
        return ItineraryResponse.builder()
                .id(it.getId())
                .dayNumber(it.getDayNumber())
                .title(it.getTitle())
                .description(it.getDescription())
                .build();
    }

    private void validateDuration(int day, int night) {

        if (day <= 0 || night < 0)
            throw new BadRequestException("Invalid duration values");

        if (night > day)
            throw new BadRequestException("Nights cannot be greater than days");


        if (night != day && night != day - 1) {
            throw new BadRequestException(
                    "Invalid duration: nights must be equal to (days or days - 1)"
            );
        }
    }
}