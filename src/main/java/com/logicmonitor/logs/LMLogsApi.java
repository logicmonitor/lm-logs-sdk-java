/*
 * Copyright (C) 2020 LogicMonitor, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.logicmonitor.logs;

import java.util.List;
import com.logicmonitor.logs.api.LogIngestApi;
import com.logicmonitor.logs.invoker.ApiException;
import com.logicmonitor.logs.invoker.ApiResponse;
import com.logicmonitor.logs.model.LogEntry;
import com.logicmonitor.logs.model.LogResponse;

/**
 * Implementation of LogicMonitor Logs API.
 */
public class LMLogsApi extends LogIngestApi {

    /**
     * API version.
     */
    public static final Integer API_VERSION = 2;
    /**
     * Name of the response header containing request ID.
     */
    public static final String REQUEST_ID_HEADER = "X-Request-ID";

    /**
     * Initializes LMLogsClient instance with the default company name.
     * @param accessId LogicMonitor access ID.
     * @param accessKey LogicMonitor access key.
     * @throws NullPointerException if any of the parameters is null.
     */
    public LMLogsApi(String accessId, String accessKey) {
        super(new LMLogsClient(accessId, accessKey));
    }

    /**
     * Initializes LMLogsClient instance.
     * @param company company name.
     * @param accessId LogicMonitor access ID.
     * @param accessKey LogicMonitor access key.
     * @throws NullPointerException when accessId or accessKey is null.
     */
    public LMLogsApi(String company, String accessId, String accessKey) {
        this(accessId, accessKey);
        getApiClient().setCompany(company);
    }

    /**
     * Gets the LogicMonitor Logs client instance.
     * @return LogIngestApi.
     */
    @Override
    public LMLogsClient getApiClient() {
        return (LMLogsClient) super.getApiClient();
    }

    /**
     * Send custom logs to your LogicMonitor account.
     * @param logEntry  (required)
     * @return LogResponse
     * @throws ApiException if fails to make API call
     * @http.response.details
       <table border="1">
         <caption>Response Details</caption>
         <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
         <tr><td> 202 </td><td> The request has been accepted for processing, but the processing has not been completed. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 401 </td><td> TAuthentication failed. The API key provided in not valid. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 402 </td><td> The account is not registered as a customer. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 403 </td><td> The API key doesn’t have permissions to perform the request. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 413 </td><td> The maximum content size per payload is 8 MB. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 422 </td><td> The request cannot be processed. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 429 </td><td> The number of requests exceeds the rate limit. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 500 </td><td> Something went wrong on LogicMonitor’s end. </td><td>  * X-Request-ID -  <br>  </td></tr>
       </table>
     */
    public LogResponse logIngestPost(List<LogEntry> logEntry) throws ApiException {
        return super.logIngestPost(API_VERSION, logEntry);
    }

    /**
     * Send custom logs to your LogicMonitor account.
     * @param logEntry  (required)
     * @return ApiResponse&lt;LogResponse&gt;
     * @throws ApiException if fails to make API call
     * @http.response.details
       <table border="1">
         <caption>Response Details</caption>
         <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
         <tr><td> 202 </td><td> The request has been accepted for processing, but the processing has not been completed. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 401 </td><td> TAuthentication failed. The API key provided in not valid. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 402 </td><td> The account is not registered as a customer. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 403 </td><td> The API key doesn’t have permissions to perform the request. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 413 </td><td> The maximum content size per payload is 8 MB. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 422 </td><td> The request cannot be processed. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 429 </td><td> The number of requests exceeds the rate limit. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 500 </td><td> Something went wrong on LogicMonitor’s end. </td><td>  * X-Request-ID -  <br>  </td></tr>
       </table>
     */
    public ApiResponse<LogResponse> logIngestPostWithHttpInfo(List<LogEntry> logEntry)
            throws ApiException {
        return super.logIngestPostWithHttpInfo(API_VERSION, logEntry);
    }

}
