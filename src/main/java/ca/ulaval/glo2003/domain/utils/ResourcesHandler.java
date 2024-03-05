package ca.ulaval.glo2003.domain.utils;

import ca.ulaval.glo2003.domain.customer.Customer;
import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.models.RestaurantResponse;
import ca.ulaval.glo2003.domain.factories.ReservationFactory;

import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResourcesHandler {
    private final Map<String, Restaurant> restaurants;
    private ReservationFactory reservationsFactory;

    public ResourcesHandler(ReservationFactory reservationsFactory) {
        this.reservationsFactory = reservationsFactory;
        this.restaurants = new HashMap<>();
    }

    public void addRestaurant(Restaurant restaurant) throws NotFoundException {
        restaurants.put(restaurant.getId(), restaurant);
    }

    public Restaurant getRestaurant(String restaurantId) throws NotFoundException {
        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant == null) throw new NotFoundException();
        return restaurant;
    }

    public List<RestaurantResponse> getAllRestaurantsForOwner(String ownerId) {
        List<RestaurantResponse> ownerRestaurants = new ArrayList<>();
        for (Restaurant restaurant : restaurants.values()) {
            if (restaurant.getOwnerId().equals(ownerId)) {
                ownerRestaurants.add(new RestaurantResponse(restaurant));
            }
        }
        return ownerRestaurants;
    }

    public String addReservation(String restaurantId, String date, String startTime, Integer groupSize, Customer customer)
            throws NotFoundException {
        Restaurant restaurant = restaurants.get(restaurantId);
        Reservation reservation = reservationsFactory.buildReservation(restaurantId, date, startTime, groupSize, customer);
        restaurant.addReservation(reservation);
        return reservation.getId();
    }

    public Reservation getReservation(String reservationId) throws NotFoundException {
        for (Restaurant restaurant : restaurants.values()) {
            var reservationIds = restaurant.getReservationsById();
            for (String number : reservationIds.keySet()) {
                if (Objects.equals(number, reservationId))
                    return reservationIds.get(reservationId);
            }
        }
        throw new NotFoundException();
    }
}
