package manager;

import model.ParkingSpot;
import strategy.SpotAllocation.SpotAllocationStrategy;

import java.util.List;

public class FourWheelerSpotManager extends SpotManager{
    public FourWheelerSpotManager(List<ParkingSpot> parkingSpots, SpotAllocationStrategy spotAllocationStrategy) {
        super(parkingSpots, spotAllocationStrategy);
    }
}
