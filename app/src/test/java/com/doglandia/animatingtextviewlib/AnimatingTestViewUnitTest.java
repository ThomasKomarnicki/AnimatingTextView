package com.doglandia.animatingtextviewlib;


import android.app.Activity;
import android.app.Application;
import android.view.View;

import org.junit.Test;


import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


/**
 * Created by Thomas on 10/24/2015.
 */
public class AnimatingTestViewUnitTest {

    @Test
    public void mockFinalMethod() {
        Activity activity = mock(Activity.class);
        Application app = mock(Application.class);
//        when(activity.getApplication()).thenReturn(app);
//
//        assertThat(app, is(equalTo(activity.getApplication())));

//        verify(activity).getApplication();
//        verifyNoMoreInteractions(activity);

        mock(View.class);
        AnimatingTextView animatingTextView = new AnimatingTextView(app);
        animatingTextView.setTextToAnimate("");
        animatingTextView.start();
    }
}
