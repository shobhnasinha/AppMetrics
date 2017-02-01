package metrics.app.com.appmetrics.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SalesAnalytics implements Serializable {

    public ArrayList<SalesData> sales_list;

    public String next_page, market;

    public int page_num, page_index;

    public ArrayList<SalesData> getSalesList() {
        return sales_list;
    }

    public String getNextPage() {
        return next_page;
    }

    public String getMarket() {
        return market;
    }

    public int getPageNum() {
        return page_num;
    }

    public int getPageIndex() {
        return page_index;
    }
}