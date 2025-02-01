import { CatFood } from './cat-food.model';

export interface CartItemWithDescription {
  catFood: CatFood;
  quantity: number;
}

export interface CartActionRequest {
  email: string;
  productCode: string;
  quantity: number;
}