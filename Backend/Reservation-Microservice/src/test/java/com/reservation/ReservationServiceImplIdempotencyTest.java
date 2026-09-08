//package com.reservation;
//
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.mockito.Mockito.verifyNoInteractions;
//import static org.mockito.Mockito.when;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import com.reservation.dto.TicketRequest;
//import com.reservation.entity.Ticket;
//import com.reservation.entity.TicketStatus;
//import com.reservation.feign.PaymentServiceClient;
//import com.reservation.feign.TrainServiceClient;
//import com.reservation.repo.PassengerRepository;
//import com.reservation.repo.TicketRepository;
//import com.reservation.service.EmailService;
//import com.reservation.service.ReservationServiceImpl;
//
//@ExtendWith(MockitoExtension.class)
//class ReservationServiceImplIdempotencyTest {
//
//    @Mock
//    private TicketRepository ticketRepository;
//
//    @Mock
//    private PassengerRepository passengerRepository;
//
//    @Mock
//    private TrainServiceClient trainClient;
//
//    @Mock
//    private PaymentServiceClient paymentClient;
//
//    @Mock
//    private EmailService emailService;
//
//    private ReservationServiceImpl reservationService;
//
//    @BeforeEach
//    void setUp() {
//        reservationService = new ReservationServiceImpl();
//        ReflectionTestUtils.setField(reservationService, "ticketRepository", ticketRepository);
//        ReflectionTestUtils.setField(reservationService, "passengerRepository", passengerRepository);
//        ReflectionTestUtils.setField(reservationService, "trainClient", trainClient);
//        ReflectionTestUtils.setField(reservationService, "paymentClient", paymentClient);
//        ReflectionTestUtils.setField(reservationService, "emailService", emailService);
//    }
//
//    @Test
//    void reserveTicket_shouldReturnExistingTicketForSameIdempotencyKey() throws Exception {
//        String idempotencyKey = "req-123";
//        Ticket existingTicket = new Ticket();
//        existingTicket.setTicketNo(99L);
//        existingTicket.setPnrNo("ABC123");
//        existingTicket.setStatus(TicketStatus.CONFIRMED);
//        existingTicket.setIdempotencyKey(idempotencyKey);
//
//        when(ticketRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.of(existingTicket));
//
//        TicketRequest request = new TicketRequest();
//        request.setIdempotencyKey(idempotencyKey);
//
//        Ticket result = reservationService.reserveTicket(request);
//
//        assertSame(existingTicket, result);
//        verifyNoInteractions(trainClient);
//        verifyNoInteractions(paymentClient);
//    }
//}
