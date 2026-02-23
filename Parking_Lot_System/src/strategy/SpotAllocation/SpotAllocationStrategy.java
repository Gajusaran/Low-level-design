package strategy.SpotAllocation;

import model.ParkingSpot;

import java.util.List;

public interface SpotAllocationStrategy {
    public ParkingSpot selectSpot(List<ParkingSpot> parkingSpots);
}
