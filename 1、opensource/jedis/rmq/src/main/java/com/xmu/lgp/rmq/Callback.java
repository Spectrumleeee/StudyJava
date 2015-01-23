package com.xmu.lgp.rmq;

public interface Callback {
    public void onMessage(String message);
}
