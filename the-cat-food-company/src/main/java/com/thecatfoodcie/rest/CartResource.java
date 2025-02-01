package com.thecatfoodcie.rest;

import com.thecatfoodcie.model.Cart;
import com.thecatfoodcie.model.Cart.CartItem;
import com.thecatfoodcie.model.CatFood;
import com.thecatfoodcie.services.CartService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

import java.util.List;

@Path("/api/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@JBossLog
public class CartResource {

    @Inject
    CartService cartService;

    @POST
    public void addToOrRemoveFromCart(CartActionRequest request) {
        log.info("Adding or removing product from cart: " + request);
        cartService.addOrRemoveProductToCart(request.getEmail(), request.getProductCode(), request.getQuantity());
        // log current content of cart
        log.info("Current cart content: " + cartService.getCartItems(request.getEmail()));
    }

    @GET
    public List<CartItemWithDescription> getCart(@QueryParam("email") String email) {
        List<Cart.CartItem> cartItems = cartService.getCartItems(email);
        log.info("Getting cart for email: '" + email+"' : " + cartItems);
        return cartItems.stream()
                .map(item -> buildObject(item))
                .toList();
    }

    private CartItemWithDescription buildObject(CartItem item) {
        return CartItemWithDescription.builder()
                .catFood(CatFood.find("code=?1",item.getCode()).firstResult())
                .quantity(item.getQuantity())
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CartItemWithDescription {
        private CatFood catFood;
        private int quantity;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CartActionRequest {
        private String email;
        private String productCode;
        private int quantity;
    }
}