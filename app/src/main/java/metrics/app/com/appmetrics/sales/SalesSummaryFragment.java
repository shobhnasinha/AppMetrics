package metrics.app.com.appmetrics.sales;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import metrics.app.com.appmetrics.CountryListActivity;
import metrics.app.com.appmetrics.R;
import metrics.app.com.appmetrics.SalesByDateActivity;
import metrics.app.com.appmetrics.Utils;
import metrics.app.com.appmetrics.api.ApiProvider;
import metrics.app.com.appmetrics.model.SalesAnalytics;
import metrics.app.com.appmetrics.model.SalesData;

/**
 * Fragment which represents home screen of the app, displaying summary data for analytics.
 * It also listens to internet connectivity change and retrieves summary data if needed.
 */
public class SalesSummaryFragment extends Fragment implements SalesDataContract.SummaryView {

    private ProgressBar progressBar;

    private String downloads, revenue, platform;

    private static final String NO_OF_DOWNLOADS = "NO_OF_DOWNLOADS";

    private static final String REVENUE_AMOUNT = "REVENUE_AMOUNT";

    private static final String PLATFORM = "PLATFORM";

    private SalesDataPresenter presenter;

    /*
    * Update the weather information for the cities when network is connected
    */
    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED && (downloads == null || downloads.isEmpty())
                    && Utils.isNetworkAvailable(getContext()) && presenter != null) {
                presenter.getSalesDataSummary();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sales_summary, container);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        view.findViewById(R.id.by_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkAvailable(getContext())) {
                    Intent intent = new Intent(getContext(), SalesByDateActivity.class);
                    getContext().startActivity(intent);
                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
        view.findViewById(R.id.by_country).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkAvailable(getContext())) {
                    Intent intent = new Intent(getContext(), CountryListActivity.class);
                    getContext().startActivity(intent);
                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                }
            }
        });
        presenter = new SalesDataPresenter(this, ApiProvider.getInstance().getApiEndpoint());
        if (savedInstanceState == null) {
            if (Utils.isNetworkAvailable(getContext())) {
                presenter.getSalesDataSummary();
            } else {
                Toast.makeText(getContext(), getContext().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            }
        } else {
            downloads = savedInstanceState.getString(NO_OF_DOWNLOADS);
            revenue = savedInstanceState.getString(REVENUE_AMOUNT);
            platform = savedInstanceState.getString(PLATFORM);
            setSummaryValues(view);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NO_OF_DOWNLOADS, downloads);
        outState.putString(REVENUE_AMOUNT, revenue);
        outState.putString(PLATFORM, platform);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(networkStateReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(networkStateReceiver);
        } catch (IllegalArgumentException e) {

        }
    }

    @Override
    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setSummarySalesData(SalesAnalytics salesAnalytics, boolean isError) {
        if (getView() == null) {
            return;
        }
        SalesData salesData = salesAnalytics.getSalesList().get(0);
        if (isError || salesData == null) {
            Toast.makeText(getContext(), getContext().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        } else {
            downloads = Utils.getTruncatedValueToDisplay(salesData.getDownloadsToDisplay());
            revenue = "$ " + Utils.getTruncatedValueToDisplay(salesData.getRevenueToDisplay());
            platform = salesAnalytics.getMarket();
            setSummaryValues(getView());
        }
    }

    private void setSummaryValues(View view) {
        TextView noOfDownloadsTv = (TextView) view.findViewById(R.id.noOfDownloadsValue);
        TextView revenueAmountTv = (TextView) view.findViewById(R.id.noOfRevenueValue);
        TextView marketTv = (TextView) view.findViewById(R.id.platformValue);
        noOfDownloadsTv.setText(downloads);
        revenueAmountTv.setText(revenue);
        marketTv.setText(platform);
    }
}
