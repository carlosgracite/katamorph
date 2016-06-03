package com.carlosgracite.katamorph.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.carlosgracite.katamorph.cache.RxCache;

import java.util.HashSet;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class RxPresenter<View> extends Presenter<View> {

    private CompositeSubscription subscriptions;

    protected final RxCache rxCache;

    public HashSet<String> requestIds = new HashSet<>();

    public RxPresenter(@NonNull View view) {
        super(view);
        rxCache = RxCache.getInstance();
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void onViewCreated(@Nullable Bundle state) {
        super.onViewCreated(state);
        if (state == null) {
            requestIds = new HashSet<>();
        } else {
            requestIds = (HashSet<String>)state.getSerializable("requests_state");
        }
    }

    @Override
    public void onSave(@NonNull Bundle bundle) {
        super.onSave(bundle);
        bundle.putSerializable("requests_state", requestIds);
    }

    public void onRestoreRequests() {
        for (String id: requestIds) {
            onRestoreRequest(id);
        }
    }

    protected void onRestoreRequest(@NonNull String id) {

    }

    @Override
    public void onDestroy(boolean isDestroyedBySystem) {
        super.onDestroy(isDestroyedBySystem);
        subscriptions.clear();

        if (!isDestroyedBySystem) {
            for (String key: requestIds) {
                rxCache.remove(key);
            }
            requestIds.clear();
        }
    }

    protected <T> void load(@NonNull Observable<T> observable,
                            @NonNull final Action1<T> onNext) {
        add(observable.subscribe(onNext));
    }

    protected <T> void load(@NonNull Observable<T> observable,
                            @NonNull final Subscriber<T> subscriber) {
        add(observable.subscribe(subscriber));
    }

    protected <T> void load(@NonNull final String key,
                            @NonNull Observable<T> observable,
                            @NonNull final Subscriber<T> subscriber) {
        Subscription subscription = rxCache.get(key, observable.doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                rxCache.remove(key);
                requestIds.remove(key);
            }
        })).subscribe(subscriber);

        requestIds.add(key);

        add(subscription);
    }

    protected <T> void load(@NonNull final String key,
                            @NonNull Observable<T> observable,
                            @NonNull final Action1<T> onNext) {
        Subscription subscription = rxCache.get(key, observable.doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                rxCache.remove(key);
                requestIds.remove(key);
            }
        })).subscribe(onNext);

        requestIds.add(key);

        add(subscription);
    }

    protected void add(@NonNull Subscription subscription) {
        subscriptions.add(subscription);
    }

    public boolean hasPendingRequests() {
        return !requestIds.isEmpty();
    }
}
