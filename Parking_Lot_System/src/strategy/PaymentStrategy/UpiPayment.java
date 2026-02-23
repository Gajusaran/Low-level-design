package strategy.PaymentStrategy;

public class UpiPayment implements PaymentStrategy{
    @Override
    public boolean pay(double price) {
        // Implement UPI payment logic here
        System.out.println("Processing UPI payment of amount: " + price);
        return true; // Assume payment is successful for this example
    }
    
}
