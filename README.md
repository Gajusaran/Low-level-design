# Low Level Design (LLD) Repository

This repository contains implementations of various Low Level Design problems. Each problem is organized in its own folder with a complete, runnable solution.

## Repository Structure

```
lld/
├── README.md
└── [problem-name]/
    ├── README.md          # Problem description and design notes
    ├── go.mod             # Go module file
    ├── main.go            # Example usage/driver code
    └── [package]/         # Implementation code
```

## Problems

### 1. Meeting Scheduler
**Location:** `meeting-scheduler/`

A meeting room scheduling system that allows booking, checking availability, and canceling meetings. Implements thread-safe operations using mutexes and follows singleton pattern for the scheduler.

**Language:** Go

**Features:**
- Book meetings in rooms
- Check room availability for time slots
- Cancel meetings
- Thread-safe operations

**Run:**
```bash
cd meeting-scheduler
go run main.go
```

---

### 2. Parking Lot System
**Location:** `Parking_Lot_System/`

A multi-building, multi-level parking lot system with support for different vehicle types and spot types. Implements a layered architecture (Gate → Service → Domain → Repository) with thread-safe spot allocation.

**Language:** Java

**Features:**
- Multiple buildings, levels, and spot types (Compact, Regular, Large)
- Vehicle type support: Two-Wheeler, Four-Wheeler, Large
- Entry gate issues tickets; exit gate calculates fare and processes payment
- Thread-safe spot allocation using `ReentrantLock` (fair lock)
- Extensible via Strategy pattern: pricing, payment, building/level/spot selection
- Repository pattern for ticket storage

**Design Patterns:**
- Strategy (5 variations: building/level/spot selection, pricing, payment)
- Template Method (SpotManager hierarchy)
- Repository (TicketRepository)

**Run:**
```bash
cd Parking_Lot_System
javac -d out src/**/*.java src/*.java
java -cp out ParkingLotClient
```

## Adding New Problems

When adding a new problem:
1. Create a new folder with a descriptive name (use kebab-case)
2. Add a `README.md` describing the problem and design approach
3. Include all necessary code files
4. Update this main README.md with the new problem entry

## Language

Currently implemented in **Go**, but you can add problems in any language you prefer.

