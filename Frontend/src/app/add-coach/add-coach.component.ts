import { Component } from '@angular/core';
import { TrainService } from '../services/train.service';
import { CoachesService } from '../services/coaches.service';
import { Train } from '../train.model';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Coaches } from '../coaches.model';

@Component({
  selector: 'app-add-coach',
  templateUrl: './add-coach.component.html',
  styleUrls: ['./add-coach.component.css']
})


export class AddCoachComponent {

  trainList: Train[] = [];
  selectedTrainId: number = 0;
  constructor(private trainService: TrainService, private coachService: CoachesService, private router : Router, private toaster : ToastrService) { }
  coach: Coaches = new Coaches();

  ngOnInit() {
    this.trainService.getAllTrains().subscribe({
      next: (response) => {
        this.trainList = response;
        console.log('Train list:', this.trainList); // ✅ Move inside
      },
      error: (error) => {
        console.error('Error fetching trains', error);
      }
    });
  }

  addCoach() {
    this.coachService.addCoachesDetails(this.coach, this.selectedTrainId)
      .subscribe({
        next: (response) => {
          // console.log('Coach Details added successfully', response);
          // alert('Coach Details added successfully');
          this.toaster.success('Coach Details added successfully','Success')
          this.router.navigate(['/admin-home/manage-coach-details']);

        }
        , error: (err) => {
          // console.log("addCoach err ",err)
          this.toaster.error('failed to add coach', 'Error');
        }
      });
  }
}


