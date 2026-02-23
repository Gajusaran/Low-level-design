package Repository;

import model.Ticket;
import model.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class TicketRepositry {
    private Map<Vehicle, Ticket> activetickets;

    public TicketRepositry() {
        this.activetickets = new HashMap<>();
    }

    public Map<Vehicle, Ticket> getActivetickets() {
        return activetickets;
    }

    public void addTicket(Ticket ticket){
        activetickets.put(ticket.getVehicle(), ticket);
    }

    public Ticket getTicket(Vehicle vehicle){
        return activetickets.get(vehicle);
    }

    public void removeTicket(Ticket ticket){
        activetickets.remove(ticket.getVehicle());
    }
}
