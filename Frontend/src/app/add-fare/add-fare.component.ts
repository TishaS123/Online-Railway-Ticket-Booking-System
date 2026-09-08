import { Component } from '@angular/core';
import { Fare } from '../fare.model';
import { FareService } from '../services/fare.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-add-fare',
  templateUrl: './add-fare.component.html',
  styleUrls: ['./add-fare.component.css']
})
export class AddFareComponent {

  fare : Fare = new Fare();

  constructor(private fareService : FareService, private router : Router, private toaster : ToastrService){}

  addFare(){
    this.fareService.addFareDetails(this.fare)
      .subscribe({
        next: (response) => {
          // console.log('Fare added successfully', response);
          // alert('Fare added successfully');
          this.toaster.success('Fare added successfully...', 'Success');
          this.router.navigate(['/admin-home/manage-fare-details']);

        },
        error: (error) => {
          // alert('Failed to add fare details...');
          this.toaster.error('Failed to add fare details...', 'Error');
        }
      });
  }
}
