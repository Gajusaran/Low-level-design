package gate;

import Service.ParkingSerivce;
import model.Vehicle;

public class ExitGate {
    private final String gateId;
    private final ParkingSerivce parkingSerivce;

    public ExitGate(String gateId, ParkingSerivce parkingSerivce) {
        this.gateId = gateId;
        this.parkingSerivce = parkingSerivce;
    }

    public String getGateId(){
        return gateId;
    }

    public void exit(Vehicle vehicle){
         parkingSerivce.unparkVehicle(vehicle);
    }
}
