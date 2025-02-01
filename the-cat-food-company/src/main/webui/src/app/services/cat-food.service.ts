import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CatFood } from '../models/cat-food.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CatFoodService {
  private apiUrl = `${environment.apiUrl}/api/cat-foods`;

  constructor(private http: HttpClient) { }

  getCatFoodItems(): Observable<CatFood[]> {
    return this.http.get<CatFood[]>(`${this.apiUrl}`);
  }
}
