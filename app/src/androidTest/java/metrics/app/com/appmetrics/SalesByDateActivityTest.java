package metrics.app.com.appmetrics;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SalesByDateActivityTest extends BaseTest {

    @Rule
    public IntentsTestRule<SalesByDateActivity> activityRule = new IntentsTestRule<>(SalesByDateActivity.class, true, false);

    @Test
    public void shouldShowSummaryCardViewAndExploreMoreByDateAndCountryOptions() {
        // Start activity
        Intent startIntent = new Intent();
        activityRule.launchActivity(startIntent);

        // date_selector layout
        onView(withId(R.id.date_selector)).check(matches(isDisplayed()));
        onView(withId(R.id.from_str)).check(matches(isDisplayed()));
        onView(withId(R.id.choose_from_date)).check(matches(isDisplayed()));
        onView(withId(R.id.to_str)).check(matches(isDisplayed()));
        onView(withId(R.id.choose_to_date)).check(matches(isDisplayed()));
        onView(withId(R.id.done_action)).check(matches(isDisplayed()));


        onView(withId(R.id.sales_list_by_date)).check(matches(isDisplayed()));
    }
}
