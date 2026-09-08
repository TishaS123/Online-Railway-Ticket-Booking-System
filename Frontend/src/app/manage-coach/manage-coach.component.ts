import { Component } from '@angular/core';
import { Coaches } from '../coaches.model';
import { CoachesService } from '../services/coaches.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-manage-coach',
  templateUrl: './manage-coach.component.html',
  styleUrls: ['./manage-coach.component.css']
})
export class ManageCoachComponent {
  coachList: Coaches[] = [];
    apiUrl = 'http://localhost:8002/fare'
  
    constructor(private coachService: CoachesService, private router : Router, private http : HttpClient, private toaster: ToastrService){}
  
    ngOnInit(){
      this.coachService.getAllCoaches().subscribe({
        next: (response) => {
          this.coachList = response;
        },
        error : (err) => {
          console.log("Error while fetching Coach List", err);
        }
      })
    }
  
    edit(coach: any): void {
      this.router.navigate(['/admin-home/edit-coach', coach.coachId]);
    }
  
  
    
  delete(coach: any): void {
    const token = localStorage.getItem('token');
    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type':'application/json'
    };
      if (confirm(`Are you sure you want to delete coach ID ${coach.coachId}?`)) {
        this.http.delete(`${this.apiUrl}/${coach.coachId}`, {headers, responseType:'text'}).subscribe({
          next: () => {
            this.toaster.success('Coach deleted successfully!', 'Success');
            this.coachList = this.coachList.filter(c => c.coachId !== coach.coachId);
          },
          error: (err) => {
            console.error('Error deleting coach:', err);
            this.toaster.error('Failed to delete coach.','Error');
          }
        });
      }
}
}
