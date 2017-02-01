package metrics.app.com.appmetrics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Activity which displays list of countries to show sales analytics for.
 * SalesByCountryActivity displays result based on country selected in here
 */
public class CountryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);
    }
}
