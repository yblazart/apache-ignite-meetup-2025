package com.thecatfoodcie.services;

import com.thecatfoodcie.model.Cart;
import com.thecatfoodcie.model.Cart.CartItem;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.jbosslog.JBossLog;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

import java.util.List;

@ApplicationScoped
@JBossLog
public class CartService {
    @Inject
    Ignite ignite;

    private IgniteCache<String, Cart> igniteCarts;

    @PostConstruct
    @SneakyThrows
    public void init() {
        igniteCarts = ignite.getOrCreateCache("carts");
    }

    @SneakyThrows
    public void addOrRemoveProductToCart(String email, String code, int quantity) {
        Cart cart = getCart(email);
        cart.addRemoveProduct(code, quantity);
        igniteCarts.put(email, cart);
    }

    @SneakyThrows
    private Cart getCart(String email) {
        if ( !igniteCarts.containsKey(email) )
            igniteCarts.put(email, new Cart());
        return igniteCarts.get(email);
    }

    @SneakyThrows
    public List<CartItem> getCartItems(String email) {
        return getCart(email).listItems();
    }
}