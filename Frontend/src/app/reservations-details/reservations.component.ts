import { Component } from '@angular/core';
import { Reservation } from '../reservation.model';
import { ReservationService } from '../services/reservation.service';

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css']
})
export class ReservationsComponent {
  reservationList : Reservation[] = [];

  constructor(private reservationService : ReservationService){}

  

  ngOnInit(){
    this.reservationService.getAllReservations().subscribe({
      next : (response) => {
        this.reservationList = response;
        console.log('Reservation List: ', this.reservationList);
      },
      error: (error) => {
        console.error('Error ', error);
      }
    });
  }
}
