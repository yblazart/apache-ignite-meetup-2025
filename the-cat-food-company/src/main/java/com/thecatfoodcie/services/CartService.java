package com.thecatfoodcie.services;

import com.thecatfoodcie.model.Cart;
import com.thecatfoodcie.model.Cart.CartItem;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.jbosslog.JBossLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@JBossLog
public class CartService {

    private final Map<String, Cart> carts = new HashMap<>();

    public void addOrRemoveProductToCart(String email, String code,int quantity) {
        log.info("Adding or removing product to cart: " + email + " " + code + " = " + quantity);
        Cart cart = getCart(email);
        cart.addRemoveProduct(code, quantity);
    }

    private Cart getCart(String email) {
        return carts.computeIfAbsent(email, k -> new Cart());
    }

    public List<CartItem> getCartItems(String email) {
        return getCart(email).listItems();
    }
}