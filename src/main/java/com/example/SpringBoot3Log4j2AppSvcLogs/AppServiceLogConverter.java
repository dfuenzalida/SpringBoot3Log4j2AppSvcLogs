package com.example.SpringBoot3Log4j2AppSvcLogs;

import java.util.Base64;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

@Plugin(name = "appsvc", category = PatternConverter.CATEGORY)
@ConverterKeys({"appsvc", "appSvc","appService"})
public class AppServiceLogConverter extends LogEventPatternConverter {

    private final Base64.Encoder encoder;

    public static AppServiceLogConverter newInstance(final Configuration config, final String[] options) {
        return new AppServiceLogConverter(options);
    }

    private AppServiceLogConverter(final String[] options) {
        super("AppServiceLog", "appsvc");
        this.encoder = Base64.getEncoder();
    }

    @Override
    public void format(final LogEvent event, final StringBuilder buffer) {
        String eventLevelName = getAppSvcLevelName(event.getLevel().name());
        String message = event.getMessage().getFormattedMessage();

        if (event.getThrown() != null) {
            message += event.getThrown().getCause();
        }

        String encoded = encoder.encodeToString(message.getBytes());
        buffer.append(String.format("x-ms-applog:%s:base64:%s", eventLevelName, encoded));
    }

    // Convert log4j2 log levels to App Service log levels
    public String getAppSvcLevelName(String level) {
        switch (level) {
            case "FATAL":
                return "critical";
            case "ERROR":
                return "error";
            case "WARN":
                return "warning";
            default:
                return "informational";
        }
    }
}