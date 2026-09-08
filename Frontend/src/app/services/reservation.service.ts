import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Reservation } from '../reservation.model';
import { TicketRequest } from '../ticket-request.model';
import { Passenger } from '../passenger.model';
import { Ticket } from '../ticket.model';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private ticketRequest: TicketRequest | null = null;
  private ticket!: Ticket;

  setTicketRequest(request: TicketRequest): void {
    this.ticketRequest = request;
  }

  getTicketRequest(): TicketRequest | null {
    return this.ticketRequest;
  }


setTicket(ticket: Ticket): void {
    this.ticket = ticket;
  }

  getTicket(): Ticket {
    return this.ticket;
  }

  baseUrl = 'http://localhost:8003/booking';

  private token = localStorage.getItem('token');
  constructor(private http : HttpClient) { }

  // getAllReservations(): Observable<Reservation[]>{
  //   const headers = {
  //     'Authorization': `Bearer ${this.token}`,
  //     'Content-Type': 'application/json'
  //   };
  //   return this.http.get<Reservation[]>('http://localhost:8003/booking/showAllReservations', {headers});
  // }


  
getAllReservations(): Observable<Reservation[]> {
  const token = localStorage.getItem('token');
  const headers = {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
  return this.http.get<Reservation[]>('http://localhost:8003/booking/showAllReservations', { headers });
}



bookTicket(request: TicketRequest): Observable<Ticket> {
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${localStorage.getItem('token')}`,
    'Content-Type': 'application/json'
  });

  this.ticketRequest = request;
  return this.http.post<Ticket>('http://localhost:8003/booking/book', request, { headers });
}


  makePayment(ticket: Ticket): Observable<Ticket>{
    const token = localStorage.getItem('token');
    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    };

    return this.http.post<Ticket>('http://localhost:8003/booking/make-payment', ticket, {headers});
  }

  getTicketById(ticketNo: number): Observable<Ticket> {
    const token = localStorage.getItem('token');
    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    };
    return this.http.get<Ticket>(`${this.baseUrl}/ticket/${ticketNo}`, { headers });
  }

  getAllPassengers(): Observable<Passenger[]>{
    const token = localStorage.getItem('token');
    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    };
    return this.http.get<Passenger[]>('http://localhost:8003/booking/get-passengers', {headers});
  }

  getMyReservations(username:string):Observable<Ticket[]>{
    const token = localStorage.getItem('token');
    const headers = {
      'Authorization':`Bearer ${token}`,
      'Content-Type': 'application/json'
    };
    return this.http.get<Ticket[]>('http://localhost:8003/booking/my-reservations/'+username, {headers});
  }

  cancelTicket(pnrNo: string) {
  const token = localStorage.getItem('token'); // assuming JWT is stored here

  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  });

  const params = new HttpParams().set('pnrNo', pnrNo);

  return this.http.put(`${this.baseUrl}/cancelTicket`, null, {
    headers,
    params,
    responseType: 'text'
  });
}



}
