# lm-logs-sdk-java

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
  <version>1.0</version>
  <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "com.logicmonitor:lm-logs-sdk-java:1.0"
```

### Others

At first generate the JAR by executing:

```shell
mvn clean package
```

Then manually install the following JARs:

- `target/lm-logs-sdk-java-1.0.jar`
- `target/lib/*.jar`

## Getting Started

```java

import java.util.Arrays;
import java.util.List;
import com.logicmonitor.logs.LMLogsApi;
import com.logicmonitor.logs.invoker.ApiException;
import com.logicmonitor.logs.model.LogEntry;
import com.logicmonitor.logs.model.LogResponse;

public class LogIngestApiExample {

    public static void main(String[] args) {
        LMLogsApi apiInstance = new LMLogsApi("your_company", "LM_access_id", "LM_token_id");
        List<LogEntry> logEntry = Arrays.asList(); // List<LogEntry>
        try {
            LogResponse result = apiInstance.logIngestPost(logEntry);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling LogIngestApi#logIngestPost");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
}

```

## Documentation for Models

 - [LogEntry](docs/LogEntry.md)
 - [LogResponse](docs/LogResponse.md)

