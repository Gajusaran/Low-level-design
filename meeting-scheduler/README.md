# Meeting Scheduler

A thread-safe meeting room scheduling system implemented in Go.

## Problem Statement

Design a meeting room scheduler that allows users to:
- Book a meeting room for a specific time slot
- Check which rooms are available for a given time slot
- Cancel a meeting

## Design Approach

### Components

1. **Meeting**: Represents a meeting with ID, start time, and end time
2. **Room**: Manages meetings for a single room with thread-safe operations
3. **Scheduler**: Singleton service that manages multiple rooms and provides booking operations

### Key Design Decisions

- **Thread Safety**: Uses `sync.RWMutex` for concurrent access protection
- **Singleton Pattern**: Scheduler uses singleton pattern to ensure single instance
- **Atomic Operations**: Meeting IDs are generated using atomic operations
- **Time Overlap Detection**: Uses interval overlap logic to check conflicts

### API

```go
// Book a room
meetingId, success := scheduler.Book(roomId, startTime, endTime)

// Check available rooms
availableRooms := scheduler.Avaible(startTime, endTime)

// Cancel a meeting
success := scheduler.Cancel(meetingId)
```

## Running

```bash
go run main.go
```

