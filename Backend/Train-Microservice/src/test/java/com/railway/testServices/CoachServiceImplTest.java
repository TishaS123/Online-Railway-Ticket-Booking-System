//package com.railway.testServices;
//
//
//import com.railway.entity.Coaches;
//import com.railway.entity.Train;
//import com.railway.exception.CoachNotFoundException;
//import com.railway.feign.ReservationClient;
//import com.railway.repository.CoachRepository;
//import com.railway.service.CoachServiceImpl;
//import com.railway.service.TrainService;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class CoachServiceImplTest {
//
//    @InjectMocks
//    private CoachServiceImpl coachService;
//
//    @Mock
//    private CoachRepository coachRepo;
//
//    @Mock
//    private TrainService trainService;
//
//    @Mock
//    private ReservationClient reservationClient;
//
//    private Train train;
//    private Coaches coach;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        train = new Train();
//        train.setTrainId(1);
//        train.setTrainName("Express");
//        train.setSource("CityA");
//        train.setDestination("CityB");
//        train.setArrivalTime(LocalTime.of(10, 30));
//        train.setDepartureTime(LocalTime.of(11, 00));
//        train.setDate(LocalDate.now());
//
//        coach = new Coaches();
//        coach.setCoachId(1);
//        coach.setCoachNumber("C1");
//        coach.setClassType("AC");
//        coach.setTotalSeats(50);
//        coach.setTrain(train);
//    }
//
//    @Test
//    void testAddCoachDetails() {
//        when(trainService.getTrainById(1)).thenReturn(train);
//        coachService.addCoachDetails(coach, 1);
//        verify(coachRepo, times(1)).save(coach);
//    }
//
//    @Test
//    void testGetCoaches() {
//        when(coachRepo.findAll()).thenReturn(List.of(coach));
//        List<Coaches> result = coachService.getCoaches();
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void testGetCoachById_Success() throws CoachNotFoundException {
//        when(coachRepo.findById(1)).thenReturn(Optional.of(coach));
//        Coaches result = coachService.getCoaches(1);
//        assertEquals("C1", result.getCoachNumber());
//    }
//
//    @Test
//    void testGetCoachById_NotFound() {
//        when(coachRepo.findById(1)).thenReturn(Optional.empty());
//        assertThrows(CoachNotFoundException.class, () -> coachService.getCoaches(1));
//    }
//
//    @Test
//    void testUpdateCoach() throws CoachNotFoundException {
//        Coaches updated = new Coaches();
//        updated.setCoachNumber("C2");
//        updated.setClassType("Sleeper");
//        updated.setTotalSeats(60);
//        updated.setTrain(train);
//
//        when(coachRepo.findById(1)).thenReturn(Optional.of(coach));
//        when(coachRepo.save(any(Coaches.class))).thenReturn(updated);
//
//        Coaches result = coachService.updateCoach(1, updated);
//        assertEquals("C2", result.getCoachNumber());
//        assertEquals("Sleeper", result.getClassType());
//    }
//
//    @Test
//    void testDeleteCoach() {
//        coachService.deleteCoach(1);
//        verify(coachRepo, times(1)).deleteById(1);
//    }
//
//    @Test
//    void testGetCoachByTrainId() {
//        when(coachRepo.findByTrainTrainId(1)).thenReturn(List.of(coach));
//        List<Coaches> result = coachService.getCoachByTrainId(1);
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void testGetCoachCountByType() {
//        when(coachRepo.countByTrainTrainIdAndClassType(1, "AC")).thenReturn(2);
//        int count = coachService.getCoachCountByType(1, "AC");
//        assertEquals(2, count);
//    }
//
//    @Test
//    void testGetAvailableSeatByTrainIdAndClassType() {
//        when(coachRepo.findByTrainTrainIdAndClassType(1, "AC")).thenReturn(coach);
//        int seats = coachService.getAvailableSeatByTrainIdAndClassType(1, "AC");
//        assertEquals(50, seats);
//    }
//
//    @Test
//    void testDecreaseSeatInCoach() throws CoachNotFoundException {
//        when(coachRepo.findById(1)).thenReturn(Optional.of(coach));
//        coachService.decreaseSeatInCoach(1, 10);
//        assertEquals(40, coach.getTotalSeats());
//    }
//
//    @Test
//    void testIncreaseSeatInCoach() throws CoachNotFoundException {
//        when(coachRepo.findById(1)).thenReturn(Optional.of(coach));
//        coachService.increaseSeatInCoach(1, 10);
//        assertEquals(60, coach.getTotalSeats());
//    }
//}
//
