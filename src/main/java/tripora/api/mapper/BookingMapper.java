package tripora.api.mapper;

import org.springframework.stereotype.Component;
import tripora.api.domain.Booking;
import tripora.api.dto.BookingResponse;


@Component
public class BookingMapper {

    public BookingResponse toResponse(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getTour().getId(),
                b.getTour().getTitle(),
                b.getUser().getId(),
                b.getGuide() != null ? b.getGuide().getId() : null,
                b.getTransportOption() != null ? b.getTransportOption().getId() : null,
                b.getTravelDate(),
                b.getGuestCount(),
                b.getTotalPrice(),
                b.getStatus().toString(),
                b.getBookedAt()
        );
    }
}