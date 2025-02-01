package com.thecatfoodcie.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;

import com.thecatfoodcie.model.Customer;

@Path("/api/customers")
public class CustomerResource {

    @GET
    public List<Customer> list() {
        return Customer.listAll();
    }

    @GET
    @Path("/{id}")
    public Customer get(@PathParam("id") Long id) {
        return Customer.findById(id);
    }

    @POST
    public Response create(Customer customer) {
        customer.persist();
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @PUT
    @Path("/{id}")
    public Customer update(@PathParam("id") Long id, Customer customer) {
        Customer entity = Customer.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.setEmail(customer.getEmail());
        entity.setPassword(customer.getPassword());
        entity.persist();
        return entity;
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Customer entity = Customer.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}