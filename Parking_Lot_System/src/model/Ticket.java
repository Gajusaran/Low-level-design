package model;

import lot.ParkingBuilding;
import lot.ParkingLevel;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
   private final String ticketId;
   private final Vehicle vehicle;
   private final ParkingSpot parkingSpot;
   private final ParkingLevel parkingLevel;
   private final ParkingBuilding parkingBuilding;
   private final LocalDateTime entryTime;
   private LocalDateTime exitTime;

   public Ticket(Vehicle vehicle, ParkingSpot parkingSpot, ParkingLevel parkingLevel, ParkingBuilding parkingBuilding, LocalDateTime entryTime) {
        this.ticketId = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.parkingLevel = parkingLevel;
        this.parkingBuilding = parkingBuilding;
        this.entryTime = entryTime;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public ParkingLevel getParkingLevel() {
        return parkingLevel;
    }

    public ParkingBuilding getParkingBuilding() {
        return parkingBuilding;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
}
