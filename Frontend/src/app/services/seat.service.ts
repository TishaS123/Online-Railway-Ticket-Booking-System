import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Seat } from '../seat.model';
import { AddSeat } from '../add-seat.model';

@Injectable({
  providedIn: 'root'
})
export class SeatService {

  private token = localStorage.getItem('token');
  constructor(private http: HttpClient) { }

  // addSeatDetails(seat : Seat, coachId : number): Observable<any>{
  //   const headers = new HttpHeaders({
  //     'Authorization': `Bearer ${this.token}`,
  //     'Content-Type': 'application/json'
  //   });
  //   return this.http.post('http://localhost:8002/seat/addSeat/'+coachId, seat, { headers});
  // }

  addSeatDetails(seat: AddSeat): Observable<any> {

  const token = localStorage.getItem('token');

  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  });

  return this.http.post(
    `http://localhost:8002/seat/addSeat/${seat.coachId}`,
    seat,
    {
      headers,
      responseType: 'text'
    }
  );
}

  getAllSeats(): Observable<Seat[]>{
    const token = localStorage.getItem('token');
    const headers = {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    };
    return this.http.get<Seat[]>('http://localhost:8002/seat/getAllSeats', { headers });
  }

  getSeatById(id:number):Observable<Seat>{
    const token = localStorage.getItem('token');
    const headers= {
      'Authorization': `Bearer ${token}`,
      'Content-Type':'application/json'
    };
    return this.http.get<Seat>('http://localhost:8002/seat/get-seat-by-id/' + id, { headers });
  }
}
