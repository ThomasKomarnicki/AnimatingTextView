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
    private AnimatingTextView mAnimatingTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        mAnimatingTextView = (AnimatingTextView) findViewById(R.id.animating_text_view);
        mAnimatingTextView.setTextColor(Color.BLACK);

        if(!mAnimatingTextView.isAnimating()) {
            Log.d(TAG,"restarting animation");
            mAnimatingTextView.setTextToAnimate("THIS IS A TEST PLEASE HELP, THIS IS A TEST PLEASE HELP, THIS IS A TEST PLEASE HELP, THIS IS A TEST PLEASE HELP");
            mAnimatingTextView.setDurationPerCharacter(100);
            mAnimatingTextView.start();
        }
//        mAnimatingTextView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mAnimatingTextView.start();
//            }
//        }, 2000);
    }
}
