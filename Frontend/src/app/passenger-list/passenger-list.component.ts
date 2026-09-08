import { Component } from '@angular/core';
import { Passenger } from '../passenger.model';
import { ReservationService } from '../services/reservation.service';

@Component({
  selector: 'app-passenger-list',
  templateUrl: './passenger-list.component.html',
  styleUrls: ['./passenger-list.component.css']
})
export class PassengerListComponent {

  passengerList : Passenger[] = [];
  constructor(private reservationService: ReservationService){
    
  }

  ngOnInit(){
    this.reservationService.getAllPassengers().subscribe({
      next: (response) => {
        this.passengerList = response;
      },
      error: (err) => {
        console.log('Error while fetching Passenger List ', err);
      }
    })
  }


}
