//package com.railway.testServices;
//
//
//import com.railway.entity.Coaches;
//import com.railway.entity.Seat;
//import com.railway.entity.SeatStatus;
//import com.railway.repository.SeatRepository;
//import com.railway.service.CoachService;
//import com.railway.service.SeatServiceImpl;
//import com.railway.exception.CoachNotFoundException;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class SeatServiceImplTest {
//
//    @InjectMocks
//    private SeatServiceImpl seatService;
//
//    @Mock
//    private SeatRepository seatRepo;
//
//    @Mock
//    private CoachService coachService;
//
//    private Coaches coach;
//    private Seat seat;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        coach = new Coaches();
//        coach.setCoachId(1);
//        coach.setCoachNumber("C1");
//        coach.setClassType("Sleeper");
//        coach.setTotalSeats(72);
//
//        seat = new Seat();
//        seat.setId(1L);
//        seat.setSeatNumber("A1");
//        seat.setStatus(SeatStatus.AVAILABLE);
//        seat.setCoach(coach);
//    }
//
//    @Test
//    void testAddSeat_Success() throws CoachNotFoundException {
//        when(coachService.getCoaches(1)).thenReturn(coach);
//        when(seatRepo.save(any(Seat.class))).thenReturn(seat);
//
//        seatService.addSeat(seat, 1);
//
//        verify(seatRepo, times(1)).save(seat);
//        assertEquals(coach, seat.getCoach());
//    }
//
//    @Test
//    void testGetSeatByCoachId() {
//        when(seatRepo.findByCoach_CoachId(1)).thenReturn(seat);
//        Seat result = seatService.getSeatByCoachId(1);
//        assertEquals(seat, result);
//    }
//
//    @Test
//    void testGetAvailableSeatsByCoachId() {
//        List<Seat> seats = List.of(seat);
//        when(seatRepo.findByCoach_CoachIdAndStatus(1, SeatStatus.AVAILABLE)).thenReturn(seats);
//        List<Seat> result = seatService.getAvailableSeatsByCoachId(1);
//        assertEquals(1, result.size());
//        assertEquals(SeatStatus.AVAILABLE, result.get(0).getStatus());
//    }
//
//    @Test
//    void testMarkSeatsAsBooked() {
//        seat.setStatus(SeatStatus.AVAILABLE);
//        List<Long> seatIds = List.of(1L);
//        when(seatRepo.findAllById(seatIds)).thenReturn(List.of(seat));
//
//        seatService.markSeatsAsBooked(seatIds);
//
//        assertEquals(SeatStatus.BOOKED, seat.getStatus());
//        verify(seatRepo, times(1)).saveAll(anyList());
//    }
//
//    @Test
//    void testMarkSeatsAsAvailable() {
//        seat.setStatus(SeatStatus.BOOKED);
//        List<Long> seatIds = List.of(1L);
//        when(seatRepo.findAllById(seatIds)).thenReturn(List.of(seat));
//
//        seatService.markSeatsAsAvailable(seatIds);
//
//        assertEquals(SeatStatus.AVAILABLE, seat.getStatus());
//        verify(seatRepo, times(1)).saveAll(anyList());
//    }
//}
//
