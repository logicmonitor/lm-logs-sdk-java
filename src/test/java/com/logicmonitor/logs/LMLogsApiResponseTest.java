package com.logicmonitor.logs;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.logicmonitor.logs.invoker.ApiResponse;
import com.logicmonitor.logs.model.LogResponse;

public class LMLogsApiResponseTest {

    @ParameterizedTest
    @ValueSource(strings = {
        LMLogsApi.REQUEST_ID_HEADER,
        "x-request-id",
        "X-REQUEST-ID",
    })
    public void testGetRequestId(String headerName) {
        final String requestId = "123-456-789";
        ApiResponse<LogResponse> response = new ApiResponse<>(200, Map.of(headerName,
                List.of(requestId)));
        LMLogsApiResponse<LogResponse> lmResponse = new LMLogsApiResponse<>(response);
        assertEquals(requestId, lmResponse.getRequestId());
    }

    @Test
    public void testMissingRequestId() {
        ApiResponse<LogResponse> response = new ApiResponse<>(200, null);
        LMLogsApiResponse<LogResponse> lmResponse = new LMLogsApiResponse<>(response);
        assertNull(lmResponse.getRequestId());
    }

}
