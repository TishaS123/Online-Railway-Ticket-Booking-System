//package com.railway.testControllers;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.railway.controller.TrainController;
//import com.railway.entity.Train;
//import com.railway.service.TrainService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//class TrainControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private TrainService trainService;
//
//    @InjectMocks
//    private TrainController trainController;
//
//    private Train train;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(trainController).build();
//
//        train = new Train();
//        train.setTrainId(1);
//        train.setTrainName("Express");
//        train.setSource("Mumbai");
//        train.setArrivalTime(LocalTime.of(10, 30));
//        train.setDepartureTime(LocalTime.of(11, 00));
//        train.setDate(LocalDate.of(2025, 5, 28));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void testAddTrainDetails() throws Exception {
//        mockMvc.perform(post("/train/addTrainDetails")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(train)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Train Details are added successfully..."));
//
//        verify(trainService, times(1)).addTrainDetails(any(Train.class));
//    }
//
//    @Test
//    void testGetTrainByTrainId() throws Exception {
//        when(trainService.getTrainById(1)).thenReturn(train);
//
//        mockMvc.perform(get("/train/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.trainName").value("Express"));
//
//        verify(trainService, times(1)).getTrainById(1);
//    }
//
//    @Test
//    void testGetAllTrains() throws Exception {
//        when(trainService.getAllTrain()).thenReturn(List.of(train));
//
//        mockMvc.perform(get("/train"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].trainName").value("Express"));
//
//        verify(trainService, times(1)).getAllTrain();
//    }
//
//    @Test
//    void testGetAllTrainsFromSourceAndDestination() throws Exception {
//        when(trainService.getTrainBySourceAndDestination("Mumbai", "Pune")).thenReturn(List.of(train));
//
//        mockMvc.perform(get("/train/Mumbai/Pune"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].source").value("Mumbai"));
//
//        verify(trainService, times(1)).getTrainBySourceAndDestination("Mumbai", "Pune");
//    }
//
//    @Test
//    void testUpdateTrainDetails() throws Exception {
//        mockMvc.perform(put("/train/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(train)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Train Details Updated Successfully..."));
//
//        verify(trainService, times(1)).updateTrain(eq(1), any(Train.class));
//    }
//
//    @Test
//    void testDeleteTrain() throws Exception {
//        mockMvc.perform(delete("/train/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Train Details Deleted Successfully."));
//
//        verify(trainService, times(1)).delete(1);
//    }
//
//    @Test
//    void testGetTrainByIdSourceDestinationDate() throws Exception {
//        when(trainService.getTrainByIdAndSourceAndDestinationAndDate(1, "Mumbai", "Pune", LocalDate.of(2025, 5, 28)))
//                .thenReturn(train);
//
//        mockMvc.perform(get("/train/getTrainByIdSourceDestinationDate")
//                .param("trainId", "1")
//                .param("source", "Mumbai")
//                .param("destination", "Pune")
//                .param("date", "2025-05-28"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.trainName").value("Express"));
//
//        verify(trainService, times(1)).getTrainByIdAndSourceAndDestinationAndDate(1, "Mumbai", "Pune", LocalDate.of(2025, 5, 28));
//    }
//}
//
