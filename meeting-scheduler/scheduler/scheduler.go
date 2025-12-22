package scheduler

import (
	"sync"
	"sync/atomic"
	"time"
)

type Scheduler interface {
	Book(roomId int, start time.Time, end time.Time) (meetingId int, status bool)
	Avaible(start time.Time, end time.Time) (AvaibleRooms []int)
	Cancel(meetingId int) (success bool)
}

type SchedulerImpl struct {
	rooms     map[int]*Room // we need tracking of rooms - so we use id -> room object, thats why we are using map here
	roomsLock sync.RWMutex
	meetingId atomic.Int64
}

var (
	once     sync.Once
	instance *SchedulerImpl
)

func SchedulerInstance(roomNumber int) *SchedulerImpl {

	once.Do(func() {
		instance = &SchedulerImpl{
			rooms: make(map[int]*Room),
		}
		for i := 1; i <= roomNumber; i++ {
			instance.rooms[i] = NewRoom(i)
		}
	})
	return instance
}

// var _ Scheduler = (*SchedulerImpl)(nil)

func (s *SchedulerImpl) Book(roomId int, start time.Time, end time.Time) (int, bool) {
	//create a new meeting object
	s.roomsLock.RLock()
	defer s.roomsLock.RUnlock()

	r, exits := s.rooms[roomId]

	if !exits {
		return 0, false
	}

	meetingId := s.meetingId.Add(1)

	m := NewMeeting(int(meetingId), start, end)

	status := r.AddMeeting(m)

	if status {
		return int(meetingId), status
	} else {
		return 0, status
	}
}

func (s *SchedulerImpl) Avaible(start time.Time, end time.Time) []int {
	s.roomsLock.RLock()
	defer s.roomsLock.RUnlock()

	avbrooms := []int{}

	for _, r := range s.rooms {
		if r.Avaiable_slot(start, end) {
			avbrooms = append(avbrooms, r.ID)
		}
	}

	return avbrooms
}

func (s *SchedulerImpl) Cancel(meetingID int) bool {
	s.roomsLock.RLock()
	defer s.roomsLock.RUnlock()

	for _, r := range s.rooms {
		if r.RemoveMeeting(meetingID) {
			return true
		}
	}
	return false
}

