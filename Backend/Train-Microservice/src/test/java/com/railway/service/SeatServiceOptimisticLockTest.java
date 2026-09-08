package com.railway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.railway.entity.Seat;
import com.railway.entity.SeatStatus;
import com.railway.exception.SeatBookingConflictException;
import com.railway.repository.SeatRepository;

class SeatServiceOptimisticLockTest {

    @Test
    void shouldFailWhenSeatIsAlreadyBooked() {
        SeatRepository seatRepository = mock(SeatRepository.class);
        SeatServiceImpl seatService = new SeatServiceImpl();
        ReflectionTestUtils.setField(seatService, "seatRepo", seatRepository);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("A1");
        seat.setStatus(SeatStatus.BOOKED);

        when(seatRepository.findAllByIdForUpdate(List.of(1L))).thenReturn(List.of(seat));

        SeatBookingConflictException exception = assertThrows(
            SeatBookingConflictException.class,
            () -> seatService.markSeatsAsBooked(List.of(1L))
        );

        assertTrue(exception.getMessage().contains("already booked"));
    }

    @Test
    void shouldHoldSeatBeforePaymentAndThenBookAfterConfirmation() {
        SeatRepository seatRepository = mock(SeatRepository.class);
        SeatServiceImpl seatService = new SeatServiceImpl();
        ReflectionTestUtils.setField(seatService, "seatRepo", seatRepository);

        Seat seat = new Seat();
        seat.setId(2L);
        seat.setSeatNumber("B2");
        seat.setStatus(SeatStatus.AVAILABLE);

        when(seatRepository.findAllByIdForUpdate(List.of(2L))).thenReturn(List.of(seat));

        seatService.holdSeats(List.of(2L));
        assertEquals(SeatStatus.LOCKED, seat.getStatus());
        assertNotNull(seat.getHoldExpiresAt());

        seatService.confirmSeatsBooked(List.of(2L));
        assertEquals(SeatStatus.BOOKED, seat.getStatus());
    }
}
