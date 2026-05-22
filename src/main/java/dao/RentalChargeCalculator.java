package dao;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class RentalChargeCalculator {

    private RentalChargeCalculator() {
    }

    public static long countOverlapDays(LocalDate rentedAt, LocalDate returnedAt, LocalDate startDate, LocalDate endDate) {
        if (rentedAt == null || startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return 0;
        }

        LocalDate effectiveReturnedAt = returnedAt == null ? endDate : returnedAt;
        LocalDate fromDate = rentedAt.isAfter(startDate) ? rentedAt : startDate;
        LocalDate toDate = effectiveReturnedAt.isBefore(endDate) ? effectiveReturnedAt : endDate;

        if (fromDate.isAfter(toDate)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(fromDate, toDate) + 1;
    }

    public static float calculateRevenue(
            float rentalPrice,
            int quantity,
            LocalDate rentedAt,
            LocalDate returnedAt,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (quantity <= 0 || rentalPrice <= 0) {
            return 0;
        }
        return rentalPrice * quantity * countOverlapDays(rentedAt, returnedAt, startDate, endDate);
    }
}
