# Parking Lot System — LLD Design

---

## Problem Statement

Design a **Parking Lot System** that can manage multiple buildings, multiple levels per building, and multiple spot types. The system should:

- Allow vehicles to enter and get a ticket
- Allocate the best available spot based on vehicle type
- Allow vehicles to exit, calculate fare, and process payment
- Free the spot after successful exit

---

## Requirements Gathering (Interview Step 1)

### Functional Requirements
- Support multiple **vehicle types**: Two-Wheeler, Four-Wheeler, Large
- Support multiple **spot types**: Compact, Regular, Large
- **Entry gate** issues a ticket on vehicle entry
- **Exit gate** calculates price and processes payment on exit
- Spot is freed only after **successful payment**
- Support multiple **buildings**, each with multiple **levels**
- Each level manages its own spots per vehicle type

### Non-Functional Requirements
- **Thread-safe** spot allocation (concurrent vehicles entering)
- **Extensible** — adding a new pricing strategy or payment method should not change existing code
- **Scalable** — new buildings/levels can be added without code change

### Assumptions / Clarifications
- One vehicle occupies exactly one spot
- A vehicle can only have one active ticket at a time
- Pricing is time-based (entry to exit)
- Payment is external — system only knows success/failure

---

## Architecture Choice

Two common approaches for this problem:

| Approach | Structure | When to Use |
|---|---|---|
| **Facade-style** | `Client → ParkingLot → Gates → Building` | Smaller scope, single entry point |
| **Layered / Service-oriented** | `Gate → Service → Domain → Repository` | SDE-2 level, mirrors real backend systems |

**We use the Layered approach** — it maps directly to how production systems are built:

```
Gate (Boundary Layer)       ← like a Controller in Spring
     ↓
ParkingService (App Layer)  ← orchestration, exception boundary
     ↓
ParkingLot → Building → Level → SpotManager (Domain Layer)
     ↓
TicketRepository (Data Layer)
```

---

## Class Design

### Core Domain Hierarchy

```
ParkingLot
 └── ParkingBuilding (1..*)
      └── ParkingLevel (1..*)
           └── SpotManager (one per VehicleType)
                └── ParkingSpot (1..*)
```

### Key Classes

| Class | Responsibility |
|---|---|
| `ParkingLot` | Entry point for domain; delegates to building selection strategy |
| `ParkingBuilding` | Holds levels; delegates to level selection strategy |
| `ParkingLevel` | Routes park/unpark to the right `SpotManager` by `VehicleType` |
| `SpotManager` | Thread-safe spot allocation and release |
| `ParkingSpot` | Holds spot state (`available`/`occupied`) |
| `Ticket` | Snapshot of entry — vehicle, spot, level, building, time |
| `TicketRepository` | In-memory store of active tickets |
| `ParkingService` | Orchestrates park/unpark flow; sole exception-throwing layer |
| `EntryGate` | Boundary — calls `parkVehicle`, returns ticket |
| `ExitGate` | Boundary — calls `unparkVehicle`, triggers payment |

---

## Design Patterns Used

### 1. Strategy Pattern
Applied in **5 places** — any behavior that may vary is extracted as a strategy:

| Strategy Interface | Purpose | Concrete Implementation |
|---|---|---|
| `BuildingSelectionStrategy` | Which building to park in | `PickFirstBuilding` |
| `LevelSelectionStrategy` | Which level to park on | `FirstLevelWithAvailability` |
| `SpotAllocationStrategy` | Which specific spot to assign | `NearestToEntryGate` |
| `PricingStrategy` | How to calculate fare | `FlatHourlyRateStrategy` |
| `PaymentStrategy` | How to process payment | `UpiPayment` |

> **Why Strategy?** Adding a new pricing model (e.g., weekend surge pricing) or payment method (e.g., Card) requires zero change to existing code — just add a new class implementing the interface. This satisfies the **Open/Closed Principle**.

### 2. Template Method Pattern (implicit)
`SpotManager` is an abstract class. `allocateSpot()`, `freeSpot()`, and `hasAvailability()` are implemented once with locking. Subclasses (`TwoWheelerSpotManager`, `FourWheelerSpotManager`, `LargeVehicleSpotManager`) only define their type — the algorithm stays in the parent.

### 3. Repository Pattern
`TicketRepository` abstracts ticket storage. The service layer never deals with `Map` directly — it talks to a repository. Swapping to a DB-backed implementation later requires no change in `ParkingService`.

### 4. Factory Pattern (noted, not fully implemented)
`PricingStrategyFactory` and `PaymentStrategyFactory` can create the right strategy based on enum input — decouples client from concrete strategy classes.

---

## Exception Handling Strategy

**Core rule**: each layer has one responsibility for errors.

```
SpotAllocationStrategy   →  return null       (couldn't find a spot)
SpotManager              →  return null       (propagate up)
ParkingLevel             →  return null       (propagate up)
ParkingBuilding          →  return null       (propagate up)
ParkingLot               →  return null       (propagate up)
─────────────────────────────────────────────────────────────────
ParkingService           →  throw exception   ← ONLY layer that throws
─────────────────────────────────────────────────────────────────
EntryGate / ExitGate     →  catch & handle    (user-facing response)
```

**Why not throw in inner layers?**
If `SpotManager` throws `ParkingFullException`, it now knows about a domain concept it shouldn't own. Lower layers should be dumb and reusable — the service layer is the semantic boundary.

### Exception types

| Exception | Thrown when |
|---|---|
| `RuntimeException("Parking Full")` | No building/level/spot available for vehicle type |
| `RuntimeException("Ticket Expired")` | Vehicle not found in active ticket store at exit |
| `RuntimeException("Payment failed")` | Payment strategy returns `false` |

> In production, these would be custom exception classes (`ParkingFullException`, `TicketNotFoundException`) for proper error handling and HTTP status mapping.

---

## Thread Safety

`SpotManager` uses a `ReentrantLock(true)` (fair lock) to prevent race conditions during concurrent spot allocation:

```java
public ParkingSpot allocateSpot() {
    lock.lock();
    try {
        ParkingSpot spot = spotAllocationStrategy.selectSpot(parkingSpots);
        if (spot == null) return null;
        spot.park();       // mark occupied inside the lock
        return spot;
    } finally {
        lock.unlock();
    }
}
```

**Fair lock** ensures threads are served in arrival order — no thread starvation.

**Note**: `hasAvailability()` and `allocateSpot()` are separately locked. A caller seeing `true` from `hasAvailability()` does not guarantee `allocateSpot()` won't return null — always handle the null return from `allocateSpot()`.

---

## Flow Walkthrough

### Entry Flow
```
EntryGate.enter(vehicle)
  → ParkingService.parkVehicle(vehicle)
    → ParkingLot.park(vehicleType)
      → BuildingSelectionStrategy.selectBuilding()   // picks a building
        → ParkingBuilding.park(vehicleType)
          → LevelSelectionStrategy.selectLevel()     // picks a level with availability
            → ParkingLevel.park(vehicleType)
              → SpotManager.allocateSpot()           // locks, picks spot, marks occupied
    → creates Ticket (vehicle + spot + level + building + entryTime)
    → TicketRepository.addTicket(ticket)
  ← returns Ticket to gate
```

### Exit Flow
```
ExitGate.exit(vehicle)
  → ParkingService.unparkVehicle(vehicle)
    → TicketRepository.getTicket(vehicle)            // lookup by vehicle reference
    → ticket.setExitTime(now)
    → PricingStrategy.calculatePrice(ticket)         // exitTime - entryTime * rate
    → PaymentStrategy.pay(amount)                    // UPI / Card / Cash
    → TicketRepository.removeTicket(ticket)
    → ParkingLot.unpark(ticket)
      → ParkingBuilding.unpark(ticket)
        → ParkingLevel.unpark(vehicleType, spot)
          → SpotManager.freeSpot(spot)               // locks, marks available
```

---

## Project Structure

```
src/
├── ParkingLotClient.java              # Main / demo
├── model/
│   ├── Vehicle.java
│   ├── VehicleType.java               # enum: TWO_WHEELER, FOUR_WHEELER, LARGE
│   ├── ParkingSpot.java
│   ├── SpotType.java                  # enum: COMPACT, REGULAR, LARGE
│   ├── Ticket.java
│   ├── VehicleParkingDetails.java     # DTO returned from domain layer
│   └── PaymentType.java
├── lot/
│   ├── ParkingLot.java
│   ├── ParkingBuilding.java
│   └── ParkingLevel.java
├── manager/
│   ├── SpotManager.java               # abstract, thread-safe
│   ├── TwoWheelerSpotManager.java
│   ├── FourWheelerSpotManager.java
│   └── LargeVehicleSpotManager.java
├── gate/
│   ├── EntryGate.java
│   ├── ExitGate.java
│   └── GateManager.java
├── Service/
│   └── ParkingSerivce.java            # orchestration + exception boundary
├── Repository/
│   └── TicketRepositry.java
└── strategy/
    ├── BuildingSelecationStrategy/
    │   ├── BuildingSelecationStrategy.java   (interface)
    │   └── PickFirstBuilding.java
    ├── LevelSelecation/
    │   ├── LevelSelecstionStrategy.java      (interface)
    │   └── FirstLevelWithAvaiability.java
    ├── SpotAllocation/
    │   ├── SpotAllocationStrategy.java       (interface)
    │   └── NearestToEntryGate.java
    ├── PricingStrategy/
    │   ├── PricingStrategy.java              (interface)
    │   └── FlatHourlyRateStrategy.java
    └── PaymentStrategy/
        ├── PaymentStrategy.java              (interface)
        └── UpiPayment.java
```

---

## How to Extend (Open/Closed in action)

**Add surge pricing on weekends:**
```java
public class WeekendSurgePricingStrategy implements PricingStrategy {
    public double calculatePrice(Ticket ticket) { ... }
}
// Plug in: new ParkingSerivce(lot, repo, payment, new WeekendSurgePricingStrategy())
// Zero existing code changed.
```

**Add card payment:**
```java
public class CardPayment implements PaymentStrategy {
    public boolean pay(double price) { ... }
}
```

**Add a new building or level:**
```java
parkingLot = new ParkingLot("Lot1", Arrays.asList(buildingA, buildingB), strategy);
```

No existing class changes. This is the payoff of the Strategy pattern.

---

## Interview Q&A Prep

**Q: Why separate `ParkingBuilding` and `ParkingLevel`?**
> Real parking lots have buildings and floors. Separating them allows independent selection strategies — e.g., "prefer ground floor" or "prefer Building A because it's nearer to exit". Each has its own strategy.

**Q: Why is `SpotManager` abstract instead of an interface?**
> The locking logic (`ReentrantLock`, `allocateSpot`, `freeSpot`, `hasAvailability`) is identical for all vehicle types. Abstract class lets us share this implementation via Template Method. An interface would force each subclass to duplicate locking code.

**Q: Why not store the ticket keyed by license plate string?**
> In this demo, the same `Vehicle` object is reused at entry and exit, so object identity works. In production, you'd key by `licensePlate` (String) and add `equals()`/`hashCode()` to `Vehicle` based on `licensePlate` — since entry and exit would create separate `Vehicle` objects from a scanner.

**Q: What happens if two vehicles try to take the last spot simultaneously?**
> `SpotManager.allocateSpot()` is wrapped in a `ReentrantLock`. Only one thread enters the critical section. The second thread waits, then finds the spot already marked occupied by `spot.park()`, and `selectSpot()` returns null — which propagates up as `ParkingFullException`.
