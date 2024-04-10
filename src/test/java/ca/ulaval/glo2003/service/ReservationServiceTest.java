package ca.ulaval.glo2003.service;

import ca.ulaval.glo2003.controllers.api.fixture.ReservationRequestFixture;
import ca.ulaval.glo2003.controllers.assemblers.ReservationGeneralResponseAssembler;
import ca.ulaval.glo2003.controllers.assemblers.ReservationResponseAssembler;
import ca.ulaval.glo2003.controllers.requests.ReservationRequest;
import ca.ulaval.glo2003.controllers.responses.ReservationGeneralResponse;
import ca.ulaval.glo2003.controllers.responses.ReservationResponse;
import ca.ulaval.glo2003.domain.customer.Customer;
import ca.ulaval.glo2003.domain.exceptions.InvalidParameterException;
import ca.ulaval.glo2003.domain.exceptions.MissingParameterException;
import ca.ulaval.glo2003.domain.repositories.ReservationRepository;
import ca.ulaval.glo2003.domain.repositories.RestaurantRepository;
import ca.ulaval.glo2003.domain.reservation.Reservation;
import ca.ulaval.glo2003.domain.reservation.ReservationFactory;
import ca.ulaval.glo2003.domain.restaurant.Restaurant;
import ca.ulaval.glo2003.domain.utils.Hours;
import ca.ulaval.glo2003.service.assembler.CustomerAssembler;
import ca.ulaval.glo2003.service.dtos.CustomerDTO;
import ca.ulaval.glo2003.service.dtos.TimeDTO;
import ca.ulaval.glo2003.service.validators.HeaderValidator;
import ca.ulaval.glo2003.service.validators.ReservationValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {
    private static final String OWNER_ID = "1";
    private static final String INVALID_OWNER_ID = "2";
    private static final String INVALID_ID = "2";
    private static final String INVALID_DATE = "2024-03-02";
    private static final String VALID_CUSTOMER_NAME = "John Doe";
    private static final String INVALID_CUSTOMER_NAME = "Jhon Doe";
    private static final String RESTO_NAME = "1";
    private static final Integer RESTO_CAPACITY = 10;
    private static final Hours RESTO_HOURS = new Hours("10:00:00", "21:00:00");
    private static final String DATE = "2024-03-31";
    private static final String INVALID_DATEE = "03-31-2024";
    private static final String START_TIME = "20:46:00";
    private static final int GROUP_SIZE = 3;
    private static final String CUSTOMER_EMAIL = "z@y.z";
    private static final String CUSTOMER_PHONE_NUMBER = "123456789";
    @Mock
    private ReservationRepository reservationRepository = Mockito.mock(ReservationRepository.class);
    @Mock
    private RestaurantRepository restaurantRepository = Mockito.mock(RestaurantRepository.class);
    @Mock
    private ReservationFactory reservationFactory;
    @Mock
    private ReservationValidator reservationValidator = Mockito.mock(ReservationValidator.class);
    @Mock
    private HeaderValidator headerValidator = Mockito.mock(HeaderValidator.class);
    private CustomerAssembler customerAssembler;
    private CustomerDTO customerDto;
    private ReservationResponseAssembler reservationResponseAssembler;
    private ReservationGeneralResponseAssembler reservationGeneralResponseAssembler;
    private ReservationService reservationService;
    private Restaurant restaurant;
    private Reservation reservation;
    private Customer customer;
    private ReservationGeneralResponse reservationGeneralResponse;
    private TimeDTO timeDTO;
    private final List<Reservation> reservationList = new ArrayList<>();
    List<ReservationGeneralResponse> searchedReservations = new ArrayList<>();
    @BeforeEach
    public void setUp() throws MissingParameterException, InvalidParameterException {
        customer = mock(Customer.class);
        restaurant = new Restaurant(OWNER_ID, RESTO_NAME, RESTO_CAPACITY, RESTO_HOURS);
        reservation = new Reservation(restaurant.getId(), DATE, START_TIME, GROUP_SIZE, customer);
        customerAssembler = new CustomerAssembler();
        customerDto = new CustomerDTO("John Doe", CUSTOMER_EMAIL, CUSTOMER_PHONE_NUMBER);

        reservationResponseAssembler = new ReservationResponseAssembler();
        reservationGeneralResponseAssembler = new ReservationGeneralResponseAssembler();
        reservationService = new ReservationService(
                restaurantRepository,
                reservationRepository,
                reservationFactory,
                customerAssembler,
                reservationResponseAssembler,
                reservationValidator,
                reservationGeneralResponseAssembler);

        reservationGeneralResponse = new ReservationGeneralResponse(reservation.getId(),
                reservation.getDate(),
                new TimeDTO(reservation.getStartTime(), restaurant.getRestaurantConfiguration().getDuration()),
                reservation.getGroupSize(),
                customerDto);
        reservationList.add(reservation);
        when(reservationRepository.getAllRestaurantReservations(restaurant.getId()))
                .thenReturn(reservationList);

        when(restaurantRepository.findRestaurantById(restaurant.getId()))
                .thenReturn(restaurant);
        when(customer.getName())
                .thenReturn(VALID_CUSTOMER_NAME);

        doNothing().when(headerValidator).verifyMissingHeader(OWNER_ID);


        doNothing().when(reservationValidator).validateSearchReservationRequest(restaurant.getId(), DATE);

    }
    public void givenValidRestaurantIdAndReservationRequest_whenCreatingReservation_shouldReturnRightReservationId()  {
        ReservationRequest reservationRequestFixture = new ReservationRequestFixture().create();
        when(restaurantRepository.findRestaurantById(restaurant.getId())).thenReturn(restaurant);
        when(reservationFactory.createReservation(restaurant.getId(),
                reservationRequestFixture.date(),
                reservationRequestFixture.startTime(),
                reservationRequestFixture.groupSize(),
                customer))
                .thenReturn(reservation);

        //String createdId = reservationService.createReservation(restaurant.getId(), reservationRequestFixture);

        //Assertions.assertThat(createdId).isEqualTo(TOURNAMENT_ID);
    }






    //    //TODO : IMPLEMENT THE SERVICE TEST
//    private static final String CUSTOMER_NAME = "John Deer";
//    private static final String CUSTOMER_EMAIL = "john.deer@gmail.com";
//    private static final String CUSTOMER_PHONE_NUMBER = "1234567890";
//    private static final String OWNER_ID = "1";
//    private static final String RESTO_NAME = "1";
//    private static final Integer RESTO_CAPACITY = 10;
//    private static final Hours RESTO_HOURS = new Hours("10:00:00", "21:00:00");
//    Customer customer;
//    ReservationService reservationService;
//    @Mock
//    ReservationFactory reservationFactory;
//    @Mock
//    RestaurantAndReservationRepository restaurantAndReservationRepository;
//    @Mock
//    CustomerAssembler customerAssembler;
//    @Mock
//    ReservationResponseAssembler reservationResponseAssembler;
//    @Mock
//    ReservationValidator reservationValidator;
//    @Mock
//    Restaurant restaurant;
//
//    CustomerDTO customerDto;
//
//    @BeforeEach
//    void setup() {
//        customerDto = new CustomerDTO(CUSTOMER_NAME, CUSTOMER_EMAIL, CUSTOMER_PHONE_NUMBER);
//        customer = new Customer(CUSTOMER_NAME, CUSTOMER_EMAIL, CUSTOMER_PHONE_NUMBER);
//        restaurant = new Restaurant(OWNER_ID, RESTO_NAME, RESTO_CAPACITY, RESTO_HOURS);
//
//        reservationService = new ReservationService(
//                restaurantAndReservationRepository,
//            restaurantRepository, reservationRepository, reservationFactory,
//                customerAssembler,
//                reservationResponseAssembler,
//                reservationValidator
//        );
//    }
//
//    @Test
//    void canCreateReservation() {
//        // TODO: since we changed the flow, the service now accepts a request; We have to change the test.
//        String date = "2024-03-31";
//        String startTime = "20:46:00";
//        Integer groupSize = 5;
//        Reservation reservation = new Reservation(restaurant.getId(), date, startTime, groupSize, customer);
//        when(restaurantAndReservationRepository.findRestaurantByRestaurantId(restaurant.getId())).thenReturn(restaurant);
//        when(reservationFactory.createReservation(restaurant.getId(), date, startTime, groupSize, customer)).thenReturn(reservation);
//
//        //reservationService.createReservation(restaurant.getId(), date, startTime, groupSize);
//    }
}
