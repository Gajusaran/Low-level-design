package scheduler

import (
	"sync"
	"time"
)

type Room struct {
	ID       int
	meetings []*Meeting // we are not using as slices of pointer not use pointer of slice
	mu       sync.RWMutex
}

func NewRoom(id int) *Room {
	return &Room{
		ID: id, //no need use make slice we use when we need fix size of slice
	}
}

// for add meeting  -
func (r *Room) AddMeeting(meeting *Meeting) bool {
	r.mu.Lock()
	defer r.mu.Unlock()

	for _, m := range r.meetings {
		if meeting.Start.Before(m.End) && meeting.End.After(m.Start) {
			return false
		}
	}

	r.meetings = append(r.meetings, meeting)

	return true
}

// for checking on that time and for particular slot is avaible or not
func (r *Room) Avaiable_slot(start time.Time, end time.Time) bool {
	r.mu.Lock()
	defer r.mu.Unlock()

	for _, m := range r.meetings {
		if start.Before(m.End) && end.After(m.Start) {
			return false
		}
	}

	return true
}

// for remove meeting -
func (r *Room) RemoveMeeting(Id int) bool {
	r.mu.Lock()
	defer r.mu.Unlock()

	for i, m := range r.meetings {
		if m.ID == Id {
			r.meetings = append(r.meetings[:i], r.meetings[i+1:]...)
			return true
		}
	}
	return false
}

// getting meetings details - will discuss why lock needed here
func (r *Room) GetMeeting() []Meeting {
	r.mu.Lock()
	defer r.mu.Unlock()

	//we return copy so noone do extra modification, if we use normal object but still we can do modification in slice so thats why we return copy

	meetingCopy := make([]Meeting, len(r.meetings)) // we have to use make bcz either we cant make new slice (internal impletation

	return meetingCopy
}

