package metrics.app.com.appmetrics;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {

    public static final String APP_ID = "250888";

    public static final String PRODUCT_ID = "515736862";

    public static SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    }

    public static SimpleDateFormat getSimpleMonthFormat() {
        return new SimpleDateFormat("MMM, yyyy", Locale.ENGLISH);
    }

    public static SimpleDateFormat getSimpleYearFormat() {
        return new SimpleDateFormat("yyyy", Locale.ENGLISH);
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getTruncatedValueToDisplay(double val) {
        return String.valueOf(Math.round(val * 100.0) / 100.0);
    }
}
