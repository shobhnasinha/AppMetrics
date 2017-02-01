package metrics.app.com.appmetrics.model;

import java.io.Serializable;

public class SalesData implements Serializable {
    public String date;

    public String country;

    public Units units;

    public Revenue revenue;

    public double revenueToDisplay, downloadToDisplay;

    public String dateToDisplay;

    public double getRevenueToDisplay() {
        if (revenueToDisplay == 0) {
            if (revenue == null || revenue.product == null) {
                return 0;
            }
            revenueToDisplay = revenue.product.downloads + revenue.product.refunds;
        }
        return revenueToDisplay;
    }

    public void setRevenueToDisplay(double revenueToDisplay) {
        this.revenueToDisplay = revenueToDisplay;
    }

    public double getDownloadsToDisplay() {
        if (downloadToDisplay == 0) {
            if (units == null || units.product == null) {
                return 0;
            }
            downloadToDisplay = units.product.downloads;
        }
        return downloadToDisplay;
    }

    public void setDownloadToDisplay(double downloadToDisplay) {
        this.downloadToDisplay = downloadToDisplay;
    }

    public String getDateToDisplay() {
        return dateToDisplay;
    }

    public void setDateToDisplay(String dateToDisplay) {
        this.dateToDisplay = dateToDisplay;
    }
}
