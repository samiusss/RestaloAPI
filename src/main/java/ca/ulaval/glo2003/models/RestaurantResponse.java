package ca.ulaval.glo2003.models;

import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.utils.Hours;
import ca.ulaval.glo2003.domain.restaurant.ReservationConfiguration;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;

import java.util.Map;

public class RestaurantResponse {
    public String id;
    public String name;
    public Integer capacity;
    public Hours hours;
    public ReservationConfiguration reservations;

    public RestaurantResponse(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.capacity = restaurant.getCapacity();
        this.hours = restaurant.getHours();
        this.reservations = restaurant.getRestaurantConfiguration();
    }
}
