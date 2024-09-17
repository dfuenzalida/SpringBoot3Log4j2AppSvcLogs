package com.example.SpringBoot3Log4j2AppSvcLogs;

import java.util.Base64;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.LayoutBase;

public class AppServiceLogbackLayout extends LayoutBase<ILoggingEvent> {

    private final Base64.Encoder encoder = Base64.getEncoder();

    public String doLayout(ILoggingEvent event) {
        StringBuilder buffer = new StringBuilder();
        String eventLevelName = getAppSvcLevelName(event.getLevel().toString());
        String message = event.getFormattedMessage();

        // Encode exceptions
        if (event.getThrowableProxy() != null) {
            StackTraceElementProxy[] stacktrace = event.getThrowableProxy().getStackTraceElementProxyArray();
            for (StackTraceElementProxy element: stacktrace) {
                message += element.getSTEAsString() + "\n";
            }
        }

        String encoded = encoder.encodeToString(message.getBytes());
        buffer.append(String.format("x-ms-applog:%s:base64:%s\n", eventLevelName, encoded));
        return buffer.toString();
    }

    // Logback levels are: ERROR, WARN, INFO, DEBUG and TRACE
    public String getAppSvcLevelName(String level) {
        switch (level) {
            case "ERROR":
                return "error";
            case "WARN":
                return "warning";
            default:
                return "informational";
        }
    }
}
