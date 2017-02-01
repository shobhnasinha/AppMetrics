package metrics.app.com.appmetrics.api;

import android.support.v4.util.ArrayMap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import metrics.app.com.appmetrics.BuildConfig;
import metrics.app.com.appmetrics.model.CountryList;
import metrics.app.com.appmetrics.model.SalesAnalytics;
import metrics.app.com.appmetrics.sales.SalesDataContract;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

class ApiEndpointImpl implements ApiEndpoint {

    private static ObjectMapper camelCaseMapper;

    private ArrayMap<String, Call<?>> cancellableRequests = new ArrayMap<>();

    private ApiService apiService;

    private static final String APP_ID = "250888";

    private static final String PRODUCT_ID = "515736862";

    @Override
    public void getSalesListSummary(final SalesDataContract.OnSummaryCallback callback) {
        Listener<SalesAnalytics> onSuccess = new Listener<SalesAnalytics>() {
            @Override
            public void onResponse(SalesAnalytics response) {
                if (response.getSalesList() != null && response.getSalesList().size() > 0) {
                    callback.onSummaryDataLoaded(response, false);
                } else {
                    callback.onSummaryDataLoaded(null, false);
                }
            }
        };
        ErrorListener onError = new ErrorListener() {
            @Override
            public void onErrorResponse(NetworkError error) {
                callback.onSummaryDataLoaded(null, true);
            }
        };
        enqueueRequest(getApiService().getSalesListSummary(), onSuccess, onError, null);
    }

    @Override
    public void getSalesListSplitByDate(final SalesDataContract.OnSalesDataListLoadedCallback callback, String startDate, String endDate, int pageIndex) {
        Listener<SalesAnalytics> onSuccess = new Listener<SalesAnalytics>() {
            @Override
            public void onResponse(SalesAnalytics response) {
                callback.onSalesDataListLoaded(response, false);
            }
        };
        ErrorListener onError = new ErrorListener() {
            @Override
            public void onErrorResponse(NetworkError error) {
                callback.onSalesDataListLoaded(null, true);
            }
        };
        if (pageIndex == 0) {
            enqueueRequest(getApiService().getSalesListSplitByDate(startDate, endDate), onSuccess, onError, null);
        } else {
            enqueueRequest(getApiService().getNextPageSalesListSplitByDate(pageIndex, startDate, endDate), onSuccess, onError, null);
        }
    }

    @Override
    public void getCountryList(final SalesDataContract.OnCountryListLoadedCallback callback) {
        Listener<CountryList> onSuccess = new Listener<CountryList>() {
            @Override
            public void onResponse(CountryList response) {
                callback.onCountryListLoaded(response.getCountryList(), false);
            }
        };
        ErrorListener onError = new ErrorListener() {
            @Override
            public void onErrorResponse(NetworkError error) {
                callback.onCountryListLoaded(null, true);
            }
        };
        enqueueRequest(getApiService().getCountryList(), onSuccess, onError, null);
    }

    @Override
    public void getSalesListSplitByCountry(final SalesDataContract.OnSalesDataListLoadedCallback callback, String startDate, String endDate, String countries) {
        Listener<SalesAnalytics> onSuccess = new Listener<SalesAnalytics>() {
            @Override
            public void onResponse(SalesAnalytics response) {
                callback.onSalesDataListLoaded(response, false);
            }
        };
        ErrorListener onError = new ErrorListener() {
            @Override
            public void onErrorResponse(NetworkError error) {
                callback.onSalesDataListLoaded(null, true);
            }
        };
        enqueueRequest(getApiService().getSalesListSplitByCountry(startDate, endDate, countries), onSuccess, onError, null);
    }

    private <T> void enqueueRequest(Call<T> call, final Listener<T> listener, final ErrorListener errorListener, final String tag) {
        if (tag != null) {
            //cancel any of the same request still running
            if (cancellableRequests.containsKey(tag)) {
                cancellableRequests.remove(tag).cancel();
            }
            cancellableRequests.put(tag, call);
        }

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (tag != null) {
                    cancellableRequests.remove(tag);
                }
                if (response.isSuccessful()) {
                    if (listener != null) {
                        listener.onResponse(response.body());
                    }
                } else if (errorListener != null) {
                    errorListener.onErrorResponse(new NetworkError("Unknown error"));
                }

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (call.isCanceled()) {
                    return;
                }

                if (tag != null) {
                    cancellableRequests.remove(tag);
                }
                if (errorListener != null) {
                    errorListener.onErrorResponse(new NetworkError(t));
                }
            }

        });
    }

    private ApiService getApiService() {
        if (apiService == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.connectTimeout(30, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                // set your desired log level
                logging.setLevel(HttpLoggingInterceptor.Level.valueOf("BODY"));
                builder.addInterceptor(logging);
            }

            builder.authenticator(new Authenticator() {
                @Override
                public Request authenticate(Route route, okhttp3.Response response) throws IOException {
                    String bearerString = "Bearer " + "7f5c19e409ab5fb1ee7cd7bd68fa54f7c358a65f";
                    return response.request().newBuilder().header("Authorization", bearerString).build();
                }
            });

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getBaseUrl() + "/")
                    .addConverterFactory(JacksonConverterFactory.create(getCamelCase()))
                    .callFactory(builder.build())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    private static ObjectMapper getCamelCase() {
        if (camelCaseMapper == null) {
            camelCaseMapper = new ObjectMapper();
            // We don't want the app to throw an exception (and stop processing) when an unknown/new JSON property/field comes by
            camelCaseMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return camelCaseMapper;
    }

    private static String getBaseUrl() {
        // return "https://www.appannie.com/v1.2/accounts/" + APP_ID + "/products/" + PRODUCT_ID;
        return "https://www.appannie.com/v1.2";
    }
}
