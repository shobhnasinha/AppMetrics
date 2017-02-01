package metrics.app.com.appmetrics.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CountryList implements Serializable {

    public ArrayList<Country> country_list;

    public ArrayList<Country> getCountryList() {
        return country_list;
    }
}
