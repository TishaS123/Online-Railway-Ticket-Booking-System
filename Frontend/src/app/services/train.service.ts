import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Train } from '../train.model';
import { BehaviorSubject, Observable } from 'rxjs';
import { TrainResponseDTO } from '../train-response-dto.model';

@Injectable({
  providedIn: 'root'
})
export class TrainService {
  

  private token = localStorage.getItem('token');
  constructor(private http : HttpClient) { 
    
  }

  private baseUrl = 'http://localhost:8002/train';

  addTrainDetails(train: Train): Observable<any>{
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`,
      'Content-Type': 'application/json'
    })
    return this.http.post('http://localhost:8002/train/addTrainDetails', train, {headers, responseType:'text'});
  }

  getAllTrains(): Observable<Train[]> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.token}`,
      'Content-Type': 'application/json'
    });
    return this.http.get<Train[]>('http://localhost:8002/train', { headers });
  }


  getTrainResponseDto(source: string, destination: string, classType: string, date: string): Observable<TrainResponseDTO[] | TrainResponseDTO> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    const params = new HttpParams()
    .set('source', source)
    .set('destination', destination)
    .set('classType', classType)
    .set('date', date);
    return this.http.get<TrainResponseDTO[] | TrainResponseDTO>(`${this.baseUrl}/search`, { headers, params });
  }


  
private searchParamsSource = new BehaviorSubject<any>(null);
  searchParams$ = this.searchParamsSource.asObservable();

  updateSearchParams(params: any) {
    this.searchParamsSource.next(params);
  }

  getTrainByTrainId(trainId : number): Observable<Train>{
    const token = localStorage.getItem('token');

    const headers = {
      'Authorization': `Bearer ${this.token}`,
      'Content-Type':'application/json'
    };
    return this.http.get<Train>('http://localhost:8002/train/'+trainId, {headers});
  }

}
