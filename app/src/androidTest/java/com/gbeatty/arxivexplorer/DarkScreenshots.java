package com.gbeatty.arxivexplorer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;

import com.gbeatty.arxivexplorer.main.MainActivity;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy;
import tools.fastlane.screengrab.locale.LocaleTestRule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(JUnit4.class)
public class DarkScreenshots {
    static Intent intent;
    static SharedPreferences.Editor preferencesEditor;

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

        onView(withId(R.id.navigation_browse)).perform(click());
        Screengrab.screenshot("dark_browse");

        onView(withId(R.id.navigation_dashboard)).perform(click());

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

        onView(withId(R.id.navigation_downloaded)).perform(click());

        Screengrab.screenshot("dark_downloaded");

    }

}


