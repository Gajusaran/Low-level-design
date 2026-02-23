1.Facade-style orchestration

2.Layered / Service-oriented design (Gate → Service → Domain)  -> we use this 


1. Facade-style orchestration
Client → ParkingLot (Facade)
↓
EntranceGate / ExitGate
↓
ParkingBuilding

2. Layered / Service-oriented design - perfred for sde2
   Gate (Boundary) - kind of controller funcation
   ↓
   ParkingService (Application Layer)
   ↓
   ParkingLot (Domain)
   ↓
   Level → Spot


🧩 Mental Model
Facade = "One Main Controller"

Like:

AmazonApp.placeOrder()

Internally it calls multiple services.

Layered = "Clean Architecture"

Like:

Controller → Service → Domain → Repository

This is how real backend systems are structured.


Client (user/app)
↓
System / Facade   ← (Controller equivalent)
↓
Service
↓
Domain + Repository


controller/
service/
domain/
repository/
strategy/ (if needed)
factory/ (if needed)

Controller
↓
Service
↓
Domain
↓
Repository


Exception Handling Philosophy for LLD Interviews
The Core Rule: Layer-based responsibility

--> Strategy Layer     →  return null   (I couldn't find anything)
Manager/Lot Layer  →  return null   (propagate up)
Service Layer      →  throw         (convert null to domain exception)
Gate/Client Layer  →  catch & handle (user-facing response)

