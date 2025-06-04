import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Stats } from '../models/stats';
import { environment } from '../../environments/environment.development';
import { ReadingSession } from '../models/reading-session';


@Injectable({
  providedIn: 'root'
})
export class StatsService {
private apiUrl = `${environment.apiUrl}/api/stats`;

  constructor(private http: HttpClient) { }

  getStats(): Observable<Stats> {
    return this.http.get<Stats>(this.apiUrl);
  }

  getRecentSessions(limit: number = 10): Observable<ReadingSession[]> {
    return this.http.get<ReadingSession[]>(`${this.apiUrl}/sessions?limit=${limit}`);
  }
}
