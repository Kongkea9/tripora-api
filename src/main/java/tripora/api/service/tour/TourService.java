package tripora.api.service.tour;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import tripora.api.dto.*;

import java.util.List;

public interface TourService {

    Page<TourFlatResponse> getAll(int pageNum, int pageSize,
                              String categorySlug, String province, String city);

    Page<TourFlatResponse> getAllForAdmin(int pageNum, int pageSize,
                                  String categorySlug, String province, String city);

    TourResponse getById(Integer id);

    TourResponse getByIdForAdmin(Integer id);

    void updateIsActive(Integer id, Boolean active);


    TourResponse createTour(TourRequest req);

    TourResponse updateTour(Integer id, UpdateTourRequest updateTourRequest);

    void deleteTour(Integer id);

    TourImageResponse addImage(Integer tourId, TourImageRequest tourImageRequest);
    TourImageResponse uploadImage(Integer tourId,
                                  MultipartFile file,
                                  Integer sortOrder);

    void reorderImages(Integer tourId, TourImageReorderBatchRequest request);
    void removeImage(Integer tourId, Integer imageId);

    TourResponse updateCoverImage(Integer id, MultipartFile file);

    ItineraryResponse addItineraryDay(Integer tourId, ItineraryRequest itineraryRequest);

    ItineraryResponse updateItineraryDay(Integer tourId, Integer dayId, UpdateItineraryRequest request);
    void removeItineraryDay(Integer tourId, Integer dayId);


    TransportOptionResponse addTransportOption(Integer tourId, TransportOptionRequest request);

    TransportOptionResponse updateTransportOption(Integer tourId, Integer optId, TransportOptionRequest request);

    void deleteTransportOption(Integer tourId, Integer optId);

    List<TransportOptionResponse> getTransportOptions(Integer tourId);

    //Publish
    TourResponse publishTour(Integer tourId);
}