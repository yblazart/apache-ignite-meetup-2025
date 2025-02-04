package com.thecatfoodcie.services;

import com.thecatfoodcie.model.CatFood;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

@ApplicationScoped
public class IgniteProvider {
    @Produces
    @ApplicationScoped
    IgniteConfiguration configuration() {
        CacheConfiguration<String, CatFood> catFoodCacheConfiguration = new CacheConfiguration<String, CatFood>("catFoodCache")
                .setCacheMode(CacheMode.PARTITIONED)
                .setAtomicityMode(CacheAtomicityMode.ATOMIC)
                .setCacheStoreFactory(new CDICacheStoreFactory<>(CatFoodCacheStore.class))
                .setReadThrough(true)
                .setWriteThrough(true);

        return new IgniteConfiguration()
                .setClientMode(false)
                .setClassLoader(Thread.currentThread().getContextClassLoader())
                .setPeerClassLoadingEnabled(true)
                .setCacheConfiguration(catFoodCacheConfiguration)
                ;
    }

    @Produces
    @ApplicationScoped
    Ignite produceIgnite(IgniteConfiguration configuration) {
        Ignite ignite = Ignition.start(configuration);
        ignite.cache("catFoodCache").loadCache(null);
        return ignite;
    }

    public void closeIgnite(@Disposes Ignite ignite) {
        ignite.close();
    }
}