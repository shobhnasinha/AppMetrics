package metrics.app.com.appmetrics.sales;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import metrics.app.com.appmetrics.R;
import metrics.app.com.appmetrics.Utils;
import metrics.app.com.appmetrics.api.ApiProvider;
import metrics.app.com.appmetrics.model.SalesData;

/**
 * View which displays app sales analytics data based on date range. The value is shown graphically (horizontal bars)
 */
public class SalesByDateFragment extends Fragment implements SalesDataContract.SalesDataView {

    private ProgressBar progressBar;

    private Button fromDateButton, toDateButton, goButton;

    private RecyclerView recyclerView;

    private SalesDataPresenter presenter;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    private SalesByDateAdapter adapter;

    private ArrayList<SalesData> salesDataList;

    private TextView noDataFound;

    private static final String FROM_DATE = "FROM_DATE";

    private static final String TO_DATE = "TO_DATE";

    private static final String SALES_DATA_LIST = "SALES_DATA_LIST";

    private static final String DEFAULT_FROM_DATE = "2016-01-01";

    private static final String DEFAULT_TO_DATE = "2016-01-26";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sales_list_by_date, container);
        initViews(view);
        initAdapter();
        presenter = new SalesDataPresenter(this, ApiProvider.getInstance().getApiEndpoint());
        if (savedInstanceState == null) {
            setDateTimeField(DEFAULT_FROM_DATE, DEFAULT_TO_DATE);
            setCalendarValues(DEFAULT_FROM_DATE, DEFAULT_TO_DATE);
            fetchSalesDataFromPresenter(0);
        } else {
            setDateTimeField(savedInstanceState.getString(FROM_DATE), savedInstanceState.getString(TO_DATE));
            setCalendarValues(savedInstanceState.getString(FROM_DATE), savedInstanceState.getString(TO_DATE));
            salesDataList = (ArrayList<SalesData>) savedInstanceState.getSerializable(SALES_DATA_LIST);
            adapter.setSalesDataList(salesDataList);
        }
        fromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });
        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchSalesDataFromPresenter(0);
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FROM_DATE, fromDateButton.getText().toString());
        outState.putString(TO_DATE, toDateButton.getText().toString());
        outState.putSerializable(SALES_DATA_LIST, salesDataList);
    }

    @Override
    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSalesDataList(ArrayList<SalesData> salesDataList, String nextPage, int pageNum, int currentPageIndex, boolean isError) {
        if (salesDataList == null || isError) {
            Snackbar.make(recyclerView, getContext().getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (currentPageIndex == 0) {
            this.salesDataList = salesDataList;
        } else {
            for (SalesData salesData : salesDataList) {
                this.salesDataList.add(salesData);
            }
        }
        if (salesDataList.size() == 0) {
            noDataFound.setVisibility(View.VISIBLE);
        } else {
            noDataFound.setVisibility(View.GONE);
        }
        adapter.setSalesDataList(salesDataList);
        adapter.notifyDataSetChanged();
        if (nextPage != null && currentPageIndex < pageNum - 1) {
            fetchSalesDataFromPresenter(currentPageIndex + 1);
        }
    }

    private void initViews(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        fromDateButton = (Button) view.findViewById(R.id.choose_from_date);
        toDateButton = (Button) view.findViewById(R.id.choose_to_date);
        goButton = (Button) view.findViewById(R.id.done_action);
        noDataFound = (TextView) view.findViewById(R.id.no_data_found);
        recyclerView = (RecyclerView) view.findViewById(R.id.sales_list_by_date);
    }

    private void initAdapter() {
        adapter = new SalesByDateAdapter(getContext());
        adapter.setScreenWidth(getScreenWidth());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void setCalendarValues(String fromDate, String toDate) {
        fromDateButton.setText(fromDate);
        toDateButton.setText(toDate);
    }

    private void setDateTimeField(String fromDate, String toDate) {
        Calendar newCalendar = Calendar.getInstance();
        dateFormatter = Utils.getSimpleDateFormat();
        SalesTimeConverter salesTimeConverter = new SalesTimeConverter();
        Date startDate, endDate;
        try {
            startDate = salesTimeConverter.getDate(fromDate);
            endDate = salesTimeConverter.getDate(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        fromDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateButton.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.updateDate(salesTimeConverter.getYear(startDate), salesTimeConverter.getMonth(startDate), salesTimeConverter.getDateNumber(startDate));

        toDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateButton.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        toDatePickerDialog.updateDate(salesTimeConverter.getYear(endDate), salesTimeConverter.getMonth(endDate), salesTimeConverter.getDateNumber(endDate));
    }

    private void fetchSalesDataFromPresenter(int nextPge) {
        if (fromDateButton.getText() == null || toDateButton.getText() == null) {
            // It should never reach here
            Snackbar.make(recyclerView, "Empty date", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (!Utils.isNetworkAvailable(getContext())) {
            Snackbar.make(recyclerView, getContext().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            return;
        }
        SalesTimeConverter converter = new SalesTimeConverter();
        String fromDate = fromDateButton.getText().toString();
        String toDate = toDateButton.getText().toString();
        try {
            int noOfDays = converter.getDaysBetween(converter.getDate(fromDate), converter.getDate(toDate));
            if (noOfDays < 0) {
                Snackbar.make(recyclerView, "Invalid date range", Snackbar.LENGTH_SHORT).show();
            } else {
                presenter.getSalesForDateRange(fromDate, toDate, nextPge);
            }
        } catch (ParseException e) {
            Snackbar.make(recyclerView, "Invalid date", Snackbar.LENGTH_SHORT).show();
        }
    }

    private int getScreenWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

}
