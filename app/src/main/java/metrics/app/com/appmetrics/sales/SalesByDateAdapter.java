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

class SalesByDateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SalesData> salesDataList;


    private int graphHeight;

    private int minGraphWidth, maxGraphWidth;

    private double maxDownloadValue;

    private double maxRevenueValue;

    private static final float GRAPH_WIDTH_STRETCH = 0.7f;

    private static final int SALES_CATEGORY_TITLE = 1;

    private static final int SALES_ITEM_GRAPH = 2;

    private static final int ANIM_DURATION = 500;

    SalesByDateAdapter(Context context) {
        graphHeight = (int) context.getResources().getDimension(R.dimen.sales_graph_height);
        minGraphWidth = (int) context.getResources().getDimension(R.dimen.sales_zero_value);
    }

    void setSalesDataList(ArrayList<SalesData> salesDataList) {
        this.salesDataList = salesDataList;
        updateMaxDownloadAndRevenueValue();
    }

    void setScreenWidth(int screenWidth) {
        maxGraphWidth = (int) (screenWidth * GRAPH_WIDTH_STRETCH);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case SALES_CATEGORY_TITLE:
                return new TitleViewHolder(inflater.inflate(R.layout.sales_list_title, parent, false));
            case SALES_ITEM_GRAPH:
                return new GraphViewHolder(inflater.inflate(R.layout.sales_list_item, parent, false));
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null || salesDataList == null || salesDataList.size() == 0) {
            return;
        }
        if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).salesListCategory.setText(position == 0 ? "Download" : "Revenue");
        } else {
            GraphViewHolder graphViewHolder = (GraphViewHolder) holder;
            if (position <= salesDataList.size()) {
                // Download case
                SalesData salesData = salesDataList.get(position - 1);
                graphViewHolder.graphValue.setText(Utils.getTruncatedValueToDisplay(salesData.getDownloadsToDisplay()));
                graphViewHolder.graphTitle.setText(salesData.getDateToDisplay());
                setGraphView(graphViewHolder.graphView, salesData, true);
            } else {
                // Revenue case
                SalesData salesData = salesDataList.get(position - salesDataList.size() - 2);
                graphViewHolder.graphValue.setText(String.valueOf("$ " + Utils.getTruncatedValueToDisplay(salesData.getRevenueToDisplay())));
                graphViewHolder.graphTitle.setText(salesData.getDateToDisplay());
                setGraphView(graphViewHolder.graphView, salesData, false);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (salesDataList == null || salesDataList.size() == 0) {
            return 0;
        }
        return salesDataList.size() * 2 + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == 0) {
            return -1;
        }
        if (position == 0 || position == salesDataList.size() + 1) {
            return SALES_CATEGORY_TITLE;
        } else {
            return SALES_ITEM_GRAPH;
        }
    }

    private void setGraphView(final View graphView, SalesData salesData, boolean isDownload) {
        graphView.getLayoutParams().height = graphHeight;
        double val;
        if (isDownload) {
            if (salesData.getDownloadsToDisplay() == 0) {
                val = minGraphWidth;
            } else {
                val = ((maxGraphWidth - minGraphWidth) / maxDownloadValue) * salesData.getDownloadsToDisplay();
            }
        } else {
            if (salesData.getRevenueToDisplay() == 0) {
                val = minGraphWidth;
            } else {
                val = ((maxGraphWidth - minGraphWidth) / maxRevenueValue) * salesData.getRevenueToDisplay();
            }
        }
        graphView.getLayoutParams().width = (int) (val);
        graphView.requestLayout();
    }

    private void updateMaxDownloadAndRevenueValue() {
        for (SalesData salesData : salesDataList) {
            if (salesData.getDownloadsToDisplay() > maxDownloadValue) {
                maxDownloadValue = salesData.getDownloadsToDisplay();
            }
            if (salesData.getRevenueToDisplay() > maxRevenueValue) {
                maxRevenueValue = salesData.getRevenueToDisplay();
            }
        }
    }

    private class GraphViewHolder extends RecyclerView.ViewHolder {

        TextView graphTitle, graphValue;

        View graphView;

        GraphViewHolder(View itemView) {
            super(itemView);
            graphTitle = (TextView) itemView.findViewById(R.id.graph_title);
            graphValue = (TextView) itemView.findViewById(R.id.graph_value);
            graphView = itemView.findViewById(R.id.graph_view);
        }
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {

        TextView salesListCategory;

        TitleViewHolder(View itemView) {
            super(itemView);
            salesListCategory = (TextView) itemView.findViewById(R.id.sales_category_title);
        }
    }
}
