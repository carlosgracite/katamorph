package com.carlosgracite.katamorph.sample.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.carlosgracite.katamorph.presenter.RxPresenter;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TestPresenter extends RxPresenter<TestView> {

    private static final String REQUEST_LOAD1 = "load1";

    private int value;

    public TestPresenter(@NonNull TestView testView) {
        super(testView);
    }

    @Override
    public void onViewCreated(@Nullable Bundle state) {
        super.onViewCreated(state);

        if (state == null) {
            startLoading();

        } else {
            value = state.getInt("value", 0);

            if (!requestIds.contains(REQUEST_LOAD1)) {
                getView().setValue(value);
            }
        }
    }

    @Override
    protected void onRestoreRequest(@NonNull String id) {
        super.onRestoreRequest(id);
        switch (id) {
            case REQUEST_LOAD1:
                startLoading();
                break;
        }
    }

    @Override
    public void onSave(@NonNull Bundle bundle) {
        super.onSave(bundle);
        bundle.putInt("value", value);
    }

    public void startLoading() {

        getView().loadStarted();

        Observable<Integer> observable =
                Observable.just(value+1)
                .delay(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        load(REQUEST_LOAD1, observable, new Action1<Integer>() {
            @Override
            public void call(Integer value) {
                TestPresenter.this.value = value;
                getView().loadCompleted(value);
            }
        });
    }

}
