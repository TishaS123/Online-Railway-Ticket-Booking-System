import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TrainService } from '../services/train.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-edit-train',
  templateUrl: './edit-train.component.html',
  styleUrls: ['./edit-train.component.css']
})
export class EditTrainComponent {
  trainForm !: FormGroup;
  trainId!: number;

  apiUrl = 'http://localhost:8002/train';

  constructor(private route : ActivatedRoute, private router : Router, private http : HttpClient, private fb : FormBuilder, private trainService : TrainService, private toaster: ToastrService){}

  ngOnInit(): void{
    this.trainId = +this.route.snapshot.paramMap.get('trainId')!;
    this.trainForm = this.fb.group({
      trainName: ['', Validators.required],
      source: ['', Validators.required],
      destination: ['', Validators.required],
      arrivalTime: ['', Validators.required,],
      departureTime: ['', Validators.required],
      date: ['', Validators.required]
    })

    this.loadTrain();
  }

  loadTrain(): void{
    this.trainService.getTrainByTrainId(this.trainId).subscribe({
      next: (train) => {
        this.trainForm.patchValue(train);
      },
      error: (error) => {
        this.toaster.error("Failed to load train details", 'Error');
      }
    })
  }

  updateTrain(): void{
    if(this.trainForm.valid){
      const token = localStorage.getItem('token');
      const headers = {
        'Authorization': `Bearer ${token}`,
        'Content-Type':'application/json'
      };
      this.http.put(`${this.apiUrl}/${this.trainId}`,this.trainForm.value, {headers}).subscribe({
        next: () => {
          this.toaster.success('Train Details updated successfully...','Success');
          this.router.navigate(['/admin-home/manage-train-details']);
        },
        error: (error) => {
          this.toaster.error('Failed to update train details...', 'Error');
        }
      })
    }
  }


}
