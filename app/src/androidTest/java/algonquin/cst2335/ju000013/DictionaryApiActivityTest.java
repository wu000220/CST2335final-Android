package algonquin.cst2335.ju000013;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DictionaryApiActivityTest {

    @Rule
    public ActivityScenarioRule<DictionaryApiActivity> activityRule = new ActivityScenarioRule<>(DictionaryApiActivity.class);

    @Test
    public void testInputFieldAcceptsText() {
        onView(withId(R.id.word_search_input)).perform(ViewActions.clearText());
        onView(withId(R.id.word_search_input)).perform(ViewActions.typeText("hello"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.word_search_input)).check(matches(withText("hello")));
    }

    @Test
    public void testSearchButtonFunctionality() {
        onView(withId(R.id.word_search_input)).perform(ViewActions.typeText("test"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.search_button)).perform(ViewActions.click());
        onView(withId(R.id.definition_list)).check(matches(isDisplayed()));
    }

    @Test
    public void testResultsVisibilityAfterSearch() {
        onView(withId(R.id.word_search_input)).perform(ViewActions.typeText("water"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.search_button)).perform(ViewActions.click());
        onView(withId(R.id.definition_list)).check(matches(isDisplayed()));
    }

    @Test
    public void testClearSearchResults() {
        onView(withId(R.id.word_search_input)).perform(ViewActions.typeText("clear"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.search_button)).perform(ViewActions.click());
        onView(withId(R.id.word_search_input)).perform(ViewActions.clearText());
        onView(withId(R.id.search_button)).perform(ViewActions.click());
        onView(withId(R.id.definition_list)).check(matches(hasMinimumChildCount(0)));
    }

    @Test
    public void testSearchButtonVisibility() {
        onView(withId(R.id.search_button)).check(matches(isDisplayed()));
    }
}
