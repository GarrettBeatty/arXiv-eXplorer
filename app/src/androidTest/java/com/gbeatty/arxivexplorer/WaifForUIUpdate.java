package com.gbeatty.arxivexplorer;

import androidx.annotation.IdRes;
import androidx.test.espresso.ViewInteraction;

import org.junit.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class WaifForUIUpdate {

    public static void waifForWithId(@IdRes int stringId) {

        ViewInteraction element;
        do {
            waitFor(3000);

            //simple example using withText Matcher.
            element = onView(withId(stringId));

        } while (!MatcherExtension.exists(element));

    }


    static void waitFor(int ms) {
        final CountDownLatch signal = new CountDownLatch(1);

        try {
            signal.await(ms, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Assert.fail(e.getMessage());
        }
    }
}
