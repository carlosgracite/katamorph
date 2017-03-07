package com.carlosgracite.katamorph.presenter;

import android.support.annotation.NonNull;

public class Presenter<View> {

    private View view;

    public void attatchView(@NonNull View view) {
        this.view = view;
    }

    public void onDestroy(boolean isDestroyedBySystem) {
    }

    @NonNull
    protected View getView() {
        return view;
    }
}