import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { loadStripe, Stripe, StripeCardElement } from '@stripe/stripe-js';
import { ReservationService } from '../services/reservation.service';
import { tick } from '@angular/core/testing';
import { Ticket } from '../ticket.model';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-make-payment',
  templateUrl: './make-payment.component.html',
  styleUrls: ['./make-payment.component.css']
})
export class MakePaymentComponent implements OnInit {
   stripe: Stripe | null = null;
  card: StripeCardElement | null = null;
  ticket !: Ticket;

  constructor(private http: HttpClient, private reservationService : ReservationService, private router : Router, private toaster : ToastrService) {
  }

  async ngOnInit() {

        this.ticket = this.reservationService.getTicket();
    if(!this.ticket){
      this.toaster.error("No ticket found. Please book a ticket first", 'Error');
      return;
    }
    this.stripe = await loadStripe('pk_test_51RRV1IIwE06BdyPTr0G4rszUfl94RMlNdo1M0wP7TaiSnfY2byzVHHTLDN9dFwaSEgc2gqSGHyr7cLW8GL8TWN0d00GX1BrHh8');
    const elements = this.stripe!.elements();
    this.card = elements.create('card');
    this.card.mount('#card-element');

    
  }

  async handlePayment() {

    if(!this.stripe || !this.card || !this.ticket){
      this.toaster.error("Payment processing setup not complete...", 'Error');
      return;
    }

    const t = localStorage.getItem('token');
    const headers = {
      'Authorization': `Bearer ${t}`,
      'Content-Type':'application/json'
    };
    const { token, error } = await this.stripe!.createToken(this.card!);
    if (error) {
      alert(error.message);
    }
      if(!token || !token.id){
        this.toaster.error("Token generation failed...", 'Error');
        return;
      }
      
      this.ticket.token = token.id;
      console.log(this.ticket);
      
      // this.http.post('http://localhost:8005/api/payments/charge', paymentData, {headers})
      this.reservationService.makePayment(this.ticket)
        .subscribe({
          next: (res: any) =>{
            this.ticket = res as Ticket;
            this.reservationService.setTicket(this.ticket);
            this.toaster.success('Payment successful!','Success'),
            this.router.navigate(['/user-home/show-ticket']); 
          } ,
          error: () => alert('Payment failed.')
        });
    
  }
}


