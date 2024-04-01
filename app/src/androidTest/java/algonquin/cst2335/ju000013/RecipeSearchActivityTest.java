package algonquin.cst2335.ju000013;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeSearchActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * This function is for testing search text that contains #.
     */
    @Test
    public void testSearchText1() {
        onView(withId(R.id.button2)).perform(click());

        //find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editRecipeSearchText));
        //type nothing
        appCompatEditText.perform(replaceText("#"), closeSoftKeyboard());

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.buttonSearchRecipe));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.validateRecipeSearchText));
        //check the text
        textView.check(matches(withText(R.string.recipe_validate_error_msg)));
    }

    /**
     * This function is for testing search text that contains @.
     */
    @Test
    public void testSearchText2() {
        onView(withId(R.id.button2)).perform(click());

        //find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editRecipeSearchText));
        //type nothing
        appCompatEditText.perform(replaceText("@"), closeSoftKeyboard());

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.buttonSearchRecipe));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.validateRecipeSearchText));
        //check the text
        textView.check(matches(withText(R.string.recipe_validate_error_msg)));
    }

    /**
     * This function is for testing search text that contains ?.
     */
    @Test
    public void testSearchText3() {
        onView(withId(R.id.button2)).perform(click());

        //find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editRecipeSearchText));
        //type nothing
        appCompatEditText.perform(replaceText("?"), closeSoftKeyboard());

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.buttonSearchRecipe));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.validateRecipeSearchText));
        //check the text
        textView.check(matches(withText(R.string.recipe_validate_error_msg)));
    }

    /**
     * This function is for testing search text that contains !.
     */
    @Test
    public void testSearchText4() {
        onView(withId(R.id.button2)).perform(click());

        //find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editRecipeSearchText));
        //type nothing
        appCompatEditText.perform(replaceText("!"), closeSoftKeyboard());

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.buttonSearchRecipe));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.validateRecipeSearchText));
        //check the text
        textView.check(matches(withText(R.string.recipe_validate_error_msg)));
    }

    /**
     * This function is for testing search text that contains *.
     */
    @Test
    public void testSearchText5() {
        onView(withId(R.id.button2)).perform(click());

        //find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editRecipeSearchText));
        //type nothing
        appCompatEditText.perform(replaceText("*"), closeSoftKeyboard());

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.buttonSearchRecipe));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.validateRecipeSearchText));
        //check the text
        textView.check(matches(withText(R.string.recipe_validate_error_msg)));
    }

    /**
     * This function is for testing search text that contains &.
     */
    @Test
    public void testSearchText6() {
        onView(withId(R.id.button2)).perform(click());

        //find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editRecipeSearchText));
        //type nothing
        appCompatEditText.perform(replaceText("&"), closeSoftKeyboard());

        //find the button
        ViewInteraction materialButton = onView(withId(R.id.buttonSearchRecipe));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.validateRecipeSearchText));
        //check the text
        textView.check(matches(withText(R.string.recipe_validate_error_msg)));
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
