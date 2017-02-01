package metrics.app.com.appmetrics.sales;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import metrics.app.com.appmetrics.R;
import metrics.app.com.appmetrics.Utils;
import metrics.app.com.appmetrics.model.SalesData;

class SalesByCountryAdapter extends RecyclerView.Adapter<SalesByCountryAdapter.ViewHolder> {

    private ArrayList<SalesData> salesDataList;

    SalesByCountryAdapter() {
    }

    void setSalesDataList(ArrayList<SalesData> salesDataList) {
        this.salesDataList = salesDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.country_sales_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.countryName.setText(salesDataList.get(position).country);
        holder.downloadValue.setText(Utils.getTruncatedValueToDisplay(salesDataList.get(position).getDownloadsToDisplay()));
        holder.revenueValue.setText("$ " + Utils.getTruncatedValueToDisplay(salesDataList.get(position).getRevenueToDisplay()));
    }

    @Override
    public int getItemCount() {
        return salesDataList == null ? 0 : salesDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView countryName, downloadValue, revenueValue;

        ViewHolder(View itemView) {
            super(itemView);
            countryName = (TextView) itemView.findViewById(R.id.country_name);
            downloadValue = (TextView) itemView.findViewById(R.id.no_of_downloads_value);
            revenueValue = (TextView) itemView.findViewById(R.id.no_of_revenue_value);
        }
    }
}
