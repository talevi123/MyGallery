package com.tal.mygallery.Services;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by tal on 05/01/17.
 */
public class MyResultReceiver extends ResultReceiver {

    private Receiver myReceiver;

    public MyResultReceiver(Handler handler, Receiver receiver) {
        super(handler);
        this.myReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (myReceiver != null) {
            myReceiver.onReceiveResult(resultCode, resultData);
        }
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle bundle);
    }
}
