package com.carlosgracite.katamorph.sample.fragment_test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carlosgracite.katamorph.sample.R;
import com.carlosgracite.katamorph.sample.common.TestPresenter;
import com.carlosgracite.katamorph.sample.common.TestView;
import com.carlosgracite.katamorph.view.MvpSupportFragment;

public class TestFragment extends MvpSupportFragment<TestView, TestPresenter> implements TestView {

    private ProgressBar progressBar;
    private Button button;
    private TextView text;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter(new TestPresenter(this));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_test, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        button = (Button) rootView.findViewById(R.id.button);
        text = (TextView) rootView.findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().startLoading();
            }
        });

        return rootView;
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
