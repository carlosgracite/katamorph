package com.carlosgracite.katamorph.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.carlosgracite.katamorph.presenter.RxPresenter;

public class MvpAppCompatActivity<V, P extends RxPresenter<V>> extends AppCompatActivity {

    private P presenter;

    private boolean isDestroyedBySystem;

    @Override
    public void onResume() {
        super.onResume();
        if (presenter.hasPendingRequests()) {
            presenter.onRestoreRequests();
        }
        isDestroyedBySystem = false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSave(outState);
        isDestroyedBySystem = true;
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy(isDestroyedBySystem);
        super.onDestroy();
    }

    public void setPresenter(@NonNull P presenter) {
        this.presenter = presenter;
    }

    public P getPresenter() {
        return presenter;
    }
}
