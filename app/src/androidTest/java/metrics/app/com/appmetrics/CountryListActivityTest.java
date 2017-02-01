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

@RunWith(AndroidJUnit4.class)
public class CountryListActivityTest extends BaseTest {

    @Rule
    public IntentsTestRule<CountryListActivity> activityRule = new IntentsTestRule<>(CountryListActivity.class, true, false);

    @Test
    public void shouldShowSummaryCardViewAndExploreMoreByDateAndCountryOptions() {
        // Start activity
        Intent startIntent = new Intent();
        activityRule.launchActivity(startIntent);

        onView(withId(R.id.select_country_title)).check(matches(isDisplayed()));
        onView(withId(R.id.country_list)).check(matches(isDisplayed()));
    }
}
