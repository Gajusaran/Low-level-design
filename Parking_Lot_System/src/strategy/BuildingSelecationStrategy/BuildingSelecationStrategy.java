package strategy.BuildingSelecationStrategy;

import lot.ParkingBuilding;
import model.VehicleType;

import java.util.List;

public interface BuildingSelecationStrategy {
    public ParkingBuilding selectBuilding(List<ParkingBuilding> parkingBuildings, VehicleType vehicleType);
}
