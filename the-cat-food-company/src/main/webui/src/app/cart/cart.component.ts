import { Component, OnInit, OnDestroy } from '@angular/core';
import { CartService } from '../services/cart.service';
import { CartItemWithDescription } from '../models/cart.model';
import { AuthService } from '../auth/auth.service';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
  standalone: true,
  imports: [NgIf, NgFor, MatIconModule, CommonModule]
})
export class CartComponent implements OnInit, OnDestroy {
  cartItems: CartItemWithDescription[] = [];
  userEmail: string | null = null;
  private cartUpdateSubscription: Subscription;

  constructor(private cartService: CartService, private authService: AuthService) {
    this.cartUpdateSubscription = this.cartService.cartUpdate$.subscribe(() => {
      this.loadCartItems();
    });
  }

  ngOnInit(): void {
    this.userEmail = this.authService.getLoggedInUserEmail();
    if (this.userEmail) {
      this.loadCartItems();
    }
  }

  ngOnDestroy(): void {
    if (this.cartUpdateSubscription) {
      this.cartUpdateSubscription.unsubscribe();
    }
  }

  loadCartItems(): void {
    if (this.userEmail) {
      this.cartService.getCart(this.userEmail).subscribe(items => {
        this.cartItems = items;
      });
    }
  }

  addToCart(item: CartItemWithDescription): void {
    if (this.userEmail) {
      const request = {
        email: this.userEmail,
        productCode: item.catFood.code,
        quantity: 1
      };
      this.cartService.addToCart(request).subscribe();
    }
  }

  removeFromCart(item: CartItemWithDescription): void {
    if (this.userEmail) {
      const request = {
        email: this.userEmail,
        productCode: item.catFood.code,
        quantity: -1
      };
      this.cartService.addToCart(request).subscribe();
    }
  }

  getTotal(): number {
    return this.cartItems.reduce((total, item) => total + (item.catFood.price * item.quantity), 0);
  }
}
