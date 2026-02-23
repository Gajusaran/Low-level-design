package manager;

import model.ParkingSpot;
import strategy.SpotAllocation.SpotAllocationStrategy;

import java.util.List;

public class LargeVehicleSpotManager extends SpotManager {

    public LargeVehicleSpotManager(List<ParkingSpot> parkingSpots, SpotAllocationStrategy spotAllocationStrategy) {
        super(parkingSpots,spotAllocationStrategy);
    };
}
