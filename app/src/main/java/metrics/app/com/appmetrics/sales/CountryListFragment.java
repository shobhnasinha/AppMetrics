package metrics.app.com.appmetrics.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import metrics.app.com.appmetrics.R;
import metrics.app.com.appmetrics.SalesByCountryActivity;
import metrics.app.com.appmetrics.api.ApiProvider;
import metrics.app.com.appmetrics.model.Country;

/**
 * View which displays list of countries to show sales analytics for.
 * SalesByCountryActivity displays result based on country selected in here
 */
public class CountryListFragment extends Fragment implements SalesDataContract.CountryListView {

    private ProgressBar progressBar;

    private RecyclerView recyclerView;

    private CountryListAdapter adapter;

    private SalesDataPresenter presenter;

    private ArrayList<Country> countryList;

    private static final String COUNTRY_LIST = "COUNTRY_LIST";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.country_list, container);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.country_list);
        setHasOptionsMenu(true);
        initAdapter();
        presenter = new SalesDataPresenter(this, ApiProvider.getInstance().getApiEndpoint());
        if (savedInstanceState == null) {
            presenter.getCountryList();
        } else {
            countryList = (ArrayList<Country>) savedInstanceState.getSerializable(COUNTRY_LIST);
            adapter.clearSparseArray();
            adapter.setCountryList(countryList);
            adapter.notifyDataSetChanged();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(COUNTRY_LIST, countryList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set the fragment to retain state on orientation change
        setRetainInstance(true);
        // Initialize presenter for the fragment
        presenter = new SalesDataPresenter(this, ApiProvider.getInstance().getApiEndpoint());
        presenter.getCountryList();

    }

    private void initAdapter() {
        adapter = new CountryListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // To remove unchecked items
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_done, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:
                SparseArray<String> selectedList = adapter.getSelectedSparseArray();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < selectedList.size(); i++) {
                    stringBuilder.append(selectedList.get(selectedList.keyAt(i)));
                    if (i != selectedList.size() - 1) {
                        stringBuilder.append("+");
                    }
                }
                if (stringBuilder.toString().isEmpty()) {
                    Snackbar.make(recyclerView, "No country selected", Snackbar.LENGTH_SHORT).show();
                } else {
                    adapter.clearSparseArray();
                    selectedList.clear();
                    Intent intent = new Intent(getContext(), SalesByCountryActivity.class);
                    intent.putExtra(SalesByCountryFragment.COUNTRIES_LIST, stringBuilder.toString());
                    getContext().startActivity(intent);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setCountryList(ArrayList<Country> countryList, boolean isError) {
        if (isError || countryList == null || countryList.size() == 0) {
            Snackbar.make(recyclerView, getContext().getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT).show();
        } else {
            this.countryList = countryList;
            adapter.setCountryList(countryList);
        }
    }
}
