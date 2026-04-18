package tripora.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookingResponse(

        Integer id,
        Integer tourId,
        String tourTitle,

        Integer userId,

        Integer guideId,
        Integer transportOptionId,

        LocalDate travelDate,
        Integer guestCount,

        BigDecimal totalPrice,
        String status,

        LocalDateTime bookedAt
) {}