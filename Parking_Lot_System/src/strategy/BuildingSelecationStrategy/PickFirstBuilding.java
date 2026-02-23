package strategy.BuildingSelecationStrategy;

import java.util.List;

import lot.ParkingBuilding;
import model.VehicleType;

public class PickFirstBuilding implements BuildingSelecationStrategy{
    @Override
    public ParkingBuilding selectBuilding(List<ParkingBuilding> parkingBuildings, VehicleType vehicleType) {
        for(ParkingBuilding building : parkingBuildings){
            if(building.canPark(vehicleType)){
                return building;
            }
        }
        return null;
    }
}
