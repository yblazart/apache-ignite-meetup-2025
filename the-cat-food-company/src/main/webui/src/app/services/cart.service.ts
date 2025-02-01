import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

interface CartActionRequest {
  email: string;
  productCode: string;
  quantity: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private apiUrl = '/api/cart';
  private cartUpdateSubject = new BehaviorSubject<boolean>(true);
  cartUpdate$ = this.cartUpdateSubject.asObservable();

  constructor(private http: HttpClient) {}

  addToCart(request: CartActionRequest): Observable<void> {
    return this.http.post<void>(this.apiUrl, request).pipe(
      tap(() => {
        this.notifyCartUpdate();
      })
    );
  }

  getCart(email: string): Observable<any> {
    const params = new HttpParams().set('email', email);
    return this.http.get(`${this.apiUrl}`, { params });
  }

  private notifyCartUpdate(): void {
    this.cartUpdateSubject.next(true);
  }
}
