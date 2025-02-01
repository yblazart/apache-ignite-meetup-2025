package com.thecatfoodcie.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;

import com.thecatfoodcie.model.Order;

@Path("/api/orders")
public class OrderResource {

    @GET
    public List<Order> list() {
        return Order.listAll();
    }

    @GET
    @Path("/{id}")
    public Order get(@PathParam("id") Long id) {
        return Order.findById(id);
    }

    @POST
    public Response create(Order order) {
        order.persist();
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @PUT
    @Path("/{id}")
    public Order update(@PathParam("id") Long id, Order order) {
        Order entity = Order.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setCustomer(order.getCustomer());
        entity.setOrderItems(order.getOrderItems());
        entity.persist();
        return entity;
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Order entity = Order.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}