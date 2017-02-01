package metrics.app.com.appmetrics.sales;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

import metrics.app.com.appmetrics.R;
import metrics.app.com.appmetrics.model.Country;

class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {

    private ArrayList<Country> countryList;

    private SparseArray<String> sparseArray;

    CountryListAdapter() {
        sparseArray = new SparseArray<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.country_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.checkBox.setText(countryList.get(position).getCountryName());
        holder.checkBox.setChecked(sparseArray.get(position) != null);
    }

    @Override
    public int getItemCount() {
        return countryList == null ? 0 : countryList.size();
    }

    void setCountryList(ArrayList<Country> countryList) {
        this.countryList = countryList;
    }

    SparseArray<String> getSelectedSparseArray() {
        return sparseArray;
    }

    void clearSparseArray() {
        sparseArray.clear();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.country_item);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        sparseArray.put(getLayoutPosition(), countryList.get(getLayoutPosition()).getCountryCode());
                    } else {
                        sparseArray.remove(getLayoutPosition());
                    }
                }
            });
        }
    }
}
