import { Component, OnInit } from '@angular/core';
import { Ticket } from '../ticket.model';
import { ReservationService } from '../services/reservation.service';
import { jwtDecode, JwtPayload } from 'jwt-decode';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-my-reservation',
  templateUrl: './my-reservation.component.html',
  styleUrls: ['./my-reservation.component.css']

})
export class MyReservationComponent implements OnInit {
  
  reservations : Ticket[] = [];

  constructor(private reservationService : ReservationService, private toaster: ToastrService){}

  ngOnInit(): void{
    const token = localStorage.getItem('token');
    if(token){
      const decoded = jwtDecode<JwtPayload>(token);
      const username = decoded.sub;
      
      if(username){
        this.reservationService.getMyReservations(username).subscribe({
          next: (response) => {
            this.reservations = response;
          },
          error : (err) => {
            console.error('Error fetching reservations', err);
          }
        });
      } else{
        console.error('Username not found in token');
      }
    }
  }

  
cancel(pnrNo: string) {
    this.reservationService.cancelTicket(pnrNo).subscribe({
      next: (response) => {
        alert(response); // or use a toast
        // Optionally refresh reservations
      },
      error: (err) => {
        alert('Failed to cancel ticket: ' + err.error);
      }
    });
  }


}
