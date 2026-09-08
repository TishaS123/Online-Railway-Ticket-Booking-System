import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CoachesDTO } from '../coaches-dto.model';
import { CoachesService } from '../services/coaches.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-edit-coach',
  templateUrl: './edit-coach.component.html',
  styleUrls: ['./edit-coach.component.css']
})
export class EditCoachComponent implements OnInit {
  coachForm !: FormGroup;
  coachId !: number;

  apiUrl = 'http://localhost:8002/coach';

  constructor(private route : ActivatedRoute, private router : Router, private http: HttpClient, private fb : FormBuilder, private coachService : CoachesService, private toaster: ToastrService){}

  ngOnInit(): void{
    this.coachId = +this.route.snapshot.paramMap.get('coachId')!;
    this.coachForm = this.fb.group({
      coachNumber: ['', Validators.required],
      classType: ['', Validators.required],
      totalSeats: ['', Validators.required]
    });

    this.loadCoach();
    
  }

  loadCoach(): void{
    this.coachService.getCoachById(this.coachId).subscribe({
      next: (coach)=>{
        this.coachForm.patchValue(coach);
      },
      error: (err) => this.toaster.error('Failed to load coach details.', 'Error')
    })
  }

  updateCoach(): void {
    if (this.coachForm.valid) {
      const token = localStorage.getItem('token');
      const headers = {
        'Authorization':`Bearer ${token}`,
        'Content-Type': 'application/json'
      };
      this.http.put(`${this.apiUrl}/${this.coachId}`, this.coachForm.value, {headers}).subscribe({
        next: () => {
          // alert('Coach Details updated successfully!');
          this.toaster.success('Coach Details updated successfully!', 'Success');
          this.router.navigate(['/admin-home/manage-coach-details']);
        },
        error : (error) => {
          this.toaster.error("Failed to update coach details...", 'Error');
        }
      });
    }
  }
}
