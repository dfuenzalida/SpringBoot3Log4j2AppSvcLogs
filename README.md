# Example Spring Boot 3 webapp with Log4j2 and logging to App Service Logs

This is an example Spring Boot 3 webapp which uses Log4j2 for logging, configured in a way such that logs are sent to the `AppServiceAppLogs` in Log Analytics.

## Usage

Build the webapp using Maven with `mvn clean package` and deploy the resulting file `target/app.jar` to a web application in Azure App Service using Java 17 or later as the runtime.

Once the applications is up and running, configure Diagnostic Settings in Azure Portal for the logs to be sent to a Log Analytics Workspace.

Make sure to add the **App Service Application Logs** category to the Diagnostic Settings configuration.

Visit the `/`, `/warn` and `/error` endpoints of the web app so some logs are emitted. Then, run the following query in Azure Portal in the Logs blade:

```kql
AppServiceAppLogs
| where TimeGenerated > ago(30m)
| order by TimeGenerated desc
```

![AppServiceAppLogs screenshot](https://raw.githubusercontent.com/dfuenzalida/SpringBoot3Log4j2AppSvcLogs/main/DiagnosticLogs.png "AppServiceAppLogs screenshot")


## Code outline

* `SpringBoot3Log4j2AppSvcLogsApplication.java`: The main class is also a Controller with 3 endpoints: `/` for a regular messsage, `/warn` which logs a warning message and `/error` which throws an Exception
* `MyControllerAdvice.java`: Takes unhandled exceptions from any controllers, captures their stack trace and logs the result as an error.
* `AppServiceLogConverter.java`: Implements a Log4j2 pattern so that logs appenders that use the pattern defined by this class will take a log event and perform the conversion to Base64 and formatting required for these events to be published to the AppServiceAppLogs in Log Analytics.
* `log4j2-spring.xml` is the Log4j2 configuration file, it uses the special Log appender defined in `AppServiceLogConverter`.