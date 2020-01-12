package com.gbeatty.arxivexplorer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.gbeatty.arxivexplorer.main.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy;
import tools.fastlane.screengrab.locale.LocaleTestRule;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(JUnit4.class)
public class DarkScreenshots {
    private static Intent intent;
    private static SharedPreferences.Editor preferencesEditor;

    @ClassRule
    public static final LocaleTestRule localeTestRule = new LocaleTestRule();

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, false, false);


    @BeforeClass
    public static void setUp() {
        Screengrab.setDefaultScreenshotStrategy(new UiAutomatorScreenshotStrategy());
        intent = new Intent();
        Context context = getInstrumentation().getTargetContext();
        preferencesEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    }

    @Test
    public void takeMainScreenshots() {

        preferencesEditor.putBoolean("dark_mode", true);
        preferencesEditor.putBoolean("show_abstract", true);
        preferencesEditor.commit();

        activityRule.launchActivity(intent);

        //browse click
        ViewInteraction frameLayout = onView(
                allOf(withId(R.id.bottom_navigation_small_container),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation),
                                        1),
                                3),
                        isDisplayed()));
        frameLayout.perform(click());
//        onView(withId(R.id.navigation_browse)).perform(click());
        Screengrab.screenshot("dark_browse");

        //dashboard click
        frameLayout = onView(
                allOf(withId(R.id.bottom_navigation_small_container),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation),
                                        1),
                                0),
                        isDisplayed()));
        frameLayout.perform(click());
//        onView(withId(R.id.navigation_dashboard)).perform(click());

        WaifForUIUpdate.waifForWithId(R.id.papers_recycler_view);

        onView(withId(R.id.papers_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, MyViewAction.clickChildViewWithId(R.id.button_favorite_paper)));
        onView(withId(R.id.papers_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, MyViewAction.clickChildViewWithId(R.id.button_favorite_paper)));

        Screengrab.screenshot("dark_dashboard");

        onView(withId(R.id.papers_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        Screengrab.screenshot("dark_details");

        onView(withId(R.id.menu_download_paper))
                .perform(click());

        WaifForUIUpdate.waitFor(15000);

        Screengrab.screenshot("dark_pdf");

        UiDevice mDevice = UiDevice.getInstance(getInstrumentation());
        mDevice.pressBack();

//        onView(withId(R.id.navigation_favorites)).perform(click());
//
//        Screengrab.screenshot("favorites");

        //downloaded click
        frameLayout = onView(
                allOf(withId(R.id.bottom_navigation_small_container),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation),
                                        1),
                                2),
                        isDisplayed()));
        frameLayout.perform(click());
//        onView(withId(R.id.navigation_downloaded)).perform(click());

        Screengrab.screenshot("dark_downloaded");

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

}



