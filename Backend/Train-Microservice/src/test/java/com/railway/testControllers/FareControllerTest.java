//package com.railway.testControllers;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.railway.controller.FareController;
//import com.railway.entity.Fare;
//import com.railway.service.FareService;
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
//class FareControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private FareService fareService;
//
//    @InjectMocks
//    private FareController fareController;
//
//    private Fare fare;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(fareController).build();
//
//        fare = new Fare();
//        fare.setId(1);
//        fare.setAmount(500.0);
//        fare.setClassType("Sleeper");
//        fare.setSource("Mumbai");
//        fare.setDestination("Pune");
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void testAddFareDetails() throws Exception {
//        mockMvc.perform(post("/fare/addFareDetails")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(fare)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Fare Details added successfully.."));
//
//        verify(fareService, times(1)).addFareDetails(any(Fare.class));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void testGetFareById() throws Exception {
//        when(fareService.getFare(1)).thenReturn(fare);
//
//        mockMvc.perform(get("/fare/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.amount").value(500.0));
//    }
//
//    @Test
//    @WithMockUser(roles = {"ADMIN", "PASSENGER"})
//    void testGetAllFare() throws Exception {
//        when(fareService.getAllFare()).thenReturn(List.of(fare));
//
//        mockMvc.perform(get("/fare"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].classType").value("Sleeper"));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void testUpdateFare() throws Exception {
//        mockMvc.perform(put("/fare/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(fare)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Fare Details Updated Successfully..."));
//
//        verify(fareService, times(1)).updateFare(eq(1), any(Fare.class));
//    }
//
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void testDeleteFare() throws Exception {
//        mockMvc.perform(delete("/fare/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Fare Details Deleted Successfully..."));
//
//        verify(fareService, times(1)).deleteFare(1);
//    }
//
//    @Test
//    @WithMockUser(roles = "PASSENGER")
//    void testGetFareByRouteAndType() throws Exception {
//        when(fareService.getFareByRouteAndCoach("Mumbai", "Pune", "Sleeper")).thenReturn(500.0);
//
//        mockMvc.perform(get("/fare/getFareByRouteAndClassType")
//                .param("source", "Mumbai")
//                .param("destination", "Pune")
//                .param("classType", "Sleeper"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("500.0"));
//    }
//}
//
