package tripora.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import tripora.api.dto.ApproveBookingRequest;
import tripora.api.dto.BookingRequest;
import tripora.api.dto.BookingResponse;
import tripora.api.service.booking.BookingService;

import java.util.List;

@RestController
@RequestMapping("v1/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponse create(@RequestBody BookingRequest req) {
        return bookingService.createBooking(req);
    }

    @GetMapping("/mine")
    public List<BookingResponse> myBookings() {
        return bookingService.getMyBookings();
    }

    @GetMapping("/mine/{id}")
    public BookingResponse detail(@PathVariable Integer id) {
        return bookingService.getMyBookingDetail(id);
    }

    @DeleteMapping("/mine/{id}")
    public void cancel(@PathVariable Integer id) {
        bookingService.cancelMyBooking(id);
    }

    // ADMIN
    @GetMapping("/admin")
    public List<BookingResponse> all() {
        return bookingService.getAll();
    }



    @PatchMapping("/admin/{id}/approve")
    public BookingResponse approve(
            @PathVariable Integer id,
            @RequestBody ApproveBookingRequest req) {

        return bookingService.approve(id, req.guideId());
    }

    @PatchMapping("/admin/{id}/cancel")
    public void cancelAdmin(@PathVariable Integer id) {
        bookingService.adminCancel(id);
    }
}