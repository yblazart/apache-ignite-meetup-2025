package com.thecatfoodcie.services;

import jakarta.enterprise.inject.spi.CDI;
import lombok.RequiredArgsConstructor;

import javax.cache.configuration.Factory;
import java.io.Serializable;

@RequiredArgsConstructor
public class CDICacheStoreFactory<T> implements Factory<T>, Serializable {

    private final Class<T> storeClass;

    @Override
    public T create() {
        return CDI.current().select(storeClass).get();
    }
}
