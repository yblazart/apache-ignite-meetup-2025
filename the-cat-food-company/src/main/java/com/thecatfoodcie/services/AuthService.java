package com.thecatfoodcie.services;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import com.thecatfoodcie.model.Customer;

import java.time.Duration;
import java.util.HashSet;
import java.util.Optional;

@ApplicationScoped
public class AuthService {

    private static final Logger logger = Logger.getLogger(AuthService.class);

    @Transactional
    public String login(String email, String password) {
        logger.infof("Tentative de connexion pour l'utilisateur: %s", email);
        Optional<Customer> existingCustomer = Customer.find("email", email).firstResultOptional();

        Customer customer;
        if (existingCustomer.isEmpty()) {
            logger.infof("Création automatique d'un nouveau client avec l'email: %s", email);
            customer = new Customer(email, password);
            customer.persist();
        } else {
            customer = existingCustomer.get();
            if (!customer.getPassword().equals(password)) {
                logger.warnf("Échec de connexion - mot de passe invalide pour l'utilisateur: %s", email);
                throw new SecurityException("Invalid password");
            }
            logger.infof("Connexion réussie pour l'utilisateur: %s", email);
        }

        return generateToken(customer);
    }

    private String generateToken(Customer customer) {
        logger.debugf("Génération du token JWT pour l'utilisateur: %s", customer.getEmail());
        return Jwt.issuer("https://the-cat-food-company.com")
                .subject(customer.getEmail())
                .groups(new HashSet<>()) // Add roles if needed
                .expiresIn(Duration.ofHours(24))
                .sign();
    }
}