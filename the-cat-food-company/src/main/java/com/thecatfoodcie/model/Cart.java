package com.thecatfoodcie.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart implements Serializable {

    @Builder.Default
    private Map<String,CartItem> cartItems =new HashMap<>();

    public void addRemoveProduct(String productCode,int quantity) {
        checkItems();
        CartItem item = cartItems.get(productCode);
        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
            if (item.getQuantity() <= 0) {
                cartItems.remove(productCode);
            }
        } else if (quantity > 0) {
            cartItems.put(productCode, CartItem.builder().code(productCode).quantity(quantity).build());
        }

    }

    private void checkItems() {
        if ( cartItems ==null)
            cartItems =new HashMap<>();
    }

    public List<CartItem> listItems() {
        checkItems();
        return new ArrayList<>(cartItems.values());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CartItem implements Serializable {
        private String code;
        private int quantity;
    }

}
