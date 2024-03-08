package ca.ulaval.glo2003.domain.restaurant;

import ca.ulaval.glo2003.controllers.models.ReservationConfigurationDTO;
import ca.ulaval.glo2003.domain.hours.Hours;

public class RestaurantFactory {

    public Restaurant buildRestaurant(String ownerId,
                                      String name,
                                      Integer capacity,
                                      Hours hours,
                                      ReservationConfigurationDTO reservations) {

        if (hasReservationConfiguration(reservations)) {
            return buildRestaurantWithReservationConfiguration(
                    ownerId,
                    name,
                    capacity,
                    hours,
                    reservations);
        } else {
            return buildRestaurantWithoutReservationConfiguration(ownerId,
                    name,
                    capacity,
                    hours);
        }
    }

    private boolean hasReservationConfiguration(ReservationConfigurationDTO reservations) {
        return reservations != null;
    }

    private Restaurant buildRestaurantWithReservationConfiguration(
            String ownerId, String name, Integer capacity, Hours hours, ReservationConfigurationDTO reservations) {
        ReservationConfiguration reservationConfiguration = new ReservationConfiguration(reservations.duration());

        return new Restaurant(
            ownerId,
            name,
            capacity,
            hours,
            reservationConfiguration);
    }

    private Restaurant buildRestaurantWithoutReservationConfiguration(
            String ownerId, String name, Integer capacity, Hours hours) {
        return new Restaurant(
            ownerId,
            name,
            capacity,
            hours);
    }
}
