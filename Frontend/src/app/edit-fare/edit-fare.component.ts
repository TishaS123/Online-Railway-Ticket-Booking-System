import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FareService } from '../services/fare.service';
import { Fare } from '../fare.model';

import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-edit-fare',
  templateUrl: './edit-fare.component.html',
  styleUrls: ['./edit-fare.component.css']
})


export class EditFareComponent implements OnInit {
  fareForm!: FormGroup;
  fareId!: number;
  apiUrl = 'http://localhost:8002/fare'; // Replace with your actual backend URL


  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private fb: FormBuilder,
    private router: Router,
    private fareService: FareService,
    private toaster : ToastrService
  ) {}

  ngOnInit(): void {
    this.fareId = +this.route.snapshot.paramMap.get('id')!;
    this.fareForm = this.fb.group({
      source: ['', Validators.required],
      destination: ['', Validators.required],
      classType: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(0)]]
    });

    this.loadFare();
  }

  loadFare(): void {
    // this.http.get<any>(`${this.apiUrl}/${this.fareId}`).subscribe({
      this.fareService.getFareById(this.fareId).subscribe({
      next: (fare) => {
        this.fareForm.patchValue(fare)
      },
      error: (err) => alert('Failed to load fare details.')
    });
  }

  updateFare(): void {
    if (this.fareForm.valid) {
      const token = localStorage.getItem('token');
      const headers = {
        'Authorization':`Bearer ${token}`,
        'Content-Type': 'application/json'
      };
      this.http.put(`${this.apiUrl}/${this.fareId}`, this.fareForm.value, {headers, responseType:'text'}).subscribe({
        next: () => {
          // alert('Fare Details updated successfully!');
          this.toaster.success('Fare Details Updated successfully...', 'Success');
          this.router.navigate(['/admin-home/manage-fare-details']);
        }
      });
    }
  }
}

