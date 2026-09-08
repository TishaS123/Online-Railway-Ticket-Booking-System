import { Component } from '@angular/core';
import { Train } from '../train.model';
import { TrainService } from '../services/train.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-manage-train',
  templateUrl: './manage-train.component.html',
  styleUrls: ['./manage-train.component.css']
})
export class ManageTrainComponent {
  trainList: Train[] =[];

  apiUrl = 'http://localhost:8002/train';
  constructor(private trainService: TrainService, private router: Router, private http: HttpClient, private toaster : ToastrService){
   
  }

  ngOnInit(){
    this.trainService.getAllTrains().subscribe({
      next: (response) => {
        this.trainList = response;
      },
      error : (error) => {
        console.log("Error while fetching train list ", error);
      }
    })
  }

  edit(train: any): void {
    this.router.navigate(['/admin-home/edit-train', train.trainId]);
  }

  delete(train: any): void {
    const token = localStorage.getItem('token');
    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type' : 'application/json'
    };
    if (confirm(`Are you sure you want to delete train ID ${train.trainId}?`)) {
      this.http.delete(`${this.apiUrl}/${train.trainId}`, {headers, responseType:'text'}).subscribe({
        next: () => {
          this.toaster.success('Train Details deleted successfully!','Success');
          this.trainList = this.trainList.filter(t => t.trainId !== train.trainId);
        },
        error: (err) => {
          console.error('Error deleting train:', err);
          this.toaster.error('Failed to delete train details.', 'Error');
        }
      });
    }
}

}
