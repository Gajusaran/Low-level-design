package strategy.PricingStrategy;

import model.Ticket;
import model.Vehicle;

public interface PricingStrategy {
    public double calculatePrice(Ticket ticket);
}
