package algonquin.cst2335.ju000013;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class contains test cases for the SunriseSunset feature in the MainActivity.
 * It uses Espresso for UI testing, simulating user interactions and verifying UI responses.
 * The tests include entering latitude and longitude, clicking buttons, and checking UI changes.
 *
 * @author Zhaohong Huang
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class SunriseSunsetTest {

    /**
     * Rule to launch the MainActivity before each test. This provides an actual Activity
     * context for the tests.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests the SunriseSunset lookup functionality by simulating user inputs of latitude and longitude
     * and triggering the lookup process. It verifies that the correct UI elements are interacted with
     * and that the expected outcomes occur in response to the data entered.
     */
    @Test
    public void sunriseSunsetTest() {
        /** Added a sleep statement to match the app's execution delay.
        * The recommended way to handle such scenarios is to use Espresso idling resources:
        *https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
         */
        try {
            Thread.sleep(5788);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.item_1), withContentDescription("Sunrise & Sunset lookup"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(longClick());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.item_1), withContentDescription("Sunrise & Sunset lookup"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        /** Added a sleep statement to match the app's execution delay.
        * The recommended way to handle such scenarios is to use Espresso idling resources:
        * https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
         */
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.enterLatitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.enterLatitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.enterLongitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("111111"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.lookUpButton), withText("Lookup"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.enterLongitude), withText("111111"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText4.perform(pressImeActionButton());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.enterLatitude), withText("1"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText(""));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.enterLatitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.enterLongitude), withText("111111"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText(""));

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.enterLongitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText8.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.enterLatitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("22222"), closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.enterLongitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText10.perform(replaceText("2"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.lookUpButton), withText("Lookup"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.enterLongitude), withText("2"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText11.perform(pressImeActionButton());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.enterLatitude), withText("22222"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText12.perform(replaceText("3"));

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.enterLatitude), withText("3"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText13.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.enterLatitude), withText("3"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText14.perform(click());

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.enterLatitude), withText("3"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText15.perform(replaceText("3..."));

        ViewInteraction appCompatEditText16 = onView(
                allOf(withId(R.id.enterLatitude), withText("3..."),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText16.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText17 = onView(
                allOf(withId(R.id.enterLongitude), withText("2"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText17.perform(replaceText("333333"));

        ViewInteraction appCompatEditText18 = onView(
                allOf(withId(R.id.enterLongitude), withText("333333"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText18.perform(closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.lookUpButton), withText("Lookup"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction appCompatEditText19 = onView(
                allOf(withId(R.id.enterLongitude), withText("333333"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText19.perform(pressImeActionButton());

        ViewInteraction appCompatEditText20 = onView(
                allOf(withId(R.id.enterLatitude), withText("3..."),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText20.perform(replaceText("44444"));

        ViewInteraction appCompatEditText21 = onView(
                allOf(withId(R.id.enterLatitude), withText("44444"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText21.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText22 = onView(
                allOf(withId(R.id.enterLongitude), withText("333333"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText22.perform(replaceText("4....."));

        ViewInteraction appCompatEditText23 = onView(
                allOf(withId(R.id.enterLongitude), withText("4....."),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText23.perform(closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.lookUpButton), withText("Lookup"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction appCompatEditText24 = onView(
                allOf(withId(R.id.enterLongitude), withText("4....."),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText24.perform(pressImeActionButton());

        ViewInteraction appCompatEditText25 = onView(
                allOf(withId(R.id.enterLatitude), withText("44444"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText25.perform(replaceText(""));

        ViewInteraction appCompatEditText26 = onView(
                allOf(withId(R.id.enterLatitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText26.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText27 = onView(
                allOf(withId(R.id.enterLongitude), withText("4....."),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText27.perform(click());

        ViewInteraction appCompatEditText28 = onView(
                allOf(withId(R.id.enterLongitude), withText("4....."),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText28.perform(replaceText(""));

        ViewInteraction appCompatEditText29 = onView(
                allOf(withId(R.id.enterLongitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText29.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText30 = onView(
                allOf(withId(R.id.enterLatitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText30.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction appCompatEditText31 = onView(
                allOf(withId(R.id.enterLongitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText31.perform(replaceText("5"), closeSoftKeyboard());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.lookUpButton), withText("Lookup"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction appCompatEditText32 = onView(
                allOf(withId(R.id.enterLongitude), withText("5"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText32.perform(pressImeActionButton());

        ViewInteraction appCompatEditText33 = onView(
                allOf(withId(R.id.enterLatitude), withText("5"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText33.perform(replaceText(""));

        ViewInteraction appCompatEditText34 = onView(
                allOf(withId(R.id.enterLatitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText34.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText35 = onView(
                allOf(withId(R.id.enterLongitude), withText("5"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText35.perform(replaceText(""));

        ViewInteraction appCompatEditText36 = onView(
                allOf(withId(R.id.enterLongitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText36.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText37 = onView(
                allOf(withId(R.id.enterLatitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText37.perform(replaceText("66666666"), closeSoftKeyboard());

        ViewInteraction appCompatEditText38 = onView(
                allOf(withId(R.id.enterLongitude),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText38.perform(replaceText("66666666"), closeSoftKeyboard());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.lookUpButton), withText("Lookup"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction appCompatEditText39 = onView(
                allOf(withId(R.id.enterLongitude), withText("66666666"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText39.perform(pressImeActionButton());

        ViewInteraction appCompatEditText40 = onView(
                allOf(withId(R.id.enterLatitude), withText("66666666"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText40.perform(replaceText("77777"));

        ViewInteraction appCompatEditText41 = onView(
                allOf(withId(R.id.enterLatitude), withText("77777"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText41.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText42 = onView(
                allOf(withId(R.id.enterLongitude), withText("66666666"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText42.perform(replaceText("777777"));

        ViewInteraction appCompatEditText43 = onView(
                allOf(withId(R.id.enterLongitude), withText("777777"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText43.perform(closeSoftKeyboard());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.lookUpButton), withText("Lookup"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction appCompatEditText44 = onView(
                allOf(withId(R.id.enterLongitude), withText("777777"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText44.perform(pressImeActionButton());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.saveLocationButton), withText("Add to Favourite"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                8)),
                                6),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction materialButton9 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(com.google.android.material.R.id.buttonPanel),
                                        0),
                                3)));
        materialButton9.perform(scrollTo(), click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.enterLatitude), withText("77777"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText.check(matches(withText("Only #7 is pass!")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.enterLatitude), withText("77777"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText2.check(matches(withText("Only #7 is pass!")));
    }

    /**
     * Generates a matcher for an item at a specific position within a parent view.
     * This is used to precisely target views within a nested hierarchy.
     *
     * @param parentMatcher The matcher for the parent view.
     * @param position The position of the child view within the parent.
     * @return A Matcher that matches the child view at the specified position within its parent.
     */
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
