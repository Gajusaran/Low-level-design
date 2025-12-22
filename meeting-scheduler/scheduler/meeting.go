package scheduler

import "time"

type Meeting struct {
	ID    int
	Start time.Time
	End   time.Time
}

func NewMeeting(id int, start_time time.Time, end_time time.Time) *Meeting {
	return &Meeting{
		ID:    id,
		Start: start_time,
		End:   end_time,
	}
}

