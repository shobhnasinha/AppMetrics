package metrics.app.com.appmetrics.model;

import java.io.Serializable;

public class Country implements Serializable {

    public String country_code, country_name;

    public String getCountryCode() {
        return country_code;
    }

    public String getCountryName() {
        return country_name;
    }
}
