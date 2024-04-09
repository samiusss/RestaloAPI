package ca.ulaval.glo2003.service;

import ca.ulaval.glo2003.controllers.assemblers.FuzzySearchResponseAssembler;
import ca.ulaval.glo2003.controllers.assemblers.RestaurantResponseAssembler;
import ca.ulaval.glo2003.controllers.requests.RestaurantRequest;
import ca.ulaval.glo2003.controllers.responses.RestaurantResponse;
import ca.ulaval.glo2003.domain.exceptions.InvalidParameterException;
import ca.ulaval.glo2003.domain.exceptions.MissingParameterException;
import ca.ulaval.glo2003.domain.repositories.ReservationRepository;
import ca.ulaval.glo2003.domain.repositories.RestaurantRepository;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.restaurant.RestaurantFactory;
import ca.ulaval.glo2003.domain.utils.Hours;
import ca.ulaval.glo2003.service.dtos.HoursDTO;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class RestaurantServiceTest {
    private static RestaurantService restaurantService;
    private static RestaurantRepository restaurantRepository = Mockito.mock(RestaurantRepository.class);
    private static ReservationRepository reservationRepository = Mockito.mock(ReservationRepository.class);
    private static RestaurantFactory restaurantFactory = Mockito.mock(RestaurantFactory.class);
    private static RestaurantResponseAssembler restaurantResponseAssembler = Mockito.mock(RestaurantResponseAssembler.class);
    private static FuzzySearchResponseAssembler fuzzySearchResponseAssembler = Mockito.mock(FuzzySearchResponseAssembler.class);
    private static final String RESTAURANT_ID = "1";
    private static final String OWNER_ID = "1";
    private static final String RESTAURANT_NAME = "Restaurant Name";
    private static final Integer CAPACITY = 10;

    @Mock
    private Restaurant restaurantMock;

    @BeforeAll
    public static void setUpService() {
        restaurantService = new RestaurantService(
            restaurantRepository,
            reservationRepository,
            restaurantFactory,
            restaurantResponseAssembler,
            fuzzySearchResponseAssembler
        );
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockRestaurantBehavior();
    }

    private void mockRestaurantBehavior() {
        when(restaurantMock.getId()).thenReturn(RESTAURANT_ID);
        when(restaurantMock.getOwnerId()).thenReturn(OWNER_ID);
        when(restaurantMock.getName()).thenReturn(RESTAURANT_NAME);
        when(restaurantMock.getHours()).thenReturn(Mockito.mock(Hours.class));

    }

    @Test
    void givenValidRequest_whenCreatingRestaurant_shouldSaveRestaurantInRepository()
        throws InvalidParameterException, MissingParameterException {
        RestaurantRequest restaurantRequest = new RestaurantRequest(
            RESTAURANT_NAME, CAPACITY,
            new HoursDTO("10:00:00",
                "11:00:00"),
            null);
        when(restaurantFactory.createRestaurant(
            OWNER_ID,
            RESTAURANT_NAME,
            CAPACITY,
            new HoursDTO("10:00:00", "11:00:00"), null))
            .thenReturn(restaurantMock);

        String restaurantId = restaurantService.createRestaurant(OWNER_ID, restaurantRequest);

        verify(restaurantFactory).createRestaurant(OWNER_ID, RESTAURANT_NAME, CAPACITY, new HoursDTO("10:00:00", "11:00:00"), null);

        ArgumentCaptor<Restaurant> restaurantCaptor = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantRepository).saveRestaurant(restaurantCaptor.capture());
        Restaurant savedRestaurant = restaurantCaptor.getValue();
        assertEquals(restaurantId, savedRestaurant.getId());
    }

    @Test
    void givenNonExistingRestaurant_whenGetRestaurant_shouldThrowNotFoundException() {
        assertThrows(
            NotFoundException.class,
            () -> restaurantService.getRestaurant(OWNER_ID, "Non existing Restaurant"));
    }

    @Test
    void givenExistingRestaurant_whenGetRestaurant_thenShouldReturnRestaurant() throws MissingParameterException {
        when(restaurantRepository.findRestaurantById(RESTAURANT_ID)).thenReturn(restaurantMock);
        RestaurantResponse expectedResponse = new RestaurantResponse(
            RESTAURANT_ID,
            OWNER_ID,
            RESTAURANT_NAME,
            CAPACITY,
            new HoursDTO("10:00:00", "11:00:00"),
            null);
        when(restaurantResponseAssembler.toDTO(restaurantMock)).thenReturn(expectedResponse);

        RestaurantResponse restaurantResponse = restaurantService.getRestaurant(OWNER_ID, RESTAURANT_ID);

        assertNotNull(restaurantResponse);
        assertEquals(expectedResponse, restaurantResponse);
        verify(restaurantRepository).findRestaurantById(RESTAURANT_ID);
        verify(restaurantResponseAssembler).toDTO(restaurantMock);
    }


    @Test
    void givenValidOwnerId_whenGetRestaurantsForOwnerId_thenShouldReturnRestaurantResponses() throws MissingParameterException {
        List<Restaurant> ownerRestaurants = new ArrayList<>();
        HoursDTO hoursDTO = new HoursDTO("10:00:00", "11:00:00");
        Hours hours = new Hours("10:00:00", "11:00:00");
        ownerRestaurants.add(new Restaurant("1", OWNER_ID, "Restaurant 1", 10, hours, null));
        ownerRestaurants.add(new Restaurant("2", OWNER_ID, "Restaurant 2", 20, hours, null));
        when(restaurantRepository.findRestaurantsByOwnerId(OWNER_ID)).thenReturn(ownerRestaurants);
        RestaurantResponse expectedResponse1 = new RestaurantResponse("1", OWNER_ID, "Restaurant 1", 10, hoursDTO, null);
        RestaurantResponse expectedResponse2 = new RestaurantResponse("2", OWNER_ID, "Restaurant 2", 20, hoursDTO, null);
        when(restaurantResponseAssembler.toDTO(ownerRestaurants.get(0))).thenReturn(expectedResponse1);
        when(restaurantResponseAssembler.toDTO(ownerRestaurants.get(1))).thenReturn(expectedResponse2);

        List<RestaurantResponse> restaurantResponses = restaurantService.getRestaurantsForOwnerId(OWNER_ID);

        assertNotNull(restaurantResponses);
        assertEquals(2, restaurantResponses.size());
        assertEquals(expectedResponse1, restaurantResponses.get(0));
        assertEquals(expectedResponse2, restaurantResponses.get(1));
        verify(restaurantRepository).findRestaurantsByOwnerId(OWNER_ID);
        verify(restaurantResponseAssembler, times(2)).toDTO(any(Restaurant.class));
    }

    @Test
    void givenNonExistingRestaurantId_whenDeleteRestaurant_thenShouldThrowNotFoundException() {
        when(restaurantRepository.findRestaurantById(RESTAURANT_ID)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> restaurantService.deleteRestaurant(OWNER_ID, RESTAURANT_ID));
    }

    @Test
    void givenValidOwnerIdAndRestaurantId_whenDeleteRestaurant_thenShouldDeleteRestaurantAndReservations()
        throws MissingParameterException, NotFoundException {
        when(restaurantRepository.findRestaurantById(RESTAURANT_ID)).thenReturn(restaurantMock);

        restaurantService.deleteRestaurant(OWNER_ID, RESTAURANT_ID);

        verify(restaurantRepository).deleteRestaurant(OWNER_ID, RESTAURANT_ID);
        verify(reservationRepository).deleteReservationsWithRestaurantId(RESTAURANT_ID);
    }
}




