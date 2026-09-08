import { Component, OnInit } from '@angular/core';
import { Ticket } from '../ticket.model';
import { ReservationService } from '../services/reservation.service';
import { PaymentRequest } from '../payment-request.model';
import { PaymentService } from '../services/payment.service';

import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
// export class PaymentComponent {
//     ticket !: Ticket;
//     paymentForm: FormGroup;
//     constructor(private reservationService: ReservationService, private paymentService: PaymentService, private fb: FormBuilder){
//       this.paymentForm = this.fb.group({
//       cardName: ['', [Validators.required]],
//       cardNumber: ['', [Validators.required, Validators.pattern(/^(\d{4} ?){3}\d{4}$/)]], 
//       expiry: ['', [Validators.required, Validators.pattern('(0[1-9]|1[0-2])\\/([0-9]{2})')]],
//       cvv: ['', [Validators.required, Validators.pattern('^[0-9]{3}$')]]      
//     })

//     }

    
// formatCardNumber(event: any) {
//     let input = event.target.value.replace(/\D/g, '').substring(0, 16);
//     let formatted = input.match(/.{1,4}/g)?.join(' ') || '';
//     this.paymentForm.get('cardNumber')?.setValue(formatted, { emitEvent: false });
//   }

    
    



// isSubmitting = false;

// onSubmit() {
//   if (this.paymentForm.valid) {
//     this.isSubmitting = true;

//     // Simulate API call
// //     setTimeout(() => {
// //       this.isSubmitting = false;
// //       alert('Payment successful!');
// //     }, 2000);
     
//   }
// }


export class PaymentComponent implements OnInit {
  ticket!: Ticket;
  paymentForm: FormGroup;
  isSubmitting = false;

  constructor(
    private reservationService: ReservationService,
    private fb: FormBuilder,
    private router: Router,private toaster : ToastrService
  ) {
    this.paymentForm = this.fb.group({
      cardName: ['', [Validators.required]],
      cardNumber: ['', [Validators.required, Validators.pattern(/^(\d{4} ?){3}\d{4}$/)]],
      expiry: ['', [Validators.required, Validators.pattern('(0[1-9]|1[0-2])\\/([0-9]{2})')]],
      cvv: ['', [Validators.required, Validators.pattern('^[0-9]{3}$')]]
    });
  }

  ngOnInit(): void {
    this.ticket = this.reservationService.getTicket();
    if (!this.ticket) {
      this.toaster.error("No ticket found. Please book again.", 'Error');
      this.router.navigate(['/passenger-form']);
    }
  }

  formatCardNumber(event: any) {
    let input = event.target.value.replace(/\D/g, '').substring(0, 16);
    let formatted = input.match(/.{1,4}/g)?.join(' ') || '';
    this.paymentForm.get('cardNumber')?.setValue(formatted, { emitEvent: false });
  }

  onSubmit(): void {
    if (this.paymentForm.valid) {
      this.isSubmitting = true;

      this.reservationService.makePayment(this.ticket).subscribe({
        next: (updatedTicket) => {
          this.isSubmitting = false;
        console.log("✅ Ticket after successful payment:", updatedTicket); // 👈 Log here

          this.toaster.success("Payment successful!");
          this.reservationService.setTicket(updatedTicket);
          this.router.navigate(['/user-home/show-ticket']); 
        },
        error: (err) => {
          this.isSubmitting = false;
          alert("Payment failed: " + err.message);
        }
      });
    } else {
      this.toaster.error("Please fill in all payment details correctly.",'Error');
    }
  }

}