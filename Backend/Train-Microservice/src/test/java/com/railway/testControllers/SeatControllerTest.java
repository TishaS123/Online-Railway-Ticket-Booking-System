//package com.railway.testControllers;
//
//
//
//
//class SeatControllerTest {
//
////    private MockMvc mockMvc;
////
////    @Mock
////    private SeatService seatService;
////
////    @Mock
////    private CoachRepository coachRepo;
////
////    @Mock
////    private SeatRepository seatRepository;
////
////    @InjectMocks
////    private SeatController seatController;
////
////    private Seat seat;
////    private Coaches coach;
////
////    @BeforeEach
////    void setUp() {
////        MockitoAnnotations.openMocks(this);
////        mockMvc = MockMvcBuilders.standaloneSetup(seatController).build();
////
////        coach = new Coaches();
////        coach.setCoachId(1);
////        coach.setCoachNumber("C1");
////
////        seat = new Seat();
////        seat.setId(1L);
////        seat.setSeatNumber("A1");
////        seat.setStatus(SeatStatus.AVAILABLE);
////        seat.setCoach(coach);
////    }
////
////    @Test
////    @WithMockUser(roles = "ADMIN")
////    void testAddSeat() throws Exception {
////        mockMvc.perform(post("/seat/addSeat/1")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(new ObjectMapper().writeValueAsString(seat)))
////                .andExpect(status().isOk())
////                .andExpect(content().string("Seat Added Successfully"));
////
////        verify(seatService, times(1)).addSeat(any(Seat.class), eq(1));
////    }
////
////    @Test
////    @WithMockUser(roles = {"ADMIN", "PASSENGER"})
////    void testGetAvailableSeats() throws Exception {
////        when(seatService.getAvailableSeatsByCoachId(1)).thenReturn(List.of(seat));
////
////        mockMvc.perform(get("/seat/available/1"))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$[0].seatNumber").value("A1"));
////    }
////
////    @Test
////    @WithMockUser(roles = "PASSENGER")
////    void testMarkBooked() throws Exception {
////        List<Long> seatIds = List.of(1L);
////
////        mockMvc.perform(put("/seat/mark-booked")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(new ObjectMapper().writeValueAsString(seatIds)))
////                .andExpect(status().isOk());
////
////        verify(seatService, times(1)).markSeatsAsBooked(seatIds);
////    }
////
////    @Test
////    @WithMockUser(roles = "PASSENGER")
////    void testMarkAvailable() throws Exception {
////        List<Long> seatIds = List.of(1L);
////
////        mockMvc.perform(put("/seat/mark-available")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(new ObjectMapper().writeValueAsString(seatIds)))
////                .andExpect(status().isOk());
////
////        verify(seatService, times(1)).markSeatsAsAvailable(seatIds);
////    }
////
////    @Test
////    @WithMockUser(roles = "PASSENGER")
////    void testGetSeatIds() throws Exception {
////        SeatIdentifier identifier = new SeatIdentifier("C1", "A1", 101);
////        List<SeatIdentifier> identifiers = List.of(identifier);
////
////        when(coachRepo.findByCoachNumberAndTrainTrainId("C1", 101)).thenReturn(Optional.of(coach));
////        when(seatRepository.findBySeatNumberAndCoach_CoachId("A1", 1)).thenReturn(Optional.of(seat));
////
////        mockMvc.perform(post("/seat/seat/getSeatIds")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(new ObjectMapper().writeValueAsString(identifiers)))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$[0]").value(1));
////    }
//}
//
