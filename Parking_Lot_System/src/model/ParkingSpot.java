package model;

public class ParkingSpot {
    private final String spotID;
    private final SpotType spotType;
    private boolean available;

    public ParkingSpot(String spotID, SpotType spotType, boolean parked) {
        this.spotID = spotID;
        this.spotType = spotType;
        this.available = parked;
    }

    public String getSpotID() {
        return spotID;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void park(){
        available = false;
    }

    public void unpark(){
        available = true;
    }
}
