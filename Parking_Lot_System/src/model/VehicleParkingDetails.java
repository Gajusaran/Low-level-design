package model;

import lot.ParkingBuilding;
import lot.ParkingLevel;

public class VehicleParkingDetails {
    ParkingSpot parkingSpot;
    ParkingBuilding parkingBuilding;
    ParkingLevel parkingLevel;

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public ParkingBuilding getParkingBuilding() {
        return parkingBuilding;
    }

    public void setParkingBuilding(ParkingBuilding parkingBuilding) {
        this.parkingBuilding = parkingBuilding;
    }

    public ParkingLevel getParkingLevel() {
        return parkingLevel;
    }

    public void setParkingLevel(ParkingLevel parkingLevel) {
        this.parkingLevel = parkingLevel;
    }
}
