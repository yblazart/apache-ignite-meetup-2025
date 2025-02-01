import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCardModule } from '@angular/material/card';
import { CatFoodService } from '../services/cat-food.service';
import { CatFood } from '../models/cat-food.model';
import { CartService } from '../services/cart.service';
import { CartComponent } from '../cart/cart.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatSidenavModule,
    MatGridListModule,
    MatCardModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
    CartComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  catFoodItems: CatFood[] = [];
  breakpoint: number = 3;
  userEmail: string | null = null;

  constructor(
    private router: Router,
    private authService: AuthService,
    private catFoodService: CatFoodService,
    private cartService: CartService
  ) {}

  addToCart(item: CatFood): void {
    if (this.userEmail) {
      const request = {
        email: this.userEmail,
        productCode: item.code,
        quantity: 1
      };
      this.cartService.addToCart(request).subscribe(() => {
        // Optionally show a success message
        console.log('Item added to cart');
      });
    }
  }

  ngOnInit(): void {
    this.catFoodService.getCatFoodItems().subscribe((data: CatFood[]) => {
      this.catFoodItems = data;
    });
    this.breakpoint = (window.innerWidth <= 800) ? 1 : 3;
    this.userEmail = this.authService.getLoggedInUserEmail();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  onResize(event: any) {
    this.breakpoint = (event.target.innerWidth <= 800) ? 1 : 3;
  }
}