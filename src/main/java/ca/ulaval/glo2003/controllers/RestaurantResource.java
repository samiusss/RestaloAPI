package ca.ulaval.glo2003.controllers;

import ca.ulaval.glo2003.Main;
import ca.ulaval.glo2003.domain.exceptions.InvalidParameterException;
import ca.ulaval.glo2003.domain.exceptions.MissingParameterException;
import ca.ulaval.glo2003.domain.utils.ResourcesHandler;
import ca.ulaval.glo2003.models.ReservationRequest;
import ca.ulaval.glo2003.models.ReservationResponse;
import ca.ulaval.glo2003.models.RestaurantRequest;
import ca.ulaval.glo2003.models.RestaurantResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.List;


@Path("/")
public class RestaurantResource {
    public ResourcesHandler resourcesHandler;
    public RestaurantResource(ResourcesHandler resourcesHandler) {
        this.resourcesHandler = resourcesHandler;
    }

    @GET
    @Path("restaurants")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RestaurantResponse> getRestaurants(@HeaderParam("Owner") String ownerId) throws MissingParameterException {
        verifyMissingHeader(ownerId);
        return resourcesHandler.getAllRestaurantsForOwner(ownerId);
    }

    @POST
    @Path("restaurants")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRestaurant(@HeaderParam("Owner") String ownerId, RestaurantRequest restaurantRequest)
        throws InvalidParameterException, MissingParameterException, NotFoundException {
        verifyMissingHeader(ownerId);
        restaurantRequest.verifyParameters();

        String createdRestaurantId = resourcesHandler.addRestaurant(
                ownerId,
                restaurantRequest.getName(),
                restaurantRequest.getCapacity(),
                restaurantRequest.getHours(),
                restaurantRequest.getRestaurantConfiguration());

        URI newProductURI = UriBuilder.fromResource(RestaurantResource.class).path("restaurants").path(createdRestaurantId).build();
        return Response.created(newProductURI).build();
    }

    @GET
    @Path("restaurants/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestaurant(@HeaderParam("Owner") String ownerID, @PathParam("id") String restaurantId)
        throws MissingParameterException, NotFoundException {
        verifyMissingHeader(ownerID);
        RestaurantResponse response = resourcesHandler.getRestaurant(restaurantId, ownerID);
        return Response.ok(response).build();
    }

    @POST
    @Path("restaurants/{id}/reservations")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReservation(@PathParam("id") String restaurantId, ReservationRequest reservationRequest)
        throws NotFoundException, InvalidParameterException, MissingParameterException {
        resourcesHandler.verifyValidRestaurantIdPath(restaurantId);
        reservationRequest.verifyParameters();
        reservationRequest.adjustReservationStartTime();
        resourcesHandler.verifyValidReservationEndTime(
                restaurantId,
                reservationRequest.getStartTime());

        String createdReservationId = resourcesHandler.addReservation(
                restaurantId,
                reservationRequest.getDate(),
                reservationRequest.getStartTime(),
                reservationRequest.getGroupSize(),
                reservationRequest.getCustomer());
        URI newReservationURI = UriBuilder.fromPath(Main.BASE_URI)
            .path("reservations")
            .path(createdReservationId)
            .build();
        return Response.created(newReservationURI).build();
    }

    @GET
    @Path("reservations/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReservation(@PathParam("number") String reservationId)
        throws NotFoundException {
        ReservationResponse foundReservationResponse = resourcesHandler.getReservation(reservationId);
        return Response.ok(foundReservationResponse).build();
    }

    private void verifyMissingHeader(String ownerId) throws MissingParameterException {
        if (ownerId == null) {
            throw new MissingParameterException("Missing 'Owner' header");
        }
    }
}
