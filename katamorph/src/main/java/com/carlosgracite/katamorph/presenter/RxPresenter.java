package com.carlosgracite.katamorph.presenter;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.carlosgracite.katamorph.cache.RequestGroup;
import com.carlosgracite.katamorph.cache.RxCache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;

public class RxPresenter<View> extends Presenter<View> {

    private CompositeDisposable disposables;
    private CompositeDisposable requestDisposables;

    protected final RxCache rxCache;

    public Set<String> requestIds = new HashSet<>();
    public Map<String, RequestHandler> requestHandlerMap = new HashMap<>();

    private RequestGroup requestGroup;

    public RxPresenter() {
        rxCache = RxCache.getInstance();
        disposables = new CompositeDisposable();
        requestDisposables = new CompositeDisposable();
    }

    public Set<String> getRequestIds() {
        return requestIds;
    }

    public void onCreate(boolean recreated, boolean firstCreated) {

    }

    public void bindRequests() {

    }

    public void onRestoreRequests() {
        for (String id: requestIds) {
            onRestoreRequest(id);
        }
    }

    protected <T> void register(String key, RequestHandler<T> requestHandler) {
        requestHandlerMap.put(key, requestHandler);
    }

    protected <T> void register(String key, @NonNull final Consumer onStartLoad, @NonNull final Consumer<T> onSuccess) {
        register(key, new SimpleRequestHandler<T>() {
            @Override
            public void onStartLoad(boolean isResubscribing) {
                try {
                    onStartLoad.accept(isResubscribing);
                } catch (Exception e) {
                    e.printStackTrace();
                    Exceptions.throwIfFatal(e);
                }
            }

            @Override
            public void onSuccess(T result) throws Exception {
                onSuccess.accept(result);
            }
        });
    }

    protected <T> void register(String key, @NonNull final Consumer<Boolean> onStartLoad,
                                @NonNull final Consumer<T> onSuccess, @NonNull final Consumer<Throwable> onError) {
        register(key, new SimpleRequestHandler<T>() {
            @Override
            public void onStartLoad(boolean isResubscribing) {
                try {
                    onStartLoad.accept(isResubscribing);
                } catch (Exception e) {
                    e.printStackTrace();
                    Exceptions.throwIfFatal(e);
                }
            }

            @Override
            public void onSuccess(T result) throws Exception {
                onSuccess.accept(result);
            }

            @Override
            public void onError(Throwable throwable) throws Exception {
                onError.accept(throwable);
            }
        });
    }

    protected void onRestoreRequest(@NonNull String key) {
        Observable observable = requestGroup.get(key);

        if (observable != null) {
            subscribeToRequest(key, observable, true);
        }
    }

    protected <T> void doRequest(final String key, Observable<T> observable) {
        requestIds.add(key);
        Observable<T> cachedObservable = requestGroup.get(key, observable);
        subscribeToRequest(key, cachedObservable, false);
    }

    public void onRestoreState(@NonNull Bundle state) {

    }

    public void onSaveState(@NonNull Bundle bundle) {

    }

    public void disposeRequests() {
        requestDisposables.clear();
    }

    @CallSuper
    @Override
    public void onDestroy(boolean isDestroyedBySystem) {
        super.onDestroy(isDestroyedBySystem);
        disposables.clear();
        disposeRequests();

        if (!isDestroyedBySystem) {
            rxCache.remove(requestGroup.getId());
            requestIds.clear();
        }
    }

    private  <T> void subscribeToRequest(final String key, Observable<T> observable, boolean resubscribing) {
        final RequestHandler requestHandler = requestHandlerMap.get(key);
        requestHandler.onStartLoad(resubscribing);
        addInternal(observable.subscribe(new Consumer<T>() {
            @Override
            public void accept(T o) throws Exception {
                requestGroup.remove(key);
                requestIds.remove(key);
                requestHandler.onSuccess(o);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                requestGroup.remove(key);
                requestIds.remove(key);
                requestHandler.onError(throwable);
            }
        }));
    }

    protected void add(@NonNull Disposable disposable) {
        disposables.add(disposable);
    }

    private void addInternal(@NonNull Disposable disposable) {
        requestDisposables.add(disposable);
    }

    public boolean hasPendingRequests() {
        return !requestIds.isEmpty();
    }

    public boolean isPendingRequest(String key) {
        return requestIds.contains(key);
    }

    public boolean setupRequestIds(Set<String> requestIds, Long requestGroupId) {
        this.requestIds = requestIds;

        boolean requestGroupFirstCreated = false;

        if (requestGroupId != null) {
            requestGroup = rxCache.getGroup(requestGroupId);
        }

        if (requestGroup == null) {
            requestGroupFirstCreated = true;
            requestGroup = rxCache.newGroup();
        }

        return requestGroupFirstCreated;
    }

    public RequestGroup getRequestGroup() {
        return requestGroup;
    }

    protected static class SimpleRequestHandler<T> implements RequestHandler<T> {

        @Override
        public void onStartLoad(boolean isResubscribing) {

        }

        @Override
        public void onSuccess(T result) throws Exception {

        }

        @Override
        public void onError(Throwable throwable) throws Exception {

        }
    }

    protected interface RequestHandler<T> {
        void onStartLoad(boolean isResubscribing);
        void onSuccess(T result) throws Exception;
        void onError(Throwable throwable) throws Exception;
    }
}