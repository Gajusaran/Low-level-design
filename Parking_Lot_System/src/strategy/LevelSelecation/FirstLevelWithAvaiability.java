package strategy.LevelSelecation;

import java.util.List;

import lot.ParkingLevel;
import model.VehicleType;

public class FirstLevelWithAvaiability implements LevelSelecstionStrategy{
    @Override
    public ParkingLevel selectLevel(List<ParkingLevel> parkingLevels, VehicleType vehicleType) {
        for(ParkingLevel level : parkingLevels){
            if(level.hasAvailable(vehicleType)){
                return level;
            }
        }
        return null;
    }
    
}
