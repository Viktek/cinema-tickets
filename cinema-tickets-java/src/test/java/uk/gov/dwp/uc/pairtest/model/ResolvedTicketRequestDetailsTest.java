package uk.gov.dwp.uc.pairtest.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResolvedTicketRequestDetailsTest {
    private ResolvedTicketRequestDetails resolvedTicketRequestDetails;

    @Test
    void testGetTotalPrice() {
        resolvedTicketRequestDetails = new ResolvedTicketRequestDetails(3,4,5);
        int expectedPrice = 140;
        int actualPrice = resolvedTicketRequestDetails.getTotalPrice();

        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    void testTotalNoOfSeatsToAllocate() {
        resolvedTicketRequestDetails = new ResolvedTicketRequestDetails(3,4,5);
        int expectedSeats = 9;
        int actualSeats = resolvedTicketRequestDetails.getTotalNoOfSeatsToAllocate();

        assertEquals(expectedSeats, actualSeats);
    }
}