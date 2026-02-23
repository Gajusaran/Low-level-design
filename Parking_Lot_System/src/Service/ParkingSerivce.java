package Service;

import Repository.TicketRepositry;
import lot.ParkingLot;
import model.Ticket;
import model.Vehicle;
import model.VehicleParkingDetails;
import strategy.PaymentStrategy.PaymentStrategy;
import strategy.PricingStrategy.PricingStrategy;

import java.time.LocalDateTime;

public class ParkingSerivce {
    private final ParkingLot parkingLot;
    private final TicketRepositry ticketRepositry;
    private final PaymentStrategy paymentStrategy;
    private final PricingStrategy pricingStrategy;

    public ParkingSerivce(ParkingLot parkingLot, TicketRepositry ticketRepositry, PaymentStrategy paymentStrategy, PricingStrategy pricingStrategy) {
        this.parkingLot = parkingLot;
        this.ticketRepositry = ticketRepositry;
        this.paymentStrategy = paymentStrategy;
        this.pricingStrategy = pricingStrategy;
    }

    public Ticket parkVehicle(Vehicle vehicle) {

        VehicleParkingDetails details =
                parkingLot.park(vehicle.getVehicleType());

        if (details == null) {
            throw new RuntimeException("Parking Full");
        }

        Ticket ticket = new Ticket(
                vehicle,
                details.getParkingSpot(),
                details.getParkingLevel(),
                details.getParkingBuilding(),
                LocalDateTime.now()
        );

        ticketRepositry.addTicket(ticket);

        return ticket;
    }

    public void unparkVehicle(Vehicle vehicle) {
        Ticket ticket = ticketRepositry.getTicket(vehicle);

        if (ticket == null) {
            throw new RuntimeException("ticket Expired");
        }

        ticket.setExitTime(LocalDateTime.now());

        double amount = pricingStrategy.calculatePrice(ticket);

        boolean success = paymentStrategy.pay(amount);

        if(!success){
            throw new RuntimeException("Payment failed. Exit denied.");
        }

        ticketRepositry.removeTicket(ticket);

        parkingLot.unpark(ticket);
    }
}

