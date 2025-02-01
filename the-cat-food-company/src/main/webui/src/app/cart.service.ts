import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CartItemWithDescription, CartActionRequest } from './models/cart.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private apiUrl = '/api/cart';

  constructor(private http: HttpClient) { }

  getCart(email: string): Observable<CartItemWithDescription[]> {
    return this.http.get<CartItemWithDescription[]>(this.apiUrl, { params: { email } });
  }

  addOrRemoveFromCart(request: CartActionRequest): Observable<void> {
    return this.http.post<void>(this.apiUrl, request);
  }
}
