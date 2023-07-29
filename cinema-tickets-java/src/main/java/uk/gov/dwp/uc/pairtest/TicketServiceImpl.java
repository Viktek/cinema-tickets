package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.constants.TicketConfig;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.model.ResolvedTicketRequestDetails;

import java.util.Arrays;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
    private static final SeatReservationService seatReservationService = new SeatReservationServiceImpl();
    private static final TicketPaymentService ticketPaymentService = new TicketPaymentServiceImpl();

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
            validateAccountId(accountId);
            if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
                throw new InvalidPurchaseException("No ticket types provided for purchase.");
            }
            ResolvedTicketRequestDetails ticketRequestDetails = resolveTicketRequest(ticketTypeRequests);
            validatePurchaseRequest(ticketRequestDetails);

            ticketPaymentService.makePayment(accountId, ticketRequestDetails.getTotalPrice());
            seatReservationService.reserveSeat(accountId, ticketRequestDetails.getTotalNoOfSeatsToAllocate());
    }

    private ResolvedTicketRequestDetails resolveTicketRequest(TicketTypeRequest... ticketTypeRequests){
        int adultTickets = getTicketCount(TicketTypeRequest.Type.ADULT, ticketTypeRequests);
        int childTickets = getTicketCount(TicketTypeRequest.Type.CHILD, ticketTypeRequests);
        int infantTickets = getTicketCount(TicketTypeRequest.Type.INFANT, ticketTypeRequests);

        return new ResolvedTicketRequestDetails(infantTickets, childTickets, adultTickets);
    }

    private int getTicketCount(TicketTypeRequest.Type type, TicketTypeRequest... ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests)
                .filter(r -> r.getTicketType().equals(type))
                .mapToInt(TicketTypeRequest::getNoOfTickets).sum();
    }


    private void validatePurchaseRequest(ResolvedTicketRequestDetails ticketRequestDetails){

        if(ticketRequestDetails.getTotalNoOfTickets() > TicketConfig.MAX_NO_OF_TICKETS)
            throw new InvalidPurchaseException(String.format("Can't purchase more than %d tickets at a time", TicketConfig.MAX_NO_OF_TICKETS));

        if(ticketRequestDetails.totalNoOfAdultTickets() < 1)
            throw new InvalidPurchaseException("At least one adult required to purchase tickets");

        if(ticketRequestDetails.totalNoOfInfantTickets() > ticketRequestDetails.totalNoOfAdultTickets())
            throw new InvalidPurchaseException("Infants sit on adult lap, so can't have more infants than adults");
    }
    private void validateAccountId(Long accountId) {
        if (accountId < 1) {
            throw new InvalidPurchaseException("Invalid account ID.");
        }
    }
}