package com.carlosgracite.katamorph.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.carlosgracite.katamorph.presenter.RxPresenter;

public abstract class MvpSupportFragment<V, P extends RxPresenter<V>>
        extends Fragment {

    private P presenter;

    private boolean isDestroyedBySystem;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter.hasPendingRequests()) {
            presenter.onRestoreRequests();
        }
        isDestroyedBySystem = false;
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy(isDestroyedBySystem);
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (presenter != null) {
            presenter.onSave(outState);
        }
        isDestroyedBySystem = true;
    }

    public void setPresenter(@NonNull P presenter) {
        this.presenter = presenter;
    }

    public P getPresenter() {
        return presenter;
    }
}
