package app.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.env.MockEnvironment;

import java.util.Date;

public class TestTimeService {

    @Test
    public void testTimeZone() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty("app.TimeService.timezone", "Europe/Paris");

        TimeService timeService = new TimeService(environment);
        Date now = timeService.now();
        Assert.assertNotNull(now);
        Assert.assertTrue(now.getTime() > 0);
    }
}
