package com.carlosgracite.katamorph.cache;

import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.AsyncSubject;

public enum RxCache {

    INSTANCE;

    private final ConcurrentHashMap<String, AsyncSubject> cache = new ConcurrentHashMap<>();

    public <T> Observable<T> get(String key) {
        return cache.get(key);
    }

    public <T> Observable<T> get(String key, Observable<T> observable) {
        AsyncSubject<T> o = cache.get(key);
        if (o != null) {
            return o;
        }

        o = AsyncSubject.create();

        AsyncSubject<T> p = cache.putIfAbsent(key, o);
        if (p != null) {
            return p;
        }

        observable.subscribe(o);

        return o;
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public static RxCache getInstance() {
        return RxCache.INSTANCE;
    }
}
