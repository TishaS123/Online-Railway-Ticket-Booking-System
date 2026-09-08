import { Component } from '@angular/core';
import { Fare } from '../fare.model';
import { FareService } from '../services/fare.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-manage-fare',
  templateUrl: './manage-fare.component.html',
  styleUrls: ['./manage-fare.component.css']
})
export class ManageFareComponent {
  fareList: Fare[] = [];
  apiUrl = 'http://localhost:8002/fare'

  constructor(private fareService: FareService, private router : Router, private http : HttpClient, private toaster:ToastrService){}

  ngOnInit(){
    this.fareService.getAllFares().subscribe({
      next: (response) => {
        this.fareList = response;
      },
      error : (err) => {
        console.log("Error while fetching Fare List", err);
      }
    })
  }

  edit(fare: any): void {
    this.router.navigate(['/admin-home/edit-fare', fare.id]);
  }


  
delete(fare: any): void {
  const token = localStorage.getItem('token');
  const headers = {
    'Authorization': `Bearer ${token}`,
    'Content-Type':'application/json'
  };
    if (confirm(`Are you sure you want to delete fare ID ${fare.id}?`)) {
      this.http.delete(`${this.apiUrl}/${fare.id}`, {headers, responseType:'text'}).subscribe({
        next: () => {
          this.toaster.success('Fare deleted successfully!','Success');
          this.fareList = this.fareList.filter(f => f.id !== fare.id);
        },
        error: (err) => {
          console.error('Error deleting fare:', err);
          this.toaster.error('Failed to delete fare.','Error');
        }
      });
    }
}
}
