package com.carlosgracite.katamorph.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.carlosgracite.katamorph.view.MvpSupportFragment;

public class TestFragment extends MvpSupportFragment<TestView, TestPresenter> implements TestView {

    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_test, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        rootView.findViewById(R.id.button_load_success)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPresenter().load(true);
                    }
                });

        rootView.findViewById(R.id.button_load_error)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPresenter().load(false);
                    }
                });

        setPresenter(new TestPresenter());

        return rootView;
    }

    @Override
    public void showResult(String result) {
        Log.d("LOL", "result: " + result);
    }

    @Override
    public void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}
