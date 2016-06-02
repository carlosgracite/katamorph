package com.carlosgracite.katamorph.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class Presenter<View> {

    private View view;

    public Presenter(View view) {
        this.view = view;
    }

    public void onViewCreated(@Nullable Bundle state) {

    }

    public void onSave(Bundle bundle) {

    }

    public void onDestroy(boolean isDestroyedBySystem) {
    }

    protected View getView() {
        return view;
    }
}
