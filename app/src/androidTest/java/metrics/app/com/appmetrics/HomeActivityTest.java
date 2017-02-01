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
public class HomeActivityTest extends BaseTest {

    @Rule
    public IntentsTestRule<HomeActivity> activityRule = new IntentsTestRule<>(HomeActivity.class, true, false);

    @Test
    public void shouldShowSummaryCardViewAndExploreMoreByDateAndCountryOptions() {
        // Start activity
        Intent startIntent = new Intent();
        activityRule.launchActivity(startIntent);

        onView(withId(R.id.card_view)).check(matches(isDisplayed()));
        onView(withId(R.id.noOfDownloadsDesc)).check(matches(isDisplayed()));
        onView(withId(R.id.noOfDownloadsValue)).check(matches(isDisplayed()));
        onView(withId(R.id.noOfRevenueDesc)).check(matches(isDisplayed()));
        onView(withId(R.id.noOfRevenueValue)).check(matches(isDisplayed()));
        onView(withId(R.id.platformDesc)).check(matches(isDisplayed()));
        onView(withId(R.id.platformValue)).check(matches(isDisplayed()));
        onView(withText(R.string.explore_more)).check(matches(isDisplayed()));
        onView(withId(R.id.by_date)).check(matches(isDisplayed()));
        onView(withId(R.id.by_country)).check(matches(isDisplayed()));
    }
}
