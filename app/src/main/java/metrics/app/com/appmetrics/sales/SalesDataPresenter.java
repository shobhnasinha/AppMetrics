package metrics.app.com.appmetrics.sales;

import java.text.ParseException;
import java.util.ArrayList;

import metrics.app.com.appmetrics.api.ApiEndpoint;
import metrics.app.com.appmetrics.model.Country;
import metrics.app.com.appmetrics.model.SalesAnalytics;
import metrics.app.com.appmetrics.model.SalesData;

/**
 * The application is built with MVP architecture.
 * Presenter will talk to different views (fragments) and provide result by interacting with ApiEndpoint (through callbacks)
 */
class SalesDataPresenter implements SalesDataContract {

    private SalesDataContract.SummaryView summaryView;

    private SalesDataContract.SalesDataView salesDataView;

    private SalesDataContract.CountryListView countryListView;

    private SalesTimeConverter salesTimeConverter;

    private ApiEndpoint apiEndpoint;

    SalesDataPresenter(SalesDataContract.SummaryView summaryView, ApiEndpoint apiEndpoint) {
        this.summaryView = summaryView;
        this.apiEndpoint = apiEndpoint;
    }

    SalesDataPresenter(SalesDataContract.SalesDataView salesDataView, ApiEndpoint apiEndpoint) {
        this.salesDataView = salesDataView;
        this.apiEndpoint = apiEndpoint;
    }

    SalesDataPresenter(SalesDataContract.CountryListView countryListView, ApiEndpoint apiEndpoint) {
        this.countryListView = countryListView;
        this.apiEndpoint = apiEndpoint;
    }

    /**
     * Get summary data for the sales analytics
     */
    @Override
    public void getSalesDataSummary() {
        summaryView.showProgress(true);
        apiEndpoint.getSalesListSummary(new OnSummaryCallback() {
            @Override
            public void onSummaryDataLoaded(SalesAnalytics salesAnalytics, boolean isError) {
                summaryView.showProgress(false);
                summaryView.setSummarySalesData(salesAnalytics, isError);
            }
        });
    }

    /**
     * Get sales analytics data for the date range
     */
    @Override
    public void getSalesForDateRange(final String startDate, final String endDate, int pageIndex) {
        salesDataView.showProgress(true);
        apiEndpoint.getSalesListSplitByDate(new OnSalesDataListLoadedCallback() {
            @Override
            public void onSalesDataListLoaded(SalesAnalytics salesAnalytics, boolean isError) {
                salesDataView.showProgress(false);
                if (salesAnalytics == null || salesAnalytics.getSalesList() == null) {
                    salesDataView.setSalesDataList(null, null, -1, -1, true);
                    return;
                }
                ArrayList<SalesData> salesDataList;
                try {
                    if (getSalesTimeConverter().getYearsBetween(getSalesTimeConverter().getDate(startDate), getSalesTimeConverter().getDate(endDate)) > 0) {
                        // Dates are for range > 1 year
                        salesDataList = getSalesTimeConverter().getDataFromDailySales(salesAnalytics.getSalesList(), startDate, endDate, SalesTimeConverter.TimeRangeType.YEAR);
                    } else if (getSalesTimeConverter().getMonthsBetween(getSalesTimeConverter().getDate(startDate), getSalesTimeConverter().getDate(endDate)) > 0) {
                        // Dates are for range > 1 month
                        salesDataList = getSalesTimeConverter().getDataFromDailySales(salesAnalytics.getSalesList(), startDate, endDate, SalesTimeConverter.TimeRangeType.MONTH);
                    } else if (getSalesTimeConverter().getWeeksBetween(getSalesTimeConverter().getDate(startDate), getSalesTimeConverter().getDate(endDate)) > 0) {
                        // Dates are for range > 1 week
                        salesDataList = getSalesTimeConverter().getDataFromDailySales(salesAnalytics.getSalesList(), startDate, endDate, SalesTimeConverter.TimeRangeType.WEEK);
                    } else {
                        salesDataList = getSalesTimeConverter().getDataFromDailySales(salesAnalytics.getSalesList(), startDate, endDate, SalesTimeConverter.TimeRangeType.DAY);
                    }
                    salesDataView.setSalesDataList(salesDataList, salesAnalytics.getNextPage(), salesAnalytics.getPageNum(), salesAnalytics.getPageIndex(), false);
                } catch (ParseException e) {
                    e.printStackTrace();
                    // Incorrect data format
                    salesDataView.setSalesDataList(null, null, -1, -1, true);
                }
            }
        }, startDate, endDate, pageIndex);
    }

    /**
     * Get list of countries to choose from
     */
    @Override
    public void getCountryList() {
        countryListView.showProgress(true);
        apiEndpoint.getCountryList(new OnCountryListLoadedCallback() {
            @Override
            public void onCountryListLoaded(ArrayList<Country> countryList, boolean isError) {
                countryListView.showProgress(false);
                countryListView.setCountryList(countryList, isError);
            }
        });
    }

    /**
     * Get sales analytics data for the date range and list of countries
     */
    @Override
    public void getSalesForCountries(String startDate, String endDate, String countries) {
        salesDataView.showProgress(true);
        apiEndpoint.getSalesListSplitByCountry(new OnSalesDataListLoadedCallback() {
            @Override
            public void onSalesDataListLoaded(SalesAnalytics salesAnalytics, boolean isError) {
                salesDataView.showProgress(false);
                salesDataView.setSalesDataList(salesAnalytics.getSalesList(), null, 1, 0, false);
            }
        }, startDate, endDate, countries);
    }

    SalesTimeConverter getSalesTimeConverter() {
        if (salesTimeConverter == null) {
            salesTimeConverter = new SalesTimeConverter();
        }
        return salesTimeConverter;
    }
}
