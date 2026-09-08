import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SeatService } from '../services/seat.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-edit-seat',
  templateUrl: './edit-seat.component.html',
  styleUrls: ['./edit-seat.component.css']
})
export class EditSeatComponent implements OnInit{

  seatForm!: FormGroup;
    seatId!: number;
    apiUrl = 'http://localhost:8002/seat/update-seat'; // Replace with your actual backend URL
  
  
    constructor(
      private route: ActivatedRoute,
      private http: HttpClient,
      private fb: FormBuilder,
      private router: Router,
      private seatService: SeatService,
      private toaster : ToastrService
    ) {}
  
    ngOnInit(): void {
      this.seatId = +this.route.snapshot.paramMap.get('id')!;
      this.seatForm = this.fb.group({
        seatNumber: ['', Validators.required],
        status: ['', Validators.required]
      });
  
      this.loadSeat();
    }
  
    loadSeat(): void {
        this.seatService.getSeatById(this.seatId).subscribe({
        next: (seat) => {
          this.seatForm.patchValue(seat)
        },
        error: (err) => this.toaster.error('Failed to load fare details.', 'Error')
      });
    }
  
    updateSeat(): void {
      if (this.seatForm.valid) {
        const token = localStorage.getItem('token');
        const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type':'application/json'
    };
        this.http.put(`${this.apiUrl}/${this.seatId}`, this.seatForm.value, {headers, responseType:'text'}).subscribe({
          next: () => {
            this.toaster.success('Seat Details updated successfully!', 'Success');
            this.router.navigate(['/admin-home/manage-seat-details']);
          },
          error : (error) => {
            this.toaster.error("Failed to update seat details...", 'Error');
          }
        });
      }
    }
}
