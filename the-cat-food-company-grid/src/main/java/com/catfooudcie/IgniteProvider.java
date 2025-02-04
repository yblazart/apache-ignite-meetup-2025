package com.catfooudcie;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

@ApplicationScoped
public class IgniteProvider {

    @Produces
    @ApplicationScoped
    IgniteConfiguration configuration() {
        return new IgniteConfiguration()
                .setClientMode(false)
                .setClassLoader(Thread.currentThread().getContextClassLoader())
                ;
    }

    @Produces
    @ApplicationScoped
    Ignite produceIgnite(IgniteConfiguration configuration) {
        return Ignition.start(configuration);
    }

    public void closeIgnite(@Disposes Ignite ignite) {
        ignite.close();
    }
}