package lot;

import model.*;
import strategy.BuildingSelecationStrategy.BuildingSelecationStrategy;

import java.util.List;

public class ParkingLot {
    private final String parkingLotId;
    private final List<ParkingBuilding> parkingBuildings;
    private final BuildingSelecationStrategy buildingSelecationStrategy;


    public ParkingLot(String parkingLotId, List<ParkingBuilding> parkingBuildings, BuildingSelecationStrategy buildingSelecationStrategy) {
        this.parkingLotId = parkingLotId;
        this.parkingBuildings = parkingBuildings;
        this.buildingSelecationStrategy = buildingSelecationStrategy;
    }

    public String getParkingLotId() {
        return parkingLotId;
    }

    public VehicleParkingDetails park(VehicleType vehicleType) {
        ParkingBuilding selectedBuilding =  buildingSelecationStrategy.selectBuilding(parkingBuildings,vehicleType);
        if(selectedBuilding == null){
            return null;
        }
        VehicleParkingDetails parkingDetails  = selectedBuilding.park(vehicleType);
        if(parkingDetails == null) {
            return null;
        }
        parkingDetails.setParkingBuilding(selectedBuilding);
        return parkingDetails;
    }

    public void unpark(Ticket ticket){
        ParkingBuilding parkingBuilding = ticket.getParkingBuilding();
        parkingBuilding.unpark(ticket);
    }
}
