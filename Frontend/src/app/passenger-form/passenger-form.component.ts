import { Component } from '@angular/core';
import { TicketRequest } from '../ticket-request.model';
import { Passenger } from '../passenger.model';
import { ReservationService } from '../services/reservation.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { BookingStatus } from '../booking-status.model';
import { Ticket } from '../ticket.model';

@Component({
  selector: 'app-passenger-form',
  templateUrl: './passenger-form.component.html',
  styleUrls: ['./passenger-form.component.css']
})
export class PassengerFormComponent {
  
  ticketRequest!: TicketRequest;
  passengers: Passenger[] = [];
  ticket! : Ticket;
  constructor(private reservationService: ReservationService, private router: Router, private toaster: ToastrService) {}

  ngOnInit(): void {
    const request = this.reservationService.getTicketRequest();
    if (!request) {
//       this.router.navigate(['/trains']);
      return;
    }
  
  this.ticketRequest = request;
      this.addPassenger(); // Start with one passenger
    }

  

addPassenger(): void {
  const newPassenger: Passenger = {
    // passengerId: 0,
    passengerName: '',
    age: 0,
    gender: '',
    email: '',
    seatNumber:'',
    coachNumber:'',
    trainName: '',
    pnrNo: '',
    source: '',
    destination: '',
    trainId: 0,
    status: BookingStatus.CONFIRMED
  };
  this.passengers.push(newPassenger);
  console.log(this.passengers);
  console.log(this.ticketRequest);
  console.log(this.ticketRequest.date);
}




  removePassenger(index: number): void {
    this.passengers.splice(index, 1);
  }

  
submitPassengers(): void {
    this.ticketRequest.passengers = this.passengers;
    console.log('Final TicketRequest: ' , this.ticketRequest);

    this.reservationService.bookTicket(this.ticketRequest).subscribe({
      next: (response) => {
        this.ticket = response;
        this.toaster.success('Passenger Details Added Successfully!!!', 'Success');
        this.reservationService.setTicket(this.ticket);
        console.log(this.ticket);
        this.router.navigate(['/user-home/make-payment']);
      },
      error: (error) => {
        const message = this.getErrorMessage(error);
        this.toaster.error(message, 'Booking failed');
        console.error('Booking error:', error);
      }
    });
  }

  private getErrorMessage(error: any): string {
    if (error?.status === 403) {
      return 'Booking is not authorized. Please log in again and retry.';
    }

    if (error?.status === 409) {
      return error?.error || 'The selected seat is temporarily locked by another user.';
    }

    if (typeof error?.error === 'string' && error.error.trim().length > 0) {
      return error.error;
    }

    if (error?.error?.message) {
      return error.error.message;
    }

    if (error?.message) {
      return error.message;
    }

    return 'Seat is unavailable. Please select another seat and try again.';
  }
}
