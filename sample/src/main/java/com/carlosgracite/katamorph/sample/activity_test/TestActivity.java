package com.carlosgracite.katamorph.sample.activity_test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carlosgracite.katamorph.sample.R;
import com.carlosgracite.katamorph.sample.common.TestPresenter;
import com.carlosgracite.katamorph.sample.common.TestView;
import com.carlosgracite.katamorph.view.MvpAppCompatActivity;

public class TestActivity extends MvpAppCompatActivity<TestView, TestPresenter> implements TestView {

    private ProgressBar progressBar;
    private Button button;
    private TextView text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_test);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        button = (Button) findViewById(R.id.button);
        text = (TextView) findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().startLoading();
            }
        });

        setPresenter(new TestPresenter(this));
        getPresenter().onViewCreated(savedInstanceState);
    }

    @Override
    public void loadStarted() {
        progressBar.setVisibility(View.VISIBLE);
        text.setText("Loading...");
    }

    @Override
    public void loadCompleted(int value) {
        text.setText("Value: " + value);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setValue(int value) {
        text.setText("Value: " + value);
    }
}
