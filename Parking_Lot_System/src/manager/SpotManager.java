package manager;

import model.ParkingSpot;
import model.Vehicle;
import model.VehicleType;
import strategy.SpotAllocation.SpotAllocationStrategy;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SpotManager {

    protected List<ParkingSpot> parkingSpots;
    protected final SpotAllocationStrategy spotAllocationStrategy;
    private final ReentrantLock lock = new ReentrantLock(true);

    protected SpotManager(List<ParkingSpot> parkingSpots, SpotAllocationStrategy spotAllocationStrategy) {
        this.parkingSpots = parkingSpots;
        this.spotAllocationStrategy = spotAllocationStrategy;
    }

    public void addSpot(ParkingSpot spot){
        parkingSpots.add(spot);
    };

    public ParkingSpot allocateSpot(){
        lock.lock();
        try {
            ParkingSpot spot =  spotAllocationStrategy.selectSpot(parkingSpots);
            if(spot == null){
                return null;
            }
            spot.park();
            return spot;
        }
        finally {
            lock.unlock();
        }
    };

    public void freeSpot(ParkingSpot spot){
        lock.lock();
        try {
            spot.unpark();
        }
        finally {
            lock.unlock();
        }
    };

    public boolean hasAvailability(){
        lock.lock();
        try{
            return parkingSpots.stream().anyMatch(ParkingSpot::isAvailable);
        }
        finally {
            lock.unlock();
        }
    };
}
