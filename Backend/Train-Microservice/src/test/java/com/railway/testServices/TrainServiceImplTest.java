//package com.railway.testServices;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.railway.entity.Train;
//import com.railway.repository.TrainRepository;
//import com.railway.service.TrainServiceImpl;
//
//import static org.mockito.Mockito.*;
//
//class TrainServiceImplTest {
//
//    @InjectMocks
//    private TrainServiceImpl trainService;
//
//    @Mock
//    private TrainRepository trainRepository;
//
//    private Train train;
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
//        train.setDepartureTime(LocalTime.of(11, 0));
//        train.setDate(LocalDate.now());
//    }
//
//    @Test
//    void testAddTrainDetails() {
//        trainService.addTrainDetails(train);
//        verify(trainRepository, times(1)).save(train);
//    }
//
//    @Test
//    void testGetTrainById() {
//        when(trainRepository.findByTrainId(1)).thenReturn(train);
//        Train result = trainService.getTrainById(1);
//        assertEquals("Express", result.getTrainName());
//    }
//
//    @Test
//    void testGetAllTrain() {
//        when(trainRepository.findAll()).thenReturn(List.of(train));
//        List<Train> result = trainService.getAllTrain();
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void testGetTrainBySourceAndDestination() {
//        when(trainRepository.findTrainBySourceAndDestination("CityA", "CityB")).thenReturn(List.of(train));
//        List<Train> result = trainService.getTrainBySourceAndDestination("CityA", "CityB");
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void testUpdateTrain() {
//        Train updated = new Train();
//        updated.setTrainName("Superfast");
//        updated.setSource("CityA");
//        updated.setDestination("CityC");
//        updated.setArrivalTime(LocalTime.of(12, 0));
//        updated.setDepartureTime(LocalTime.of(12, 30));
//        updated.setDate(LocalDate.now());
//
//        when(trainRepository.findByTrainId(1)).thenReturn(train);
//        when(trainRepository.save(any(Train.class))).thenReturn(updated);
//
//        Train result = trainService.updateTrain(1, updated);
//        assertEquals("Superfast", result.getTrainName());
//        assertEquals("CityC", result.getDestination());
//    }
//
//    @Test
//    void testDeleteTrain() {
//        trainService.delete(1);
//        verify(trainRepository, times(1)).deleteById(1);
//    }
//
//    @Test
//    void testGetTrainByIdAndSourceAndDestinationAndDate() {
//        LocalDate date = LocalDate.now();
//        when(trainRepository.findByTrainIdAndSourceAndDestinationAndDate(1, "CityA", "CityB", date)).thenReturn(train);
//        Train result = trainService.getTrainByIdAndSourceAndDestinationAndDate(1, "CityA", "CityB", date);
//        assertEquals("Express", result.getTrainName());
//    }
//}
