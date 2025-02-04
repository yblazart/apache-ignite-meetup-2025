package com.thecatfoodcie.services;

import com.thecatfoodcie.model.CatFood;
import io.quarkus.arc.Unremovable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.io.Serializable;

@Unremovable
@ApplicationScoped
@JBossLog
public class CatFoodCacheStore extends CacheStoreAdapter<String, CatFood> implements Serializable {

    @Override
    public void loadCache(IgniteBiInClosure<String, CatFood> clo, Object... args) {
        CatFood.listAll().stream()
                .map(o->(CatFood)o)
                .forEach(catFood -> {
                    clo.apply(catFood.getCode(), catFood);
                    log.info("Pre - Loading CatFood with key: " + catFood.getCode());
                });
    }

    @Override
    @Transactional
    public CatFood load(String key) throws CacheLoaderException {
        log.info("Loading CatFood with key: " + key);
        return (CatFood) CatFood.find("code=?1", key).firstResultOptional().orElse(null);
    }

    @Override
    public void write(Cache.Entry<? extends String, ? extends CatFood> entry) throws CacheWriterException {
        throw new UnsupportedOperationException("Write operation is not supported");
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        throw new UnsupportedOperationException("Write operation is not supported");
    }
}
