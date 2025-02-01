package com.thecatfoodcie.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;

import com.thecatfoodcie.dto.LoginRequest;
import com.thecatfoodcie.services.AuthService;

import io.quarkus.runtime.StartupEvent;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@PermitAll  // Permettre l'accès à toutes les méthodes par défaut
public class AuthResource {

    private static final Logger LOG = Logger.getLogger(AuthResource.class);

    @Inject
    AuthService authService;

    public void onBoot(@Observes StartupEvent ev) {
        LOG.info("AuthResource.onBoot() - Service démarré");
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest loginRequest) {
        LOG.infof("Tentative de login pour: %s", loginRequest.getEmail());
        try {
            String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            LOG.infof("Login réussi pour: %s", loginRequest.getEmail());
            return Response.ok(new HashMap<String, String>() {{
                put("token", token);
            }}).build();
        } catch (Exception e) {
            LOG.errorf("Échec de login pour %s: %s", loginRequest.getEmail(), e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
}