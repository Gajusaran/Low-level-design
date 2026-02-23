package lot;

import model.ParkingSpot;
import model.Ticket;
import model.VehicleParkingDetails;
import model.VehicleType;
import strategy.LevelSelecation.LevelSelecstionStrategy;

import java.util.List;

public class ParkingBuilding {
    private final String buildingId;
    private  List<ParkingLevel> parkingLevels;
    private final LevelSelecstionStrategy levelSelecstionStrategy;

    public ParkingBuilding(String buildingId,  List<ParkingLevel> parkingLevels, LevelSelecstionStrategy levelSelecstionStrategy) {
        this.buildingId = buildingId;
        this.parkingLevels = parkingLevels;
        this.levelSelecstionStrategy = levelSelecstionStrategy;
    }

    public void addParkingLevel(ParkingLevel parkingLevel) {
        this.parkingLevels.add(parkingLevel);
    }

    public String getBuildingId() {
        return buildingId;
    }

    public boolean canPark(VehicleType vehicleType) {
        for(ParkingLevel level : parkingLevels){
            if(level.hasAvailable(vehicleType)){
                return true;
            }
        }
        return false;
    }

    public VehicleParkingDetails park(VehicleType vehicletype) {
        VehicleParkingDetails vehicleParkingDetails = new VehicleParkingDetails();
        ParkingLevel level = levelSelecstionStrategy.selectLevel(parkingLevels,vehicletype);
        if(level == null){ return null;}
        ParkingSpot spot = level.park(vehicletype);
        if(spot == null){ return null;}
        vehicleParkingDetails.setParkingSpot(spot);
        vehicleParkingDetails.setParkingLevel(level);
        return vehicleParkingDetails;
    }

    public void unpark(Ticket ticket) {
        ticket.getParkingLevel().unpark(ticket.getVehicle().getVehicleType(),ticket.getParkingSpot());
    }
}
