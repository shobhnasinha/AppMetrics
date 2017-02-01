package metrics.app.com.appmetrics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Activity which displays app sales analytics data based on date range
 */
public class SalesByDateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_list_by_date);
    }
}
