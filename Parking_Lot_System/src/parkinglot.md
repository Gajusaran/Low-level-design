```mermaid

classDiagram

%% =========================
%% ROOT
%% =========================

class ParkingLot {
    - String parkingLotId
    - List~ParkingBuilding~ buildings
    - List~EntranceGate~ entranceGates
    - List~ExitGate~ exitGates
    - BuildingSelectionStrategy buildingSelectionStrategy
    + park(Vehicle) Ticket
    + unpark(Ticket, PaymentType) void
}

%% =========================
%% BUILDING & FLOOR
%% =========================

class ParkingBuilding {
    - String buildingId
    - List~ParkingFloor~ floors
    + park(Vehicle) ParkingSpot
    + unpark(Ticket) void
}

class ParkingFloor {
    - String floorId
    - Map~SpotType, ParkingSpotManager~ spotManagers
    - Map~Vehicle, ParkingSpotManager~ vehicleSpotMap
    + park(Vehicle) ParkingSpot
    + unpark(Vehicle) void
}

ParkingLot --> ParkingBuilding
ParkingBuilding --> ParkingFloor

%% =========================
%% GATES
%% =========================

class EntranceGate {
    - String gateId
    + generateTicket(Vehicle) Ticket
}

class ExitGate {
    - String gateId
    - PricingStrategy pricingStrategy
    + processExit(Ticket, PaymentType) void
}

ParkingLot --> EntranceGate
ParkingLot --> ExitGate

%% =========================
%% SPOT MANAGEMENT
%% =========================

class ParkingSpotManager {
    <<interface>>
    + allocateSpot(Vehicle) ParkingSpot
    + freeSpot(ParkingSpot) void
    + hasAvailability(VehicleType) boolean
}

class TwoWheelerSpotManager {
    - List~ParkingSpot~ spots
    - SpotAllocationStrategy allocationStrategy
    - RentalLock rentalLock
}

class FourWheelerSpotManager {
    - List~ParkingSpot~ spots
    - SpotAllocationStrategy allocationStrategy
    - RentalLock rentalLock
}

class LargeVehicleSpotManager {
    - List~ParkingSpot~ spots
    - SpotAllocationStrategy allocationStrategy
    - RentalLock rentalLock
}

ParkingSpotManager <|.. TwoWheelerSpotManager
ParkingSpotManager <|.. FourWheelerSpotManager
ParkingSpotManager <|.. LargeVehicleSpotManager

ParkingFloor --> ParkingSpotManager

class RentalLock {
    + lock() void
    + unlock() void
}

TwoWheelerSpotManager --> RentalLock
FourWheelerSpotManager --> RentalLock
LargeVehicleSpotManager --> RentalLock

%% =========================
%% SPOT
%% =========================

class ParkingSpot {
    - String spotId
    - SpotType spotType
    - boolean isAvailable
    + park() void
    + unpark() void
    + isAvailable() boolean
}

ParkingSpotManager --> ParkingSpot

class SpotType {
    <<enumeration>>
    TWO_WHEELER
    FOUR_WHEELER
    LARGE
}

%% =========================
%% VEHICLE
%% =========================

class Vehicle {
    - String vehicleNumber
    - VehicleType type
}

class VehicleType {
    <<enumeration>>
    TWO_WHEELER
    FOUR_WHEELER
    LARGE
}

%% =========================
%% STRATEGIES
%% =========================

class BuildingSelectionStrategy {
    <<interface>>
    + selectBuilding(List~ParkingBuilding~, Vehicle) ParkingBuilding
}

class NearestBuildingStrategy

BuildingSelectionStrategy <|.. NearestBuildingStrategy
ParkingLot --> BuildingSelectionStrategy

class SpotAllocationStrategy {
    <<interface>>
    + selectSpot(List~ParkingSpot~) ParkingSpot
}

class NearestSpotStrategy

SpotAllocationStrategy <|.. NearestSpotStrategy
ParkingSpotManager --> SpotAllocationStrategy

%% =========================
%% TICKET
%% =========================

class Ticket {
    - String ticketId
    - LocalDateTime entryTime
    - LocalDateTime exitTime
    - Vehicle vehicle
    - ParkingSpot spot
    - double totalAmount
}

Ticket --> Vehicle
Ticket --> ParkingSpot

%% =========================
%% PRICING
%% =========================

class PricingStrategy {
    <<interface>>
    + calculatePrice(Ticket) double
}

class HourlyPricingStrategy
class FlatPricingStrategy
class VehicleBasedPricingStrategy

PricingStrategy <|.. HourlyPricingStrategy
PricingStrategy <|.. FlatPricingStrategy
PricingStrategy <|.. VehicleBasedPricingStrategy

ExitGate --> PricingStrategy

%% =========================
%% PAYMENT
%% =========================

class PaymentType {
    <<enumeration>>
    UPI
    CARD
    CASH
}

class PaymentStrategy {
    <<interface>>
    + pay(double) boolean
}

class UpiPayment
class CardPayment
class CashPayment

PaymentStrategy <|.. UpiPayment
PaymentStrategy <|.. CardPayment
PaymentStrategy <|.. CashPayment
