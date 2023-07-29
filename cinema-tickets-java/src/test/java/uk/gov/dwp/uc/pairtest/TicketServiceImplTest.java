package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceImplTest {
    TicketServiceImpl ticketService;
    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl();
    }

    @Test
    void testInvalidAccountId() {
        Long invalidAccountId = -1L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest[] ticketTypeRequests = {adultTicket};

        Exception exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(invalidAccountId, ticketTypeRequests));
        String expectedMessage = "Invalid account ID.";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void testValidAccountId() {
        Long validAccountId = 1234L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest[] ticketTypeRequests = {adultTicket};

        assertDoesNotThrow(() -> ticketService.purchaseTickets(validAccountId, ticketTypeRequests));
    }

    @Test
    public void testExceedMaxTicketsPerPurchase() {
        Long accountId = 1234L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 21);
        TicketTypeRequest[] ticketTypeRequests = {adultTicket};

        Exception exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(accountId, ticketTypeRequests));
        String expectedMessage = "Can't purchase more than 20 tickets at a time";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testInvalidAdultInfantTicketCombination() {
        Long accountId = 1235L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 3);
        TicketTypeRequest[] ticketTypeRequests = {adultTicket, infantTicket};

        Exception exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(accountId, ticketTypeRequests));
        String expectedMessage = "Infants sit on adult lap, so can't have more infants than adults";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testInvalidTicketCombinationNoAdult() {
        Long accountId = 1235L;
        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        TicketTypeRequest[] ticketTypeRequests = {childTicket, infantTicket};

        Exception exception = assertThrows(InvalidPurchaseException.class, () -> ticketService.purchaseTickets(accountId, ticketTypeRequests));
        String expectedMessage = "At least one adult required to purchase tickets";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testValidTicketCombination() {
        Long accountId = 1235L;
        TicketTypeRequest adultTicket = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 3);
        TicketTypeRequest childTicket = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        TicketTypeRequest infantTicket = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        TicketTypeRequest[] ticketTypeRequests = {adultTicket, childTicket, infantTicket};

        assertDoesNotThrow(() -> ticketService.purchaseTickets(accountId, ticketTypeRequests));

    }
}