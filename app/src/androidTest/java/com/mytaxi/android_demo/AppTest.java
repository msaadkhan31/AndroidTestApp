package com.mytaxi.android_demo;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import com.mytaxi.android_demo.activities.MainActivity;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.Console;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

/**
 * Created by Saad on 26/02/2018.
 */
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class AppTest {

    private String Username = "whiteelephant261";
    private String Password = "video";
    private String TextToSearch = "sa";
    private String DriverName = "Sarah Friedrich";
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void A_LoginTest() {
        try {
            onView(withId(R.id.edt_username)).check(matches((isDisplayed())));
            onView(withId(R.id.edt_password)).check(matches((isDisplayed())));
            onView(withId(R.id.btn_login)).check(matches((isDisplayed())));
            onView(withId(R.id.edt_username)).perform(clearText(), typeText(Username), ViewActions.closeSoftKeyboard());
            onView(withId(R.id.edt_password)).perform(clearText(), typeText(Password), ViewActions.closeSoftKeyboard());
            onView(withId(R.id.btn_login)).perform(click());
            onView(isRoot()).perform(waitFor(5000));
            onView(withId(R.id.textSearch)).check(matches(isDisplayed()));
            System.out.print("Logged IN successfully");

        } catch (NoMatchingViewException e) {
            Assert.fail("Login Failed!");
            throw e;
        }

    }
    @Test
    public void B_SearchDriverTest(){
        try{
            onView(isRoot()).perform(waitFor(5000));
            onView(withId(R.id.textSearch)).check(matches(isDisplayed()));
            onView(withId(R.id.textSearch)).perform(clearText(), typeText(TextToSearch), ViewActions.closeSoftKeyboard());
            onView(withText(DriverName))
                    .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getCurrentFocus()))))
                    .check(matches(isDisplayed()));
            System.out.print("Driver Found");
            onView(withText(DriverName))
                    .inRoot(withDecorView(not(is(mActivityTestRule.getActivity().getCurrentFocus()))))
                    .perform(click());
            onView(isRoot()).perform(waitFor(5000));
            onView(withId(R.id.textViewDriverName)).check(matches(withText(DriverName)));
            ViewInteraction callButton = onView(allOf(withId(R.id.fab),isDisplayed()));
            callButton.perform(click());
        }
        catch (NoMatchingViewException e){
            Assert.fail("Driver Not Found");
            throw e;
        }
    }

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }
}