package tripora.api.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripora.api.Util.Validator.BookingValidator;
import tripora.api.Util.security.SecurityUtil;
import tripora.api.enums.BookingStatus;
import tripora.api.domain.*;

import tripora.api.dto.BookingRequest;
import tripora.api.dto.BookingResponse;
import tripora.api.exception.*;

import tripora.api.mapper.BookingMapper;
import tripora.api.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final TourRepository tourRepository;
    private final GuideRepository guideRepository;
    private final TransportOptionRepository transportOptionRepository;
    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;

    private final BookingMapper bookingMapper;

    // BOOKING
    @Override
    public BookingResponse createBooking(BookingRequest req) {

        Integer userId = SecurityUtil.getCurrentUserId();


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tour tour = tourRepository.findById(req.tourId())
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found"));

        BookingValidator.validateBooking(tour, req);


        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTour(tour);
        booking.setTravelDate(req.travelDate());
        booking.setGuestCount(req.guestCount());

        TransportOption transport = null;

        if (req.transportOptionId() != null) {
            transport = transportOptionRepository.findById(req.transportOptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Transport not found"));

            if (req.guestCount() < transport.getMinGuests()
                    || req.guestCount() > transport.getMaxGuests()) {
                throw new ConflictException(
                        "Guest count not allowed for selected transport option"
                );
            }

            booking.setTransportOption(transport);
        }

        booking.setTotalPrice(
                transport != null ? transport.getGroupPrice() : BigDecimal.ZERO
        );

        booking.setStatus(BookingStatus.PENDING);
        booking.setBookedAt(LocalDateTime.now());

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    // MY BOOKINGS

    @Override
    public List<BookingResponse> getMyBookings() {

        Integer userId = SecurityUtil.getCurrentUserId();

        return bookingRepository.findByUserId(userId)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public BookingResponse getMyBookingDetail(Integer bookingId) {

        Integer userId = SecurityUtil.getCurrentUserId();
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(userId))
            throw new ConflictException("Not your booking");

        return bookingMapper.toResponse(booking);
    }

    @Override
    public void cancelMyBooking(Integer bookingId) {

        Integer userId = SecurityUtil.getCurrentUserId();
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(userId))
            throw new ConflictException("Not your booking");

        if (!"PENDING".equals(booking.getStatus()))
            throw new ConflictException("Only pending bookings can be cancelled");

        booking.setStatus(BookingStatus.valueOf("CANCELLED"));
        bookingRepository.save(booking);
    }

    // ADMIN
    @Override
    public List<BookingResponse> getAll() {
        return bookingRepository.findAll()
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }




    @Override
    public BookingResponse approve(Integer bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ConflictException("Only pending bookings can be approved");
        }

//        Guide guide = guideRepository.findById(guideId)
//                .orElseThrow(() -> new ResourceNotFoundException("Guide not found"));
//
//        if (!guide.getIsAvailable()) {
//            throw new ConflictException("Guide is not available");
//        }
//
//        boolean alreadyBooked = bookingRepository
//                .existsByGuideIdAndTravelDate(guideId, booking.getTravelDate());

//        if (alreadyBooked) {
//            throw new ConflictException("Guide already assigned on this date");
//        }

//        booking.setGuide(guide);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setApprovedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        // CREATE INVOICE
        if (invoiceRepository.findByBookingId(bookingId).isEmpty()) {

            Invoice invoice = new Invoice();
            invoice.setBooking(savedBooking);

            invoice.setInvoiceNumber("INV-" + System.currentTimeMillis()); // ✅ FIX

            BigDecimal subtotal = savedBooking.getTotalPrice();
            BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.10));
            BigDecimal total = subtotal.add(tax);

            invoice.setSubtotal(subtotal);
            invoice.setTaxAmount(tax);
            invoice.setGrandTotal(total);
            invoice.setStatus("UNPAID");
            invoice.setIssuedAt(LocalDateTime.now());

            invoiceRepository.save(invoice);
        }

        return bookingMapper.toResponse(savedBooking);
    }

    @Override
    public void adminCancel(Integer bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setStatus(BookingStatus.valueOf("CANCELLED"));
        bookingRepository.save(booking);
    }
}