package metrics.app.com.appmetrics.api;

import java.util.ArrayList;

import metrics.app.com.appmetrics.model.Country;
import metrics.app.com.appmetrics.model.Product;
import metrics.app.com.appmetrics.model.Revenue;
import metrics.app.com.appmetrics.model.SalesAnalytics;
import metrics.app.com.appmetrics.model.SalesData;
import metrics.app.com.appmetrics.model.Units;
import metrics.app.com.appmetrics.sales.SalesDataContract;

public class MockApiEndpoint implements ApiEndpoint {
    @Override
    public void getSalesListSummary(SalesDataContract.OnSummaryCallback callback) {
        callback.onSummaryDataLoaded(getDummySalesDataSummary(), false);
    }

    @Override
    public void getSalesListSplitByDate(SalesDataContract.OnSalesDataListLoadedCallback callback, String startDate, String endDate, int nextPage) {
        callback.onSalesDataListLoaded(getDummySalesList(), false);
    }

    @Override
    public void getCountryList(SalesDataContract.OnCountryListLoadedCallback callback) {
        callback.onCountryListLoaded(getDummyCountryList(), false);
    }

    @Override
    public void getSalesListSplitByCountry(SalesDataContract.OnSalesDataListLoadedCallback callback, String startDate, String endDate, String countries) {
        callback.onSalesDataListLoaded(getDummySalesList(), false);
    }

    private SalesAnalytics getDummySalesDataSummary() {
        SalesData salesData = new SalesData();
        salesData.revenue = new Revenue();
        salesData.units = new Units();
        salesData.revenue.product = new Product();
        // Set revenue
        salesData.revenue.product.downloads = 56.7;
        salesData.revenue.product.refunds = -1.1;
        // Set download
        salesData.units.product = new Product();
        salesData.units.product.downloads = 32;

        SalesAnalytics salesAnalytics = new SalesAnalytics();
        salesAnalytics.market = "ios";
        salesAnalytics.sales_list = new ArrayList<>();
        salesAnalytics.sales_list.add(salesData);

        return salesAnalytics;
    }

    private SalesAnalytics getDummySalesList() {
        SalesData salesData1 = new SalesData();
        salesData1.revenue = new Revenue();
        salesData1.units = new Units();
        salesData1.revenue.product = new Product();
        // Set revenue
        salesData1.country = "NL";
        salesData1.revenue.product.downloads = 56.7;
        salesData1.revenue.product.refunds = -1.1;
        // Set download
        salesData1.units.product = new Product();
        salesData1.units.product.downloads = 32;
        // Set date
        salesData1.date = "2017/01/02";

        salesData1.country = "US";
        SalesData salesData2 = new SalesData();
        salesData2.revenue = new Revenue();
        salesData2.units = new Units();
        salesData2.revenue.product = new Product();
        // Set revenue
        salesData2.revenue.product.downloads = 28.7;
        salesData2.revenue.product.refunds = -1.3;
        // Set download
        salesData2.units.product = new Product();
        salesData2.units.product.downloads = 16;
        // Set date
        salesData2.date = "2017/01/05";

        SalesAnalytics salesAnalytics = new SalesAnalytics();
        salesAnalytics.sales_list = new ArrayList<>();
        salesAnalytics.sales_list.add(salesData1);
        salesAnalytics.sales_list.add(salesData2);

        return salesAnalytics;
    }

    private ArrayList<Country> getDummyCountryList() {
        ArrayList<Country> countryArrayList = new ArrayList<>();
        Country c1 = new Country();
        c1.country_code = "NL";
        c1.country_name = "Netherlands";
        Country c2 = new Country();
        c2.country_code = "US";
        c2.country_name = "United States of America";
        Country c3 = new Country();
        c3.country_code = "IND";
        c3.country_name = "India";
        countryArrayList.add(c1);
        countryArrayList.add(c2);
        countryArrayList.add(c3);
        return countryArrayList;
    }
}
