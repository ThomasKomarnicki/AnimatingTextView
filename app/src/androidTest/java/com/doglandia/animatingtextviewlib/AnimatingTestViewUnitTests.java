package com.doglandia.animatingtextviewlib;

import android.os.Handler;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by Thomas on 10/24/2015.
 */

@RunWith(AndroidJUnit4.class)
@MediumTest
public class AnimatingTestViewUnitTests {

//    @Test
//    public void mockFinalMethod() {
//        Activity activity = mock(Activity.class);
//        Application app = mock(Application.class);
//        when(activity.getApplication()).thenReturn(app);
//
//        assertThat(app, is(equalTo(activity.getApplication())));
//
//        verify(activity).getApplication();
//        verifyNoMoreInteractions(activity);
//    }

    @Rule
    public ActivityTestRule<TestActivity> mActivityRule =
            new ActivityTestRule<>(TestActivity.class);

    @Test
    public void findViewPerformActionAndCheckAssertion() {
        // Find Button and Click on it
//        onView(withId(R.id.btn_hello_android_testing)).perform(click());
//
//        // Find TextView and verify the correct text that is displayed
//        onView(withId(R.id.text_view_rocks)).check(matches(withText(
//                mActivityRule.getActivity().getString(R.string.android_testing_rocks))));

        Handler handler = new Handler();

        final AnimatingTextView animatingTextView = mActivityRule.getActivity().mAnimatingTextView;
        animatingTextView.stop();

        animatingTextView.setTextToAnimate("TEST1 TEST1 TEST1");
        animatingTextView.start();
        handler.post(new Runnable() {
            @Override
            public void run() {
                assertThat(true,is(equalTo(animatingTextView.isAnimating())));
            }
        });


    }
}
