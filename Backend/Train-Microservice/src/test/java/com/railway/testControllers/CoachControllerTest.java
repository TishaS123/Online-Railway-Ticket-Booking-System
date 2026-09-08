//package com.railway.testControllers;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.railway.controller.CoachController;
//import com.railway.entity.Coaches;
//import com.railway.service.CoachService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.security.test.context.support.WithMockUser;
//
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//class CoachControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private CoachService coachService;
//
//    @InjectMocks
//    private CoachController coachController;
//
//    private Coaches coach;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(coachController).build();
//
//        coach = new Coaches();
//        coach.setCoachId(1);
//        coach.setCoachNumber("C1");
//        coach.setClassType("Sleeper");
//        coach.setTotalSeats(72);
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void testAddCoachDetails() throws Exception {
//        mockMvc.perform(post("/coach/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(coach)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Coach Details Added Successfully.."));
//
//        verify(coachService, times(1)).addCoachDetails(any(Coaches.class), eq(1));
//    }
//
//    @Test
//    @WithMockUser(roles = "PASSENGER")
//    void testGetCoach() throws Exception {
//        when(coachService.getCoaches(1)).thenReturn(coach);
//
//        mockMvc.perform(get("/coach/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.coachNumber").value("C1"));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void testGetAllCoaches() throws Exception {
//        when(coachService.getCoaches()).thenReturn(List.of(coach));
//
//        mockMvc.perform(get("/coach"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].classType").value("Sleeper"));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void testUpdateCoach() throws Exception {
//        when(coachService.updateCoach(eq(1), any(Coaches.class))).thenReturn(coach);
//
//        mockMvc.perform(put("/coach/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(coach)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.coachNumber").value("C1"));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void testDeleteCoach() throws Exception {
//        mockMvc.perform(delete("/coach/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Coach details deleted successfully..."));
//
//        verify(coachService, times(1)).deleteCoach(1);
//    }
//
//    @Test
//    @WithMockUser(roles = "PASSENGER")
//    void testGetCoachesByTrain() throws Exception {
//        when(coachService.getCoachByTrainId(1)).thenReturn(List.of(coach));
//
//        mockMvc.perform(get("/coach/train/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].coachNumber").value("C1"));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void testGetCountByType() throws Exception {
//        when(coachService.getCoachCountByType(1, "Sleeper")).thenReturn(3);
//
//        mockMvc.perform(get("/coach/count")
//                .param("trainId", "1")
//                .param("classType", "Sleeper"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("3"));
//    }
//
//    @Test
//    @WithMockUser(roles = "PASSENGER")
//    void testGetAvailableSeats() throws Exception {
//        when(coachService.getAvailableSeatByTrainIdAndClassType(1, "Sleeper")).thenReturn(20);
//
//        mockMvc.perform(get("/coach/getAvailableSeats")
//                .param("trainId", "1")
//                .param("classType", "Sleeper"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("20"));
//    }
//
//    @Test
//    @WithMockUser(roles = "PASSENGER")
//    void testDecreaseSeatInCoach() throws Exception {
//        mockMvc.perform(put("/coach/decreaseSeatInCoach")
//                .param("coachId", "1")
//                .param("updateBySeats", "5"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Coach Details Updated Successfully..."));
//
//        verify(coachService, times(1)).decreaseSeatInCoach(1, 5);
//    }
//
//    @Test
//    @WithMockUser(roles = "PASSENGER")
//    void testIncreaseSeatInCoach() throws Exception {
//        mockMvc.perform(put("/coach/increaseSeatInCoach")
//                .param("coachId", "1")
//                .param("updateBySeats", "5"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Coach seats are added successfully..."));
//
//        verify(coachService, times(1)).increaseSeatInCoach(1, 5);
//    }
//}
