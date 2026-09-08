import { Component } from '@angular/core';
import { TrainService } from '../services/train.service';
import { Train } from '../train.model';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-add-train',
  templateUrl: './add-train.component.html',
  styleUrls: ['./add-train.component.css']
})
export class AddTrainComponent {
  constructor(private service : TrainService, private router: Router, private toaster: ToastrService){}

  train : Train = new Train();
  addTrain(){
    this.service.addTrainDetails(this.train)
      .subscribe({
        next: (response) => {
          // console.log('Train added successfully', response);
          // alert('Train added successfully');
          this.toaster.success('Train details added successfully', 'Success');
          this.router.navigate(['/admin-home/manage-train-details']);
        },
        error: (error) => {
          this.toaster.error('Failed to add train details...', 'Error');
        }
      });
  }

}
