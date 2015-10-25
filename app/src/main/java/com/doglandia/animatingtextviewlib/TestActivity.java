package com.doglandia.animatingtextviewlib;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Thomas on 10/11/2015.
 */
public class TestActivity extends Activity {


    private static final String TAG = "TestActivity";
    public AnimatingTextView mAnimatingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        mAnimatingTextView = (AnimatingTextView) findViewById(R.id.tv_1);
        mAnimatingTextView.setTextColor(Color.BLACK);

        AnimatingTextView tv2 = (AnimatingTextView) findViewById(R.id.tv_2);
        tv2.setTextToAnimate("TEST 2 PLEASE HELP ME");
        tv2.setTotalDuration(2000);
        tv2.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!mAnimatingTextView.isAnimating()) {
            Log.d(TAG,"restarting animation");
            mAnimatingTextView.setTextToAnimate("THIS IS A TEST PLEASE HELP, THIS IS A TEST PLEASE HELP, THIS IS A TEST PLEASE HELP, THIS IS A TEST PLEASE HELP");
            mAnimatingTextView.setTotalDuration(5000);
            mAnimatingTextView.start();
        }
    }
}
