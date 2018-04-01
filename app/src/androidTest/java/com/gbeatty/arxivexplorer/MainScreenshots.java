package com.gbeatty.arxivexplorer;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.view.View;

import com.gbeatty.arxivexplorer.main.MainActivity;

import org.hamcrest.Matcher;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy;
import tools.fastlane.screengrab.locale.LocaleTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(JUnit4.class)
public class MainScreenshots {
    @ClassRule
    public static final LocaleTestRule localeTestRule = new LocaleTestRule();

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void setUp() {
        Screengrab.setDefaultScreenshotStrategy(new UiAutomatorScreenshotStrategy());
    }

    @Test
    public void takeMainScreenshots() {
        onView(withId(R.id.navigation_browse)).perform(click());
        Screengrab.screenshot("main");

        onView(withId(R.id.navigation_dashboard)).perform(click());

        WaifForUIUpdate.waifForWithId(R.id.papers_recycler_view);

        onView(withId(R.id.papers_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, MyViewAction.clickChildViewWithId(R.id.button_favorite_paper)));
        onView(withId(R.id.papers_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, MyViewAction.clickChildViewWithId(R.id.button_favorite_paper)));

        Screengrab.screenshot("dashboard");

        onView(withId(R.id.papers_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        Screengrab.screenshot("details");

        onView(withId(R.id.menu_download_paper))
                .perform(click());

        WaifForUIUpdate.waitFor(7000);

        Screengrab.screenshot("downloaded");

        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressBack();

//        onView(withId(R.id.navigation_favorites)).perform(click());
//
//        Screengrab.screenshot("favorites");

    }
}

class MyViewAction {

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}


