package com.thecatfoodcie.rest;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import com.thecatfoodcie.model.CatFood;

@Path("/api/cat-foods")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CatFoodResource {

    @GET
    public List<CatFood> list() {
        return CatFood.listAll();
    }

    @GET
    @Path("/{id}")
    public CatFood get(@PathParam("id") Long id) {
        return CatFood.findById(id);
    }

    @POST
    @Transactional
    public Response create(CatFood catFood) {
        if (catFood.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 400);
        }
        catFood.persist();
        return Response.created(null).entity(catFood).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public CatFood update(@PathParam("id") Long id, CatFood catFood) {
        CatFood entity = CatFood.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Cat Food with id " + id + " not found.", 404);
        }
        entity.setCode(catFood.getCode());
        entity.setName(catFood.getName());
        entity.setWeight(catFood.getWeight());
        entity.setPrice(catFood.getPrice());
        entity.setImagePath(catFood.getImagePath());
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        CatFood entity = CatFood.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Cat Food with id " + id + " not found.", 404);
        }
        entity.delete();
        return Response.noContent().build();
    }
}