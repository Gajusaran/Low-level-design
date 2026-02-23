package gate;

import Service.ParkingSerivce;
import model.Ticket;
import model.Vehicle;

public class EntryGate {
    private final String gateId;
    private final ParkingSerivce parkingSerivce;

    public EntryGate(String gateId, ParkingSerivce parkingSerivce) {
        this.gateId = gateId;
        this.parkingSerivce = parkingSerivce;
    }

    public String getGateId() {
        return gateId;
    }

    public Ticket enter(Vehicle vehicle) {
        return parkingSerivce.parkVehicle(vehicle);
    }
};
