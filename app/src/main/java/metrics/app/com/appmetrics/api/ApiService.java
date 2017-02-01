package metrics.app.com.appmetrics.api;

import metrics.app.com.appmetrics.Utils;
import metrics.app.com.appmetrics.model.CountryList;
import metrics.app.com.appmetrics.model.SalesAnalytics;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface ApiService {

    @GET("accounts/" + Utils.APP_ID + "/products/" + Utils.PRODUCT_ID + "/sales")
    Call<SalesAnalytics> getSalesListSummary();

    @GET("accounts/" + Utils.APP_ID + "/products/" + Utils.PRODUCT_ID + "/sales?break_down=date")
    Call<SalesAnalytics> getSalesListSplitByDate(@Query("start_date") String startDate, @Query("end_date") String endDate);

    @GET("accounts/" + Utils.APP_ID + "/products/" + Utils.PRODUCT_ID + "/sales?break_down=date")
    Call<SalesAnalytics> getNextPageSalesListSplitByDate(@Query("page_index") int pageIndex, @Query("start_date") String startDate, @Query("end_date") String endDate);

    @GET("meta/countries")
    Call<CountryList> getCountryList();

    @GET("accounts/" + Utils.APP_ID + "/products/" + Utils.PRODUCT_ID + "/sales?break_down=country")
    Call<SalesAnalytics> getSalesListSplitByCountry(@Query("start_date") String startDate, @Query("end_date") String endDate, @Query("countries") String countries);
}
