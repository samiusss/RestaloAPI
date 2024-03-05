package ca.ulaval.glo2003.models;

import ca.ulaval.glo2003.domain.utils.Hours;
import ca.ulaval.glo2003.domain.exceptions.InvalidParameterException;
import ca.ulaval.glo2003.domain.exceptions.MissingParameterException;
import ca.ulaval.glo2003.domain.restaurant.ReservationConfiguration;
import jakarta.ws.rs.NotFoundException;

import java.time.Duration;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestaurantRequest {
    private String name;
    private Integer capacity;
    private Hours hours;
    private ReservationConfiguration reservations;
    // Do not change this variable's name, the createRestaurant Body uses the name for assignation
    private static final String TIME_FORMAT_REGEX = "^([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
    private static final Pattern TIME_PATTERN = Pattern.compile(TIME_FORMAT_REGEX);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Hours getHours() {
        return hours;
    }

    public void setHours(Hours hours) {
        this.hours = hours;
    }

    public ReservationConfiguration getRestaurantConfiguration() {
        return reservations;
    }

    public void setReservations(ReservationConfiguration reservations) {
        this.reservations = reservations;
    }

    public static void verifyRestaurantOwnership(String expectedOwnerId, String actualOwnerId) throws NotFoundException {
        if (!expectedOwnerId.equals(actualOwnerId)) {
            throw new NotFoundException();
        }
    }

    public void verifyParameters() throws InvalidParameterException, MissingParameterException {
        verifyMissingParameters();
        verifyValidParameters();
    }

    private void verifyMissingParameters() throws MissingParameterException {
        verifyMissingCapacity();
        verifyMissingName();
        verifyMissingHours();
    }

    private void verifyValidParameters() throws InvalidParameterException {
        verifyValidName();
        verifyValidCapacity();
        verifyValidHours();
    }

    private void verifyMissingCapacity() throws MissingParameterException {
        if (capacity == null) {
            throw new MissingParameterException("Missing parameter 'capacity'");
        }
    }

    private void verifyMissingName() throws MissingParameterException {
        if (name == null) {
            throw new MissingParameterException("Missing parameter 'name'");
        }
    }

    private void verifyMissingHours() throws MissingParameterException {
        if (hours == null || hours.getOpen() == null || hours.getClose() == null) {
            throw new MissingParameterException("Missing parameter 'hours'");
        }
    }

    private void verifyValidName() throws InvalidParameterException {
        if (name.isEmpty()) {
            throw new InvalidParameterException("Invalid parameter 'name', cant be blank");
        }
    }

    private void verifyValidCapacity() throws InvalidParameterException {
        if (capacity < 1) {
            throw new InvalidParameterException("Invalid parameter 'capacity', minimum capacity of 1 person");
        }
    }

    private void verifyValidHours() throws InvalidParameterException {
        verifyValidTimeFormat(hours.getOpen());
        verifyValidTimeFormat(hours.getClose());
        verifyOpeningAndClosingCriteria();
    }

    private void verifyValidTimeFormat(String time) throws InvalidParameterException {
        Matcher matcher = TIME_PATTERN.matcher(time);
        if (!matcher.matches()) {
            throw new InvalidParameterException("Invalid time format: " + time + ". Use the 'HH:MM:SS' format");
        }
    }

    private void verifyOpeningAndClosingCriteria() throws InvalidParameterException {
        Duration duration = calculateDuration();
        verifyNonNegativeDuration(duration);
        verifyMinimumOpeningDuration(duration);
    }

    private Duration calculateDuration() {
        LocalTime openingTime = LocalTime.parse(hours.getOpen());
        LocalTime closingTime = LocalTime.parse(hours.getClose());
        return Duration.between(openingTime, closingTime);
    }

    private void verifyNonNegativeDuration(Duration duration) throws InvalidParameterException {
        if (duration.isNegative()) {
            throw new InvalidParameterException("Invalid parameter 'hours', closing time is before opening time");
        }
    }

    private void verifyMinimumOpeningDuration(Duration duration) throws InvalidParameterException {
        long minutes = duration.toMinutes();
        if (minutes < 60) {
            throw new InvalidParameterException("Invalid parameter 'hours', must be open for at least 1 hour");
        }
    }
}