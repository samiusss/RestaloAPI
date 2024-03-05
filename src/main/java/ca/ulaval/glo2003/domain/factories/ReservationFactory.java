package ca.ulaval.glo2003.domain.factories;

import ca.ulaval.glo2003.domain.customer.Customer;
import ca.ulaval.glo2003.domain.reservation.Reservation;

public class ReservationFactory {
    public Reservation buildReservation(String restaurantId, String date, String startTime, int groupSize, Customer customer) {
        return new Reservation(restaurantId, date, startTime, groupSize, customer);
    }
}
