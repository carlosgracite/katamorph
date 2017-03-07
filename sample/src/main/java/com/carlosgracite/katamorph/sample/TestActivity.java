package com.carlosgracite.katamorph.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.carlosgracite.katamorph.view.MvpAppCompatActivity;

public class TestActivity extends MvpAppCompatActivity<TestView, TestPresenter> implements TestView {

    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        findViewById(R.id.button_load_success)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPresenter().load(true);
                    }
                });

        findViewById(R.id.button_load_error)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPresenter().load(false);
                    }
                });

        setPresenter(new TestPresenter());
        setupPresenter(savedInstanceState);
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
