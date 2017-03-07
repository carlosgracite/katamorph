package com.carlosgracite.katamorph.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.carlosgracite.katamorph.presenter.RxPresenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MvpAppCompatActivity<V, P extends RxPresenter<V>> extends AppCompatActivity {

    private P presenter;

    private boolean isDestroyedBySystem;

    public void setupPresenter(@Nullable Bundle savedInstanceState) {
        Set<String> requestIds = null;
        Long requestGroupId = null;

        if (savedInstanceState != null) {
            List<String> requests = savedInstanceState.getStringArrayList("requests_state");

            if (requests != null && !requests.isEmpty()) {
                requestIds = new HashSet<>(requests);
            }

            if (savedInstanceState.containsKey("requests_group_id")) {
                requestGroupId = savedInstanceState.getLong("requests_group_id");
            }
        }

        if (requestIds == null) {
            requestIds = new HashSet<>();
        }

        boolean groupFirstCreated = presenter.setupRequestIds(requestIds, requestGroupId);

        if (savedInstanceState != null) {
            presenter.onRestoreState(savedInstanceState);
        }

        presenter.bindRequests();
        presenter.attatchView(viewFactory());

        if (presenter.hasPendingRequests()) {
            presenter.onRestoreRequests();
        }
        isDestroyedBySystem = false;

        presenter.onCreate(savedInstanceState != null, groupFirstCreated);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isDestroyedBySystem) {
            if (presenter.hasPendingRequests()) {
                presenter.onRestoreRequests();
            }
            isDestroyedBySystem = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (presenter != null) {
            Set<String> requestIds = presenter.getRequestIds();
            outState.putStringArrayList("requests_state", new ArrayList<>(requestIds));
            outState.putLong("requests_group_id", presenter.getRequestGroup().getId());
            presenter.onSaveState(outState);
            presenter.disposeRequests();
        }
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

    /**
     * Override if this fragment does not the view.
     * @return
     */
    protected V viewFactory() {
        return (V)this;
    }
}
