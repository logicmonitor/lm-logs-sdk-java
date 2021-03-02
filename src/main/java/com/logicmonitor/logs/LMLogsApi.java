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
    private LMLogsApi(String accessId, String accessKey) {
        super(new LMLogsClient(accessId, accessKey));
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
     * @param logEntry list of the log entries
     * @return LogResponse
     * @throws LMLogsApiException if fails to make API call
     * @http.response.details
       <table border="1">
         <caption>Response Details</caption>
         <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
         <tr><td> 202 </td><td> The request has been accepted for processing, but the processing has not been completed </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 207 </td><td> Some events in a batch get rejected. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 400 </td><td> Either bad format or request failed to begin processing in some form. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 401 </td><td> The access token is invalid. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 402 </td><td> The customer is not registered in the Admin App as a paying customer. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 403 </td><td> No appropriate RBAC role is assigned. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 413 </td><td> The payload exceeded 8MB. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 429 </td><td> Too many requests. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 500 </td><td> Server error. </td><td>  * X-Request-ID -  <br>  </td></tr>
       </table>
     */
    public LogResponse logIngestPost(List<LogEntry> logEntry) throws LMLogsApiException {
        try {
            return super.logIngestPost(API_VERSION, logEntry);
        } catch (ApiException e) {
            throw new LMLogsApiException(e);
        }
    }

    /**
     * Send custom logs to your LogicMonitor account.
     * @param logEntry list of the log entries
     * @return LMLogsApiResponse&lt;LogResponse&gt;
     * @throws LMLogsApiException if fails to make API call
     * @http.response.details
       <table border="1">
         <caption>Response Details</caption>
         <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
         <tr><td> 202 </td><td> The request has been accepted for processing, but the processing has not been completed </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 207 </td><td> Some events in a batch get rejected. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 400 </td><td> Either bad format or request failed to begin processing in some form. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 401 </td><td> The access token is invalid. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 402 </td><td> The customer is not registered in the Admin App as a paying customer. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 403 </td><td> No appropriate RBAC role is assigned. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 413 </td><td> The payload exceeded 8MB. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 429 </td><td> Too many requests. </td><td>  * X-Request-ID -  <br>  </td></tr>
         <tr><td> 500 </td><td> Server error. </td><td>  * X-Request-ID -  <br>  </td></tr>
       </table>
     */
    public LMLogsApiResponse<LogResponse> logIngestPostWithHttpInfo(List<LogEntry> logEntry)
            throws LMLogsApiException {
        try {
            return new LMLogsApiResponse<>(super.logIngestPostWithHttpInfo(API_VERSION, logEntry));
        } catch (ApiException e) {
            throw new LMLogsApiException(e);
        }
    }

    /**
     * A builder for creating LogicMonitor Logs API instances.
     */
    public static class Builder {

        /**
         * Company name.
         */
        private String company;
        /**
         * LogicMonitor access ID.
         */
        private String accessId;
        /**
         * LogicMonitor access key.
         */
        private String accessKey;
        /**
         * Connection timeout.
         */
        private Integer connectTimeout;
        /**
         * Read timeout.
         */
        private Integer readTimeout;
        /**
         * HTTP client debugging flag.
         */
        private Boolean debugging;
        /**
         * User-Agent header.
         */
        private String userAgentHeader;

        /**
         * Configures the company.
         * @param company
         * @return this builder object
         */
        public Builder withCompany(String company) {
            this.company = company;
            return this;
        }

        /**
         * Configures LogicMonitor access ID.
         * @param accessId
         * @return this builder object
         */
        public Builder withAccessId(String accessId) {
            this.accessId = accessId;
            return this;
        }

        /**
         * Configures LogicMonitor access key.
         * @param accessKey
         * @return this builder object
         */
        public Builder withAccessKey(String accessKey) {
            this.accessKey = accessKey;
            return this;
        }

        /**
         * Configures connection timeout.
         * @param connectTimeout
         * @return this builder object
         */
        public Builder withConnectTimeout(Integer connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        /**
         * Configures read timeout.
         * @param readTimeout
         * @return this builder object
         */
        public Builder withReadTimeout(Integer readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        /**
         * Configures HTTP client debugging.
         * @param debugging
         * @return this builder object
         */
        public Builder withDebugging(Boolean debugging) {
            this.debugging = debugging;
            return this;
        }

        /**
         * Configures user agent header.
         * @param userAgentHeader
         * @return this builder object
         */
        public Builder withUserAgentHeader(String userAgentHeader) {
            this.userAgentHeader = userAgentHeader;
            return this;
        }
        /**
         * Returns a newly-created LMLogsApi based on the contents of the builder.
         * @return new LMLogsApi instance
         * @throws NullPointerException if accessId or accessKey is null.
         */
        public LMLogsApi build() {
            LMLogsApi api = new LMLogsApi(accessId, accessKey);
            LMLogsClient client = api.getApiClient();
            if (company != null) {
                client.setCompany(company);
            }
            if (connectTimeout != null) {
                client.setConnectTimeout(connectTimeout);
            }
            if (readTimeout != null) {
                client.setReadTimeout(readTimeout);
            }
            if (debugging != null) {
                client.setDebugging(debugging);
            }
            if (userAgentHeader != null) {
                client.setUserAgent(userAgentHeader);
            }
            return api;
        }
    }

}
