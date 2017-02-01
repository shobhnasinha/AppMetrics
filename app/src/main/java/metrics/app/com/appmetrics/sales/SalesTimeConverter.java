package metrics.app.com.appmetrics.sales;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import metrics.app.com.appmetrics.Utils;
import metrics.app.com.appmetrics.model.SalesData;

/**
 * Converts and aggregates list of SalesData retrieved by endpoint. The aggregation is based on TimeRangeType.
 * It also provides utilities method to find time interval between dates based on app-wide standard formatter.
 */
class SalesTimeConverter {

    enum TimeRangeType {
        DAY, WEEK, MONTH, YEAR
    }

    /**
     * The method returns list of SalesData by aggregating salesDataList provided to it.
     * The aggregation is based on TimeRangeType
     *
     * @param salesDataList - The original list retrieved from endpoint.
     * @param startDateStr  - The start date for the requested sales list
     * @param endDateStr    - The end date for the requested sales list
     * @param timeRangeType - The aggregation is based on TimeRangeType
     * @return list of SalesData by aggregating salesDataList provided to it.
     */
    ArrayList<SalesData> getDataFromDailySales(ArrayList<SalesData> salesDataList, String startDateStr, String endDateStr, TimeRangeType timeRangeType) {
        ArrayList<SalesData> dailySalesDataList = new ArrayList<>();
        try {
            int size = getTimeRangeBetweenDates(startDateStr, endDateStr, timeRangeType) + 1;
            for (int i = 0; i < size; i++) {
                dailySalesDataList.add(new SalesData());
            }
            for (SalesData salesData : salesDataList) {
                int index = getTimeRangeBetweenDates(startDateStr, salesData.date, timeRangeType);
                if (index < 0) {
                    return null;
                }
                updateSalesDataList(dailySalesDataList, salesData, index);
            }
            for (int index = 0; index < dailySalesDataList.size(); index++) {
                if (timeRangeType == TimeRangeType.DAY) {
                    dailySalesDataList.get(index).setDateToDisplay(getDaysAfter(startDateStr, index));
                } else if (timeRangeType == TimeRangeType.WEEK) {
                    dailySalesDataList.get(index).setDateToDisplay(getWeeksAfter(startDateStr, endDateStr, index, (index == dailySalesDataList.size() - 1)));
                } else if (timeRangeType == TimeRangeType.MONTH) {
                    dailySalesDataList.get(index).setDateToDisplay(getMonthsAfter(startDateStr, index));
                } else if (timeRangeType == TimeRangeType.YEAR) {
                    dailySalesDataList.get(index).setDateToDisplay(getYearsAfter(startDateStr, index));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return dailySalesDataList;
    }

    private int getTimeRangeBetweenDates(String startDateStr, String endDateStr, TimeRangeType timeRangeType) throws ParseException {
        switch (timeRangeType) {
            case DAY:
                return getDaysBetween(getDate(startDateStr), getDate(endDateStr));
            case WEEK:
                return getWeeksBetween(getDate(startDateStr), getDate(endDateStr));
            case MONTH:
                return getMonthsBetween(getDate(startDateStr), getDate(endDateStr));
            case YEAR:
                return getYearsBetween(getDate(startDateStr), getDate(endDateStr));
            default:
                return -1;
        }
    }

    private void updateSalesDataList(ArrayList<SalesData> dailySalesDataList, SalesData salesData, int index) {
        SalesData currentData = dailySalesDataList.get(index);
        currentData.setDownloadToDisplay(currentData.getDownloadsToDisplay() + salesData.getDownloadsToDisplay());
        currentData.setRevenueToDisplay(currentData.getRevenueToDisplay() + salesData.getRevenueToDisplay());
        dailySalesDataList.set(index, currentData);
    }

    Date getDate(String strDate) throws ParseException {
        SimpleDateFormat formatter = Utils.getSimpleDateFormat();
        return formatter.parse(strDate);
    }

    int getDaysBetween(Date from, Date to) {
        return (int) ((to.getTime() - from.getTime()) / (1000 * 60 * 60 * 24));
    }

    private String getDaysAfter(String startDateStr, int index) throws ParseException {
        Date start = getDate(startDateStr);

        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE, index);
        return Utils.getSimpleDateFormat().format(c.getTime());
    }

    private String getWeeksAfter(String startDateStr, String endDateStr, int index, boolean isLast) throws ParseException {
        String from = getDaysAfter(startDateStr, index * 7);
        String to = isLast ? endDateStr : getDaysAfter(startDateStr, index * 7 + 6);
        return from + " - " + to;
    }

    private String getMonthsAfter(String startDateStr, int index) throws ParseException {
        Date start = getDate(startDateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.MONTH, index);

        return Utils.getSimpleMonthFormat().format(c.getTime());
    }

    private String getYearsAfter(String startDateStr, int index) throws ParseException {
        Date start = getDate(startDateStr);
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.YEAR, index);

        return Utils.getSimpleYearFormat().format(c.getTime());
    }

    int getWeeksBetween(Date from, Date to) {
        return getDaysBetween(from, to) / 7;
    }

    int getMonthsBetween(Date startDate, Date endDate) {
        int month1 = getMonth(startDate);
        int month2 = getMonth(endDate);
        if (month2 < month1) {
            month2 += 12;
        }
        return month2 - month1;
    }

    int getYearsBetween(Date startDate, Date endDate) {
        return getYear(endDate) - getYear(startDate);
    }

    int getDateNumber(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
}
