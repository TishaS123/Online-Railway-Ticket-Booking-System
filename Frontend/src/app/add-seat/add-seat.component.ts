import { Component } from '@angular/core';
import { Coaches } from '../coaches.model';
import { CoachesService } from '../services/coaches.service';
import { SeatService } from '../services/seat.service';
import { Seat } from '../seat.model';
import { CoachesDTO } from '../coaches-dto.model';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AddSeat } from '../add-seat.model';

@Component({
  selector: 'app-add-seat',
  templateUrl: './add-seat.component.html',
  styleUrls: ['./add-seat.component.css']
})
export class AddSeatComponent {
  coachList : Coaches[] = [];
  // selectedCoachId : number = 0;

  constructor(private toaster: ToastrService, private coachService : CoachesService, private seatService : SeatService, private router : Router){}

  seat : AddSeat = new AddSeat();


  ngOnInit() {
    this.coachService.getAllCoaches().subscribe({
      next : (response) => {
        this.coachList = response;
        console.log('Coach List: ', this.coachList);
      },
      error: (error) => {
        console.error('Error ', error);
      }
    });
   
}
addSeat() {

    this.coachService.getCoachById(this.seat.coachId).subscribe(coach => {
      const totalSeats = coach.totalSeats;
      const currentSeats = coach.seats.length;

      if(currentSeats < totalSeats){
        this.seatService.addSeatDetails(this.seat).subscribe({
          next: (response) => {
            // console.log('Seat Details added successfully', response);
            // alert('Seat Details added successfully');

            this.toaster.success('Seat Details added successfully','Success');
            this.router.navigate(['/admin-home/manage-seat-details']);

          }
          , error: (err) => {
            // console.log("addSeat err ",err)
            this.toaster.error('Failed to add seat details...', 'Error');
          }
        });
      }
      else{
        this.toaster.error("Coach is full. Cannot add more seats...", 'Error')
      }
    }
  )
  }
  
}
