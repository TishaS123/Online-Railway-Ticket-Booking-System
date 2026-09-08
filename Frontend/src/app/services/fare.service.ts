import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Fare } from '../fare.model';

@Injectable({
  providedIn: 'root'
})
export class FareService {

   private token = localStorage.getItem('token');
  constructor(private http : HttpClient) { }

  addFareDetails(fare: any): Observable<any> {
    const headers = new HttpHeaders({
          'Authorization': `Bearer ${this.token}`,
          'Content-Type': 'application/json'
        })
    return this.http.post('http://localhost:8002/fare/addFareDetails', fare, {headers, responseType:'text'});
  }

  getAllFares(): Observable<Fare[]>{
    const headers = {
      'Authorization' : `Bearer ${this.token}`,
      'Content-Type': 'application/json'
    };
    return this.http.get<Fare[]>('http://localhost:8002/fare', {headers});
  }

  getFareById(id : number): Observable<Fare>{
    const headers = {
      'Authorization' : `Bearer ${this.token}`,
      'Content-Type':'application/json'
    };
    return this.http.get<Fare>('http://localhost:8002/fare/'+id, {headers});
  }

  getFareByRouteAndClassType(source: string, destination: string, classType: string): Observable<number> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    const params = new HttpParams()
      .set('source', source)
      .set('destination', destination)
      .set('classType', classType);

    return this.http.get<number>('http://localhost:8002/fare/getFareByRouteAndClassType', { headers, params });
  }
}
