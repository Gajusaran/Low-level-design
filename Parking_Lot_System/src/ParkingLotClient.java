import Repository.TicketRepositry;
import Service.ParkingSerivce;
import gate.EntryGate;
import gate.ExitGate;
import lot.ParkingBuilding;
import lot.ParkingLevel;
import lot.ParkingLot;
import manager.FourWheelerSpotManager;
import manager.TwoWheelerSpotManager;
import model.*;
import strategy.BuildingSelecationStrategy.PickFirstBuilding;
import strategy.LevelSelecation.FirstLevelWithAvaiability;
import strategy.PaymentStrategy.UpiPayment;
import strategy.PricingStrategy.FlatHourlyRateStrategy;
import strategy.SpotAllocation.NearestToEntryGate;

import java.util.Arrays;
import java.util.Map;

public class ParkingLotClient {
    public static void main(String[] args) {

        // --- Spot Allocation Strategy ---
        NearestToEntryGate spotStrategy = new NearestToEntryGate();

        // --- Level 1: spots for two-wheelers and four-wheelers ---
        TwoWheelerSpotManager bikeManager = new TwoWheelerSpotManager(
                Arrays.asList(
                        new ParkingSpot("B1", SpotType.COMPACT, true),
                        new ParkingSpot("B2", SpotType.COMPACT, true)
                ),
                spotStrategy
        );

        FourWheelerSpotManager carManager = new FourWheelerSpotManager(
                Arrays.asList(
                        new ParkingSpot("C1", SpotType.REGULAR, true),
                        new ParkingSpot("C2", SpotType.REGULAR, true)
                ),
                spotStrategy
        );

        ParkingLevel level1 = new ParkingLevel("L1", Map.of(
                VehicleType.TWO_WHEELER, bikeManager,
                VehicleType.FOUR_WHEELER, carManager
        ));

        // --- Building A with Level 1 ---
        ParkingBuilding buildingA = new ParkingBuilding(
                "BuildingA",
                Arrays.asList(level1),
                new FirstLevelWithAvaiability()
        );

        // --- Parking Lot ---
        ParkingLot parkingLot = new ParkingLot(
                "Lot1",
                Arrays.asList(buildingA),
                new PickFirstBuilding()
        );

        // --- Service layer ---
        TicketRepositry ticketRepo = new TicketRepositry();
        ParkingSerivce service = new ParkingSerivce(
                parkingLot,
                ticketRepo,
                new UpiPayment(),
                new FlatHourlyRateStrategy(50.0)  // Rs 50 per hour
        );

        // --- Gates ---
        EntryGate entryGate = new EntryGate("E1", service);
        ExitGate exitGate = new ExitGate("X1", service);

        // --- Scenario 1: Park a car ---
        Vehicle car = new Vehicle("MH12AB1234", VehicleType.FOUR_WHEELER);
        Ticket carTicket = entryGate.enter(car);
        System.out.println("Car parked. Ticket ID: " + carTicket.getTicketId());
        System.out.println("Spot: " + carTicket.getParkingSpot().getSpotID()
                + " | Level: " + carTicket.getParkingLevel().getLevelId()
                + " | Building: " + carTicket.getParkingBuilding().getBuildingId());

        // --- Scenario 2: Park a bike ---
        Vehicle bike = new Vehicle("MH12XY5678", VehicleType.TWO_WHEELER);
        Ticket bikeTicket = entryGate.enter(bike);
        System.out.println("\nBike parked. Ticket ID: " + bikeTicket.getTicketId());
        System.out.println("Spot: " + bikeTicket.getParkingSpot().getSpotID());

        // --- Scenario 3: Exit the car ---
        System.out.println("\nCar exiting...");
        exitGate.exit(car);
        System.out.println("Car exited successfully.");

        // --- Scenario 4: Try parking when full ---
        System.out.println("\nTrying to park 3 more cars (only 1 spot left after car exited)...");
        Vehicle car2 = new Vehicle("MH12CD9999", VehicleType.FOUR_WHEELER);
        Vehicle car3 = new Vehicle("MH12EF1111", VehicleType.FOUR_WHEELER);

        try {
            entryGate.enter(car2);
            System.out.println("Car2 parked.");
            entryGate.enter(car3);
            System.out.println("Car3 parked.");
            entryGate.enter(new Vehicle("MH12GH2222", VehicleType.FOUR_WHEELER));
            System.out.println("Car4 parked.");
        } catch (RuntimeException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
    }
}
