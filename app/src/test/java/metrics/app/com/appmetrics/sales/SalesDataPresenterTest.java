package metrics.app.com.appmetrics.sales;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;

import metrics.app.com.appmetrics.api.ApiEndpoint;
import metrics.app.com.appmetrics.model.Country;
import metrics.app.com.appmetrics.model.SalesAnalytics;
import metrics.app.com.appmetrics.model.SalesData;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SalesDataPresenterTest {

    private SalesDataPresenter presenter;

    @Mock
    private SalesDataContract.SummaryView summaryView;

    @Mock
    private SalesDataContract.SalesDataView salesDataView;

    @Mock
    private SalesDataContract.CountryListView countryListView;

    @Mock
    private ApiEndpoint apiEndpoint;

    @Mock
    private SalesAnalytics salesAnalytics;

    @Mock
    private ArrayList<SalesData> salesDataList, changedSalesDataList;

    @Mock
    private ArrayList<Country> countryList;

    @Mock
    private SalesTimeConverter salesTimeConverter;

    @Mock
    private Date startDate, endDate;

    @Captor
    private ArgumentCaptor<SalesDataContract.OnSummaryCallback> onSummaryCallbackCaptor;

    @Captor
    private ArgumentCaptor<SalesDataContract.OnSalesDataListLoadedCallback> onSalesDataListLoadedCallbackCaptor;

    @Captor
    private ArgumentCaptor<SalesDataContract.OnCountryListLoadedCallback> onCountryListCallbackCaptor;

    @Captor
    private ArgumentCaptor<String> startDateCaptor;

    @Captor
    private ArgumentCaptor<String> endDateCaptor;

    @Captor
    private ArgumentCaptor<String> countryCaptor;

    @Captor
    private ArgumentCaptor<Integer> pageIndexCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getSalesDataSummaryShouldHandleProgressBarVisibilityAndSetSalesAnalyticsSummaryInCallback() {
        presenter = new SalesDataPresenter(summaryView, apiEndpoint);
        // Call method
        presenter.getSalesDataSummary();

        // Verify
        verify(summaryView).showProgress(true);
        verify(apiEndpoint).getSalesListSummary(onSummaryCallbackCaptor.capture());
        // Mock callback
        onSummaryCallbackCaptor.getValue().onSummaryDataLoaded(salesAnalytics, false);
        // Verify
        verify(summaryView).showProgress(false);
        verify(summaryView).setSummarySalesData(salesAnalytics, false);
    }

    @Test
    public void getSalesForDateRangeShouldHandleProgressBarVisibilityAndSetYearlyDataInCallbackWhenDateRangeIsMoreThanYear() throws Exception {
        String startDateStr = "2016-01-20", endDateStr = "2017-01-30";
        int pageIndex = 0;

        presenter = new SalesDataPresenter(salesDataView, apiEndpoint);
        SalesDataPresenter spy = Mockito.spy(presenter);

        when(salesAnalytics.getSalesList()).thenReturn(salesDataList);
        when(spy.getSalesTimeConverter()).thenReturn(salesTimeConverter);
        when(salesTimeConverter.getDate(startDateStr)).thenReturn(startDate);
        when(salesTimeConverter.getDate(endDateStr)).thenReturn(endDate);
        when(salesTimeConverter.getYearsBetween(startDate, endDate)).thenReturn(1);
        when(salesTimeConverter.getDataFromDailySales(salesDataList, startDateStr, endDateStr, SalesTimeConverter.TimeRangeType.YEAR)).thenReturn(changedSalesDataList);

        // Call method
        spy.getSalesForDateRange(startDateStr, endDateStr, pageIndex);

        // Verify
        verify(salesDataView).showProgress(true);
        verify(apiEndpoint).getSalesListSplitByDate(onSalesDataListLoadedCallbackCaptor.capture(), startDateCaptor.capture(), endDateCaptor.capture(), pageIndexCaptor.capture());
        // Mock callback
        onSalesDataListLoadedCallbackCaptor.getValue().onSalesDataListLoaded(salesAnalytics, false);
        verify(salesDataView).showProgress(false);
        verify(salesDataView).setSalesDataList(changedSalesDataList, salesAnalytics.getNextPage(), salesAnalytics.getPageNum(), salesAnalytics.getPageIndex(), false);
    }

    @Test
    public void getSalesForDateRangeShouldHandleProgressBarVisibilityAndSetMonthlyDataInCallbackWhenDateRangeIsMoreThanMonth() throws Exception {
        String startDateStr = "2016-01-20", endDateStr = "2016-06-30";
        int pageIndex = 0;

        presenter = new SalesDataPresenter(salesDataView, apiEndpoint);
        SalesDataPresenter spy = Mockito.spy(presenter);

        when(salesAnalytics.getSalesList()).thenReturn(salesDataList);
        when(spy.getSalesTimeConverter()).thenReturn(salesTimeConverter);
        when(salesTimeConverter.getDate(startDateStr)).thenReturn(startDate);
        when(salesTimeConverter.getDate(endDateStr)).thenReturn(endDate);
        when(salesTimeConverter.getYearsBetween(startDate, endDate)).thenReturn(0);
        when(salesTimeConverter.getMonthsBetween(startDate, endDate)).thenReturn(6);
        when(salesTimeConverter.getDataFromDailySales(salesDataList, startDateStr, endDateStr, SalesTimeConverter.TimeRangeType.MONTH)).thenReturn(changedSalesDataList);

        // Call method
        spy.getSalesForDateRange(startDateStr, endDateStr, pageIndex);

        // Verify
        verify(salesDataView).showProgress(true);
        verify(apiEndpoint).getSalesListSplitByDate(onSalesDataListLoadedCallbackCaptor.capture(), startDateCaptor.capture(), endDateCaptor.capture(), pageIndexCaptor.capture());
        // Mock callback
        onSalesDataListLoadedCallbackCaptor.getValue().onSalesDataListLoaded(salesAnalytics, false);
        verify(salesDataView).showProgress(false);
        verify(salesDataView).setSalesDataList(changedSalesDataList, salesAnalytics.getNextPage(), salesAnalytics.getPageNum(), salesAnalytics.getPageIndex(), false);
    }

    @Test
    public void getSalesForDateRangeShouldHandleProgressBarVisibilityAndSetWeeklyDataInCallbackWhenDateRangeIsMoreThanWeek() throws Exception {
        String startDateStr = "2016-01-20", endDateStr = "2016-02-01";
        int pageIndex = 0;

        presenter = new SalesDataPresenter(salesDataView, apiEndpoint);
        SalesDataPresenter spy = Mockito.spy(presenter);

        when(salesAnalytics.getSalesList()).thenReturn(salesDataList);
        when(spy.getSalesTimeConverter()).thenReturn(salesTimeConverter);
        when(salesTimeConverter.getDate(startDateStr)).thenReturn(startDate);
        when(salesTimeConverter.getDate(endDateStr)).thenReturn(endDate);
        when(salesTimeConverter.getYearsBetween(startDate, endDate)).thenReturn(0);
        when(salesTimeConverter.getMonthsBetween(startDate, endDate)).thenReturn(0);
        when(salesTimeConverter.getWeeksBetween(startDate, endDate)).thenReturn(2);
        when(salesTimeConverter.getDataFromDailySales(salesDataList, startDateStr, endDateStr, SalesTimeConverter.TimeRangeType.WEEK)).thenReturn(changedSalesDataList);

        // Call method
        spy.getSalesForDateRange(startDateStr, endDateStr, pageIndex);

        // Verify
        verify(salesDataView).showProgress(true);
        verify(apiEndpoint).getSalesListSplitByDate(onSalesDataListLoadedCallbackCaptor.capture(), startDateCaptor.capture(), endDateCaptor.capture(), pageIndexCaptor.capture());
        // Mock callback
        onSalesDataListLoadedCallbackCaptor.getValue().onSalesDataListLoaded(salesAnalytics, false);
        verify(salesDataView).showProgress(false);
        verify(salesDataView).setSalesDataList(changedSalesDataList, salesAnalytics.getNextPage(), salesAnalytics.getPageNum(), salesAnalytics.getPageIndex(), false);
    }

    @Test
    public void getSalesForDateRangeShouldHandleProgressBarVisibilityAndSetDailyDataInCallbackWhenDateRangeIsLessThanWeek() throws Exception {
        String startDateStr = "2016-01-20", endDateStr = "2016-01-21";
        int pageIndex = 0;

        presenter = new SalesDataPresenter(salesDataView, apiEndpoint);
        SalesDataPresenter spy = Mockito.spy(presenter);

        when(salesAnalytics.getSalesList()).thenReturn(salesDataList);
        when(spy.getSalesTimeConverter()).thenReturn(salesTimeConverter);
        when(salesTimeConverter.getDate(startDateStr)).thenReturn(startDate);
        when(salesTimeConverter.getDate(endDateStr)).thenReturn(endDate);
        when(salesTimeConverter.getYearsBetween(startDate, endDate)).thenReturn(0);
        when(salesTimeConverter.getMonthsBetween(startDate, endDate)).thenReturn(0);
        when(salesTimeConverter.getWeeksBetween(startDate, endDate)).thenReturn(0);
        when(salesTimeConverter.getDaysBetween(startDate, endDate)).thenReturn(2);
        when(salesTimeConverter.getDataFromDailySales(salesDataList, startDateStr, endDateStr, SalesTimeConverter.TimeRangeType.DAY)).thenReturn(changedSalesDataList);

        // Call method
        spy.getSalesForDateRange(startDateStr, endDateStr, pageIndex);

        // Verify
        verify(salesDataView).showProgress(true);
        verify(apiEndpoint).getSalesListSplitByDate(onSalesDataListLoadedCallbackCaptor.capture(), startDateCaptor.capture(), endDateCaptor.capture(), pageIndexCaptor.capture());
        // Mock callback
        onSalesDataListLoadedCallbackCaptor.getValue().onSalesDataListLoaded(salesAnalytics, false);
        verify(salesDataView).showProgress(false);
        verify(salesDataView).setSalesDataList(changedSalesDataList, salesAnalytics.getNextPage(), salesAnalytics.getPageNum(), salesAnalytics.getPageIndex(), false);
    }

    @Test
    public void getCountryListShouldHandleProgressBarVisibilityAndSetCountryListInCallback() {
        presenter = new SalesDataPresenter(countryListView, apiEndpoint);
        // Call method
        presenter.getCountryList();

        // Verify
        verify(countryListView).showProgress(true);
        verify(apiEndpoint).getCountryList(onCountryListCallbackCaptor.capture());
        // Mock callback
        onCountryListCallbackCaptor.getValue().onCountryListLoaded(countryList, false);
        // Verify
        verify(countryListView).showProgress(false);
        verify(countryListView).setCountryList(countryList, false);

    }

    @Test
    public void getSalesForCountriesShouldHandleProgressBarVisibilityAndSetCountryDataInCallback() {
        String startDateStr = "2016-01-20", endDateStr = "2016-02-01";
        String countries = "US+NL";

        when(salesAnalytics.getSalesList()).thenReturn(salesDataList);

        presenter = new SalesDataPresenter(salesDataView, apiEndpoint);
        // Call method
        presenter.getSalesForCountries(startDateStr, endDateStr, countries);

        // Verify
        verify(salesDataView).showProgress(true);
        verify(apiEndpoint).getSalesListSplitByCountry(onSalesDataListLoadedCallbackCaptor.capture(), startDateCaptor.capture(), endDateCaptor.capture(), countryCaptor.capture());
        // Mock callback
        onSalesDataListLoadedCallbackCaptor.getValue().onSalesDataListLoaded(salesAnalytics, false);
        verify(salesDataView).showProgress(false);
        verify(salesDataView).setSalesDataList(salesDataList, null, 1, 0, false);
    }
}
