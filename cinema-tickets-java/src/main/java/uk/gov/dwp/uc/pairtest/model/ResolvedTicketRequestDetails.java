package uk.gov.dwp.uc.pairtest.model;

import uk.gov.dwp.uc.pairtest.constants.TicketConfig;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

/*
 * This record contains the processed details from a TicketTypeRequest*/
public record ResolvedTicketRequestDetails(int totalNoOfInfantTickets, int totalNoOfChildTickets,
                                           int totalNoOfAdultTickets) {

    private int getTicketPrice(TicketTypeRequest.Type ticketType) {
        return switch (ticketType) {
            case ADULT -> TicketConfig.ADULT_TICKET_PRICE;
            case CHILD -> TicketConfig.CHILD_TICKET_PRICE;
            case INFANT -> TicketConfig.INFANT_TICKET_PRICE;
        };
    }

    public int getTotalNoOfTickets() {
        return totalNoOfAdultTickets + totalNoOfChildTickets + totalNoOfInfantTickets;
    }

    public int getTotalNoOfSeatsToAllocate() {
        return getTotalNoOfTickets() - totalNoOfInfantTickets;
    }

    public int getTotalPrice() {
        int totalAdultPrice = totalNoOfAdultTickets * getTicketPrice(TicketTypeRequest.Type.ADULT);
        int totalChildPrice = totalNoOfChildTickets * getTicketPrice(TicketTypeRequest.Type.CHILD);
        int totalInfantPrice = totalNoOfInfantTickets * getTicketPrice(TicketTypeRequest.Type.INFANT);

        return totalAdultPrice + totalChildPrice + totalInfantPrice;
    }

}
