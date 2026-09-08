import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PaymentRequest } from '../payment-request.model';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private http: HttpClient) { }

  private token = localStorage.getItem('token');
  makePayment(payment: PaymentRequest): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization':'Bearer ${this.token}',
      'Content-Type':'application/json'

    });

    return this.http.post<any>('http://localhost:8005/api/payments/charge', payment, {headers});
  }
}
