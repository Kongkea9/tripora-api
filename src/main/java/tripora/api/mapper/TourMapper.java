

package tripora.api.mapper;

import org.springframework.stereotype.Component;
import tripora.api.domain.*;
import tripora.api.dto.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class TourMapper {

    private final CategoryMapper categoryMapper;

    public TourMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public TourResponse mapToTourResponse(Tour tour) {

        if (tour == null) return null;

        return TourResponse.builder()
                .id(tour.getId())
                .title(tour.getTitle())
                .description(tour.getDescription())
                .durationDay(tour.getDurationDay())
                .durationNight(tour.getDurationNight())
                .whatsIncluded(tour.getWhatsIncluded())
                .whatExcluded(tour.getWhatExcluded())
                .province(tour.getProvince())
                .city(tour.getCity())
                .coverImage(tour.getCoverImage())
                .isActive(tour.getIsActive())
                .createdAt(tour.getCreatedAt())

                .category(
                        tour.getCategory() == null
                                ? null
                                : categoryMapper.mapFromCategoryEntityoCategoryResponse(tour.getCategory())
                )

                .images(mapImages(tour.getTourImages()))
                .itineraries(mapItineraries(tour.getItineraries()))
                .transportOptions(
                        tour.getTransportOptions() == null
                                ? Collections.emptyList()
                                : tour.getTransportOptions().stream()
                                .map(this::mapTransport)
                                .toList()
                )
                .build();
    }


    // IMAGES
    private List<TourImageResponse> mapImages(Collection<TourImage> images) {

        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        return images.stream()
                .map(img -> TourImageResponse.builder()
                        .id(img.getId())
                        .imageUrl(img.getImageUrl())
                        .sortOrder(img.getSortOrder())
                        .tourId(img.getTour() != null ? img.getTour().getId() : null)
                        .build()
                )
                .toList();
    }


    // ITINERARIES
    private List<ItineraryResponse> mapItineraries(Collection<Itinerary> itineraries) {

        if (itineraries == null || itineraries.isEmpty()) {
            return Collections.emptyList();
        }

        return itineraries.stream()
                .map(it -> ItineraryResponse.builder()
                        .id(it.getId())
                        .dayNumber(it.getDayNumber())
                        .title(it.getTitle())
                        .description(it.getDescription())
                        .build()
                )
                .toList();
    }


    public TransportOptionResponse mapTransport(TransportOption t) {
        return TransportOptionResponse.builder()
                .id(t.getId())
                .vehicleType(t.getVehicleType())
                .minGuests(t.getMinGuests())
                .maxGuests(t.getMaxGuests())
                .groupPrice(t.getGroupPrice())
                .note(t.getNote())
                .build();
    }



}