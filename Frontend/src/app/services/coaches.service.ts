import { Injectable } from '@angular/core';
import { Train } from '../train.model';
import { TrainService } from './train.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Coaches } from '../coaches.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CoachesService {

  private token = localStorage.getItem('token');
  constructor(private http: HttpClient) { }

  addCoachesDetails(coach: Coaches, trainId: number): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post('http://localhost:8002/coach/' + trainId, coach, { headers, responseType: 'text' });
  }


  getAllCoaches(): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`,
      'Content-Type': 'application/json'
    });
    return this.http.get<any>('http://localhost:8002/coach', { headers });
  }

  getCoachById(coachId: number): Observable<Coaches>{
    const headers = {
      'Authorization': `Bearer ${this.token}`,
      'Content-Type':'application/json'
    };
    return this.http.get<Coaches>('http://localhost:8002/coach/'+coachId, {headers});
  }

}
