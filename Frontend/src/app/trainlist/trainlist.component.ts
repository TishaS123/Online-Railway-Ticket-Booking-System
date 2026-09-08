import { Component, OnInit } from '@angular/core';
import { TrainService } from '../services/train.service';
import { CoachesDTO } from '../coaches-dto.model';
import { ReservationService } from '../services/reservation.service';
import { Router } from '@angular/router';
import { TrainResponseDTO } from '../train-response-dto.model';
import { DatePipe } from '@angular/common';
import { TicketRequest } from '../ticket-request.model';
import { Subject, takeUntil } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { FareService } from '../services/fare.service';


@Component({
  selector: 'app-trainlist',
  templateUrl: './trainlist.component.html',
  styleUrls: ['./trainlist.component.css']
})
export class TrainlistComponent implements OnInit {
  
  private destroy$ = new Subject<void>();
  trainResponseDto: TrainResponseDTO | null = null;
  showDetails: { [key: number]: boolean } = {};
constructor(private authService: AuthService, private trainService: TrainService, private reservationService: ReservationService, private router: Router, private datePipe: DatePipe, private fareService: FareService ) {}
  
ngOnInit(): void {
    this.trainService.searchParams$
    .pipe(takeUntil(this.destroy$))
    .subscribe(params => {
      if (params) {
        
        const formattedDate = this.datePipe.transform(params.date, 'yyyy-MM-dd');

        this.trainService.getTrainResponseDto(
          params.source,
          params.destination,
          params.classType,
//           params.date
          formattedDate!
        
        ).subscribe({
          next: (data) => {
            const payload = Array.isArray(data) ? data[0] : data;

            if (!payload) {
              this.trainResponseDto = null;
              return;
            }

            const normalizeCoach = (coach: any, routeFare: number) => {
              const seatList = Array.isArray(coach?.seats) ? coach.seats : [];
              const availableSeats = Number(
                coach?.availableSeats ??
                seatList.filter((seat: any) => (seat?.status || '').toString().toUpperCase() === 'AVAILABLE').length ??
                0
              );

              return {
                ...coach,
                availableSeats,
                fare: Number(coach?.fare ?? routeFare ?? 0),
                availabilityChecked: true
              };
            };

            this.fareService.getFareByRouteAndClassType(params.source, params.destination, params.classType).subscribe({
              next: (routeFare) => {
                this.trainResponseDto = {
                  ...payload,
                  date: payload.date ? new Date(payload.date) : new Date(),
                  coaches: (payload.coaches || []).map((coach: any) => normalizeCoach(coach, routeFare))
                };
              },
              error: () => {
                this.trainResponseDto = {
                  ...payload,
                  date: payload.date ? new Date(payload.date) : new Date(),
                  coaches: (payload.coaches || []).map((coach: any) => normalizeCoach(coach, 0))
                };
              }
            });

          },
          error: (err) => console.error('Error fetching trains', err)
        });
      }
    });
}

ngOnDestroy(): void{
  this.destroy$.next();
  this.destroy$.complete();
}

  
toggleDetails(trainId: number): void {
    this.showDetails[trainId] = !this.showDetails[trainId];
  }



refreshAvailability(coach: CoachesDTO): void {
    coach.availabilityChecked = true;
    // Optional: simulate update
    // coach.availableSeats = Math.floor(Math.random() * coach.totalSeats);
  }



bookTicket(train: any, coach: any): void {
  const selectedTrain = this.trainResponseDto ?? train;

  if (!selectedTrain?.date) {
    return;
  }

  const formattedDate = new Date(selectedTrain.date).toISOString().split('T')[0];

  const ticketRequest: TicketRequest = {
    trainId: train.trainId,
    source: train.source,
    destination: train.destination,
    classType: coach.classType,
    date: formattedDate,
    passengers: []
  };
  console.log(formattedDate);
  this.reservationService.setTicketRequest(ticketRequest);
  if(this.authService.isLoggedIn()){
    this.router.navigate(['/user-home/passenger']);
  }
  else{
    this.router.navigate(['/login']);
  }
}

  goToSearch(){
    this.router.navigate(['/user-home'])
  }


}
