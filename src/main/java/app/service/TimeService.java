package app.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.TimeZone;

@Service
public class TimeService {

    private TimeZone timezone;

    public TimeService(Environment environment) {
        this.timezone = TimeZone.getTimeZone(environment.getProperty("app.TimeService.timezone"));
    }

    public TimeZone getTimezone() {
        return timezone;
    }

    public Date now() {
        return new Date();
    }
}
