package com.carlosgracite.katamorph.sample.common;

/**
 * Created by carlos on 01/06/16.
 */
public interface TestView {
    void loadStarted();
    void loadCompleted(int value);

    void setValue(int value);
}
