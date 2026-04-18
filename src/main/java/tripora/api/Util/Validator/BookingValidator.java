package tripora.api.Util.Validator;

import tripora.api.domain.Guide;
import tripora.api.domain.Tour;
import tripora.api.domain.TransportOption;
import tripora.api.dto.BookingRequest;
import tripora.api.exception.BadRequestException;
import tripora.api.exception.ConflictException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookingValidator {

    public static void validateBooking(Tour tour, BookingRequest req) {

        if (!tour.getIsActive())
            throw new ConflictException("Tour is not available");

        if (req.travelDate().isAfter(LocalDate.now().plusYears(1)))
            throw new BadRequestException("Travel date too far");

        if (req.travelDate().isBefore(LocalDate.now()))
            throw new BadRequestException("Travel date must be future");

        if (req.guestCount() == null || req.guestCount() < 1)
            throw new BadRequestException("Invalid guest count");
    }




}
