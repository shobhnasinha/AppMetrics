package metrics.app.com.appmetrics.api;

import metrics.app.com.appmetrics.sales.SalesDataContract;

public interface ApiEndpoint {

    void getSalesListSummary(SalesDataContract.OnSummaryCallback callback);

    void getSalesListSplitByDate(SalesDataContract.OnSalesDataListLoadedCallback callback, String startDate, String endDate, int nextPage);

    void getCountryList(SalesDataContract.OnCountryListLoadedCallback callback);

    void getSalesListSplitByCountry(SalesDataContract.OnSalesDataListLoadedCallback callback, String startDate, String endDate, String countries);
}