package com.carlosgracite.katamorph.cache;

import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.AsyncSubject;

public class RequestGroup {

    private Long id;
    private final ConcurrentHashMap<String, AsyncSubject> cache = new ConcurrentHashMap<>();

    public RequestGroup(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

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

}
