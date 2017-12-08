package com.navercorp.plugin.sample.target;

public class TargetClass03 {

    public void invoke() {
        invoke(10);
    }

    public void invoke(int times) {
        if (times <= 1) {
            return;
        }

        invoke(times - 1);
    }
}