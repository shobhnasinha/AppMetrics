package metrics.app.com.appmetrics;

import org.junit.Before;
import metrics.app.com.appmetrics.api.ApiProvider;
import metrics.app.com.appmetrics.api.MockApiEndpoint;

public class BaseTest {

    @Before
    public void setup() {
        ApiProvider.getInstance().setMockApiEndpoint(new MockApiEndpoint());
    }
}
