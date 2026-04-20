package tripora.api.service.booking;

import tripora.api.dto.BookingRequest;
import tripora.api.dto.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingRequest req);

    List<BookingResponse> getMyBookings();

    BookingResponse getMyBookingDetail(Integer bookingId);

    void cancelMyBooking(Integer bookingId);

    List<BookingResponse> getAll();

    BookingResponse approve(Integer bookingId);

    void adminCancel(Integer bookingId);
}