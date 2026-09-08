//package com.railway.testServices;
//
//
//import com.railway.entity.Fare;
//import com.railway.exception.FareNotFoundException;
//import com.railway.repository.FareRepository;
//import com.railway.service.FareServiceImpl;
//import com.railway.service.TrainService;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class FareServiceImplTest {
//
//    @InjectMocks
//    private FareServiceImpl fareService;
//
//    @Mock
//    private FareRepository fareRepository;
//
//    @Mock
//    private TrainService trainService;
//
//    private Fare sampleFare;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        sampleFare = new Fare();
//        sampleFare.setId(1);
//        sampleFare.setAmount(500.0);
//        sampleFare.setClassType("Sleeper");
//        sampleFare.setSource("Mumbai");
//        sampleFare.setDestination("Pune");
//    }
//
//    @Test
//    void testAddFareDetails() {
//        when(fareRepository.save(sampleFare)).thenReturn(sampleFare);
//        Fare result = fareService.addFareDetails(sampleFare);
//        assertEquals(sampleFare, result);
//    }
//
//    @Test
//    void testGetAllFare() {
//        List<Fare> fares = List.of(sampleFare);
//        when(fareRepository.findAll()).thenReturn(fares);
//        List<Fare> result = fareService.getAllFare();
//        assertEquals(1, result.size());
//        assertEquals(sampleFare, result.get(0));
//    }
//
//    @Test
//    void testGetFareById_Success() throws FareNotFoundException {
//        when(fareRepository.findById(1)).thenReturn(Optional.of(sampleFare));
//        Fare result = fareService.getFare(1);
//        assertEquals(sampleFare, result);
//    }
//
//    @Test
//    void testGetFareById_NotFound() {
//        when(fareRepository.findById(1)).thenReturn(Optional.empty());
//        assertThrows(FareNotFoundException.class, () -> fareService.getFare(1));
//    }
//
//    @Test
//    void testUpdateFare() throws FareNotFoundException {
//        Fare updatedFare = new Fare();
//        updatedFare.setAmount(600.0);
//        updatedFare.setClassType("AC");
//        updatedFare.setSource("Mumbai");
//        updatedFare.setDestination("Pune");
//
//        when(fareRepository.findById(1)).thenReturn(Optional.of(sampleFare));
//        when(fareRepository.save(any(Fare.class))).thenReturn(updatedFare);
//
//        Fare result = fareService.updateFare(1, updatedFare);
//        assertEquals(600.0, result.getAmount());
//        assertEquals("AC", result.getClassType());
//    }
//
//    @Test
//    void testDeleteFare() {
//        doNothing().when(fareRepository).deleteById(1);
//        fareService.deleteFare(1);
//        verify(fareRepository, times(1)).deleteById(1);
//    }
//
//    @Test
//    void testGetFareByRouteAndCoach_Success() {
//        when(fareRepository.findBySourceAndDestinationAndClassType("Mumbai", "Pune", "Sleeper"))
//                .thenReturn(Optional.of(sampleFare));
//        double amount = fareService.getFareByRouteAndCoach("Mumbai", "Pune", "Sleeper");
//        assertEquals(500.0, amount);
//    }
//
//    @Test
//    void testGetFareByRouteAndCoach_NotFound() {
//        when(fareRepository.findBySourceAndDestinationAndClassType("Mumbai", "Pune", "Sleeper"))
//                .thenReturn(Optional.empty());
//        assertThrows(FareNotFoundException.class, () ->
//                fareService.getFareByRouteAndCoach("Mumbai", "Pune", "Sleeper"));
//    }
//}
//
