package com.gbeatty.arxivexplorer;

import android.support.annotation.CheckResult;
import android.support.test.espresso.AmbiguousViewMatcherException;
import android.support.test.espresso.NoMatchingRootException;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.view.View;

import org.hamcrest.Matcher;

import static org.hamcrest.core.IsInstanceOf.any;

public class MatcherExtension {
    @CheckResult
    public static boolean exists(ViewInteraction interaction) {
        try {
            interaction.perform(new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return any(View.class);
                }

                @Override
                public String getDescription() {
                    return "check for existence";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    // no op, if this is run, then the execution will continue after .perform(...)
                }
            });
            return true;
        } catch (AmbiguousViewMatcherException ex) {
            // if there's any interaction later with the same matcher, that'll fail anyway
            return true; // we found more than one
        } catch (NoMatchingViewException ex) {
            return false;
        } catch (NoMatchingRootException ex) {
            // optional depending on what you think "exists" means
            return false;
        }
    }

}
