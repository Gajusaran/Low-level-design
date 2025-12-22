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

## Adding New Problems

When adding a new problem:
1. Create a new folder with a descriptive name (use kebab-case)
2. Add a `README.md` describing the problem and design approach
3. Include all necessary code files
4. Update this main README.md with the new problem entry

## Language

Currently implemented in **Go**, but you can add problems in any language you prefer.

