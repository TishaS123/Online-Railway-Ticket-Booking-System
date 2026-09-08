import { Component } from '@angular/core';
import { Seat } from '../seat.model';
import { SeatService } from '../services/seat.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-manage-seat',
  templateUrl: './manage-seat.component.html',
  styleUrls: ['./manage-seat.component.css']
})
export class ManageSeatComponent {
  seatList: Seat[] = [];
    apiUrl = 'http://localhost:8002/seat'
  
    constructor(private seatService: SeatService, private router : Router, private http : HttpClient, private toaster: ToastrService){}
  
    ngOnInit(){
      this.seatService.getAllSeats().subscribe({
        next: (response) => {
          this.seatList = response;
        },
        error : (err) => {
          console.log("Error while fetching Fare List", err);
        }
      })
    }
  
    edit(seat: any): void {
      this.router.navigate(['/admin-home/edit-seat', seat.id]);
    }
  
  
    
  delete(seat: any): void {
    const token = localStorage.getItem('token');
    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type':'application/json'
    };
      if (confirm(`Are you sure you want to delete seat ID ${seat.id}?`)) {
        this.http.delete(`${this.apiUrl}/${seat.id}`, {headers, responseType:'text'}).subscribe({
          next: () => {
            this.toaster.success('Seat deleted successfully!','Success');
            this.seatList = this.seatList.filter(s => s.id !== seat.id);
          },
          error: (err) => {
            console.error('Error deleting seat:', err);
            this.toaster.error('Failed to delete seat.', 'Error');
          }
        });
      }
  }
}
