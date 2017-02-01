package metrics.app.com.appmetrics.sales;

import java.util.ArrayList;

import metrics.app.com.appmetrics.model.Country;
import metrics.app.com.appmetrics.model.SalesAnalytics;
import metrics.app.com.appmetrics.model.SalesData;

/**
 * The application is built with MVP architecture.
 * Presenter will talk to different views (fragments) and provide result by interacting with ApiEndpoint (through callbacks)
 */
public interface SalesDataContract {

    /**
     * View which represents home screen of the app, displaying summary data for analytics
     */
    interface SummaryView {
        void showProgress(boolean show);

        void setSummarySalesData(SalesAnalytics salesAnalytics, boolean isError);
    }

    /**
     * View which displays app sales analytics data, either based on date range or for set of countries
     */
    interface SalesDataView {
        void showProgress(boolean show);

        void setSalesDataList(ArrayList<SalesData> salesDataList, String nextPage, int pageNum, int currentPageIndex, boolean isError);
    }

    /**
     * View which displays list of countries to show sales analytics for
     */
    interface CountryListView {
        void showProgress(boolean show);

        void setCountryList(ArrayList<Country> countryList, boolean isError);
    }

    interface OnSummaryCallback {
        void onSummaryDataLoaded(SalesAnalytics salesAnalytics, boolean isError);
    }

    interface OnSalesDataListLoadedCallback {
        void onSalesDataListLoaded(SalesAnalytics salesAnalytics, boolean isError);
    }

    interface OnCountryListLoadedCallback {
        void onCountryListLoaded(ArrayList<Country> countryList, boolean isError);
    }

    /**
     * Get summary data for the sales analytics
     */
    void getSalesDataSummary();

    /**
     * Get sales analytics data for the date range
     */
    void getSalesForDateRange(String startDate, String endDate, int pageIndex);

    /**
     * Get list of countries to choose from
     */
    void getCountryList();

    /**
     * Get sales analytics data for the date range and list of countries
     */
    void getSalesForCountries(String startDate, String endDate, String countries);
}
