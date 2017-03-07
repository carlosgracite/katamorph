package com.carlosgracite.katamorph.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public enum RxCache {

    INSTANCE;

    private final ConcurrentHashMap<Long, RequestGroup> cache = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public RequestGroup getGroup(Long id) {
        return cache.get(id);
    }

    public RequestGroup newGroup() {
        long id = nextId.getAndIncrement();
        RequestGroup observableGroup = new RequestGroup(id);
        cache.put(id, observableGroup);
        return observableGroup;
    }

    public void remove(Long key) {
        cache.remove(key);
    }

    public static RxCache getInstance() {
        return RxCache.INSTANCE;
    }

    public void destroy(RequestGroup group) {
        // group.destroy(); TODO?
        cache.remove(group.getId());
    }
}
