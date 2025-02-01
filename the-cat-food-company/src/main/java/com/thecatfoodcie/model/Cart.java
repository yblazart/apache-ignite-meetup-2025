package com.thecatfoodcie.model;

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
public class Cart {

    @Builder.Default
    private Map<String,CartItem> items=new HashMap<>();


    public void addRemoveProduct(String productCode,int quantity) {
        CartItem item = items.get(productCode);
        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
            if (item.getQuantity() <= 0) {
                items.remove(productCode);
            }
        } else if (quantity > 0) {
            items.put(productCode, CartItem.builder().code(productCode).quantity(quantity).build());
        }

    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CartItem {
        private String code;
        private int quantity;
    }

}
