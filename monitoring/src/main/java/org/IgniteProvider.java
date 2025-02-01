package org;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class IgniteProvider {


    @Produces
    public IgniteConfiguration createIgniteConfiguration() {
        IgniteConfiguration config = new IgniteConfiguration();
        config.setClientMode(true);
        return config;
    }

    @Produces
    public Ignite createIgnite(IgniteConfiguration config) {
        return Ignition.start(config);
    }



}
