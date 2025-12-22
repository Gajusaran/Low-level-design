package main

import (
	"meeting-scheduler/scheduler"
	"fmt"
	"time"
)

func main() {
	r := scheduler.NewRoom(2)
	fmt.Println("Room created with ID:", r.ID)
	
	// Create a meeting
	meeting := scheduler.NewMeeting(1, time.Now(), time.Now().Add(1*time.Hour))
	fmt.Println("Meeting created:", meeting)
	
	// Add meeting to room
	success := r.AddMeeting(meeting)
	fmt.Println("Meeting added:", success)
}

