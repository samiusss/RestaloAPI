package ca.ulaval.glo2003.domain.utils;

import ca.ulaval.glo2003.domain.customer.Customer;
import ca.ulaval.glo2003.domain.exceptions.InvalidParameterException;
import ca.ulaval.glo2003.domain.factories.RestaurantFactory;
import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.restaurant.ReservationConfiguration;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.models.ReservationResponse;
import ca.ulaval.glo2003.models.RestaurantReservationResponse;
import ca.ulaval.glo2003.models.RestaurantResponse;
import ca.ulaval.glo2003.domain.factories.ReservationFactory;

import jakarta.ws.rs.NotFoundException;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResourcesHandler {
    private final Map<String, Restaurant> restaurants;
    private ReservationFactory reservationsFactory;
    private RestaurantFactory restaurantFactory;

    public ResourcesHandler(ReservationFactory reservationsFactory, RestaurantFactory restaurantFactory) {
        this.reservationsFactory = reservationsFactory;
        this.restaurantFactory = restaurantFactory;
        this.restaurants = new HashMap<>();
    }

    public String addRestaurant(String ownerId, String name, Integer capacity, Hours hours, ReservationConfiguration reservations)
            throws NotFoundException {
        Restaurant restaurant = restaurantFactory.buildRestaurant(ownerId, name, capacity, hours, reservations);
        restaurants.put(restaurant.getId(), restaurant);
        return restaurant.getId();
    }

    public RestaurantResponse getRestaurant(String restaurantId, String ownerID) throws NotFoundException {
        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant == null) throw new NotFoundException();
        verifyRestaurantOwnership(restaurant.getOwnerId(), ownerID);
        return new RestaurantResponse(restaurant);
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

    public ReservationResponse getReservation(String reservationId) throws NotFoundException {
        for (Restaurant restaurant : restaurants.values()) {
            var reservationIds = restaurant.getReservationsById();
            for (String number : reservationIds.keySet()) {
                if (Objects.equals(number, reservationId))
                    return new ReservationResponse(reservationIds.get(reservationId), new RestaurantReservationResponse(restaurant));
            }
        }
        throw new NotFoundException();
    }

    public static void verifyRestaurantOwnership(String expectedOwnerId, String actualOwnerId) throws NotFoundException {
        if (!expectedOwnerId.equals(actualOwnerId)) {
            throw new NotFoundException();
        }
    }

    public void verifyValidRestaurantIdPath(String restaurantId) {
        if (restaurants.get(restaurantId) == null) {
            throw new NotFoundException("Restaurant not found with ID: " + restaurantId);
        }
    }

    public void verifyValidReservationEndTime(String restaurantId, String startTime)
            throws InvalidParameterException {
        Restaurant restaurant = restaurants.get(restaurantId);
        ReservationConfiguration reservationConfiguration = restaurant.getRestaurantConfiguration();
        LocalTime reservationEndTime = calculateReservationEndTime(startTime, reservationConfiguration);
        LocalTime closingTime = LocalTime.parse(restaurant.getHours().getClose());
        LocalTime openingTime = LocalTime.parse(restaurant.getHours().getOpen());
        verifyReservationWithinOperatingHours(reservationEndTime, closingTime, openingTime);
    }

    private LocalTime calculateReservationEndTime(
            String startTime, ReservationConfiguration reservationConfiguration) {
        LocalTime reservationStartTime = LocalTime.parse(startTime);
        Duration reservationDuration = Duration.ofMinutes(reservationConfiguration.getDuration());
        return reservationStartTime.plus(reservationDuration);
    }

    private void verifyReservationWithinOperatingHours(
            LocalTime reservationEndTime, LocalTime closingTime, LocalTime openingTime) throws InvalidParameterException {
        if (reservationEndTime.isAfter(closingTime) || reservationEndTime.isBefore(openingTime)) {
            throw new InvalidParameterException(
                    "Invalid reservation start time, the reservation exceeds the restaurant's closing time");
        }
    }
}
