package strategy.LevelSelecation;

import lot.ParkingLevel;
import model.VehicleType;

import java.util.List;

public interface LevelSelecstionStrategy {
    public ParkingLevel selectLevel(List<ParkingLevel> parkingLevels,VehicleType vehicleType);
}
