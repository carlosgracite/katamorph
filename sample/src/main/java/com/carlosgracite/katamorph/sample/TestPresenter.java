package com.carlosgracite.katamorph.sample;

import android.util.Log;

import com.carlosgracite.katamorph.presenter.RxPresenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class TestPresenter extends RxPresenter<TestView> {

    public static final String REQUEST_DATA = "request_data";

    public void bindRequests() {

        register(REQUEST_DATA, new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                getView().showLoading(true);
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String result) throws Exception {
                getView().showLoading(false);
                Log.d("LOL", "onSuccess: " + result);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                getView().showLoading(false);
                Log.d("LOL", "onError");
            }
        });

    }

    @Override
    public void onCreate(boolean recreated, boolean firstCreated) {
        if (firstCreated && !isPendingRequest(REQUEST_DATA)) {
            load(true);
        }
    }

    @Override
    public void onDestroy(boolean isDestroyedBySystem) {
        super.onDestroy(isDestroyedBySystem);
    }

    public void load(final boolean isSuccess) {
        doRequest(REQUEST_DATA, Observable.just("Success")
                .delay(3000, TimeUnit.MILLISECONDS)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        if (isSuccess) {
                            return s;
                        } else {
                            throw Exceptions.propagate(new Exception());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()));
    }

}
