package manager;

import model.ParkingSpot;
import model.VehicleType;
import strategy.SpotAllocation.SpotAllocationStrategy;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class TwoWheelerSpotManager extends SpotManager {

    public TwoWheelerSpotManager(List<ParkingSpot> parkingSpots, SpotAllocationStrategy spotAllocationStrategy) {
        super(parkingSpots,spotAllocationStrategy);
    }
}
