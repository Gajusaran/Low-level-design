package strategy.PricingStrategy;

import java.time.Duration;

import model.Ticket;

public class FlatHourlyRateStrategy implements PricingStrategy{
    private double hourlyRate;

    public FlatHourlyRateStrategy(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double calculatePrice(Ticket ticket) {
        long hoursParked = Duration.between(ticket.getEntryTime(), ticket.getExitTime()).toHours();
        return hoursParked * hourlyRate;
    }
    
}
