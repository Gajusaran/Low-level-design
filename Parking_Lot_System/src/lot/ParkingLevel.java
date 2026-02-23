package lot;

import manager.SpotManager;
import model.ParkingSpot;
import model.VehicleType;

import java.util.Map;

public class ParkingLevel {
    private final String levelId;
    private final Map<VehicleType, SpotManager> managers;

    public ParkingLevel(String levelId, Map<VehicleType, SpotManager> managers) {
        this.levelId = levelId;
        this.managers = managers;
    }

    public boolean hasAvailable(VehicleType vehicleType) {
        return managers.get(vehicleType).hasAvailability();
    }

    public ParkingSpot park(VehicleType vehicleType) {
        return  managers.get(vehicleType).allocateSpot();
    }

    public void unpark(VehicleType vehicleType, ParkingSpot spot) {
        managers.get(vehicleType).freeSpot(spot);
    }

    public String getLevelId() {
        return levelId;
    }
}
