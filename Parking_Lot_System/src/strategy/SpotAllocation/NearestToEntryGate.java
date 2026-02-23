package strategy.SpotAllocation;

import model.ParkingSpot;

import java.util.List;

public class NearestToEntryGate implements SpotAllocationStrategy {
    @Override
    public ParkingSpot selectSpot(List<ParkingSpot> parkingSpots) {
        return parkingSpots.stream()
                .filter(ParkingSpot::isAvailable)
                .findFirst()
                .orElse(null);
    }
}
