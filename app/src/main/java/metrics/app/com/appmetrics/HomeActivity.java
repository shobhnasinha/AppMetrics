package metrics.app.com.appmetrics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity which represents home screen of the app, displaying summary data for analytics
 */
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
