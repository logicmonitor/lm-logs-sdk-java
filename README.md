# lm-logs-sdk-java(beta)

Logic Monitor Log Ingestion Java SDK.


## Requirements

Building the API client library requires:

1. Java 9+
2. Maven/Gradle

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>com.logicmonitor</groupId>
  <artifactId>lm-logs-sdk-java</artifactId>
  <version>1.1</version>
  <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "com.logicmonitor:lm-logs-sdk-java:1.1"
```

### Others

At first generate the JAR by executing:

```shell
mvn clean package
```

Then manually install the following JARs:

- `target/lm-logs-sdk-java-1.1.jar`
- `target/lib/*.jar`

## Getting Started

```java
import java.util.Arrays;
import java.util.List;
import com.logicmonitor.logs.LMLogsApi;
import com.logicmonitor.logs.LMLogsApiException;
import com.logicmonitor.logs.LMLogsApiResponse;
import com.logicmonitor.logs.model.LogEntry;

public class LogIngestApiExample {

    public static void main(String[] args) {
        LMLogsApi apiInstance = new LMLogsApi.Builder()
            .withCompany("your_company")
            .withAccessId("LM_access_id")
            .withAccessKey("LM_token_id")
            .build();

        LogEntry entry = new LogEntry()
            .message("log_message")
            .putLmResourceIdItem("resource_id_key", "resource_id_value");
        List<LogEntry> logEntries = Arrays.asList(entry);

        LMLogsApiResponse<?> response;
        try {
            response = apiInstance.logIngestPostWithHttpInfo(logEntries);
        } catch (LMLogsApiException e) {
            e.printStackTrace();
            response = e.getResponse();
        }

        System.out.println("Request ID: " + response.getRequestId());
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Body: " + response.getData());
    }
}

```

## Documentation for Models

 - [LogEntry](docs/LogEntry.md)
 - [LogResponse](docs/LogResponse.md)

