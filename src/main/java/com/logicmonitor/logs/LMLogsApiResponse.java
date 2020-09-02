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
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import com.logicmonitor.logs.invoker.ApiResponse;

/**
 * ApiResponse wrapper using case-insensitive map for the headers.
 */
public class LMLogsApiResponse<T> extends ApiResponse<T> {

    /**
     * Construct an instance using ApiResponse.
     * @param response API response object
     */
    LMLogsApiResponse(ApiResponse<T> response) {
        super(response.getStatusCode(), new TreeMap<>(String.CASE_INSENSITIVE_ORDER),
                response.getData());
        if (response.getHeaders() != null) {
            getHeaders().putAll(response.getHeaders());
        }
    }

    /**
     * Constructs an instance.
     * @param statusCode HTTP response status
     * @param headers HTTP response headers
     * @param body HTTP response body
     */
    LMLogsApiResponse(int statusCode, Map<String, List<String>> headers, T body) {
        super(statusCode, new TreeMap<>(String.CASE_INSENSITIVE_ORDER), body);
        if (headers != null) {
            getHeaders().putAll(headers);
        }
    }

    /**
     * Gets the request ID from {@value LMLogsApi#REQUEST_ID_HEADER} header.
     * @return request ID
     */
    public String getRequestId() {
        return Optional.ofNullable(getHeaders().get(LMLogsApi.REQUEST_ID_HEADER))
            .filter(values -> !values.isEmpty())
            .map(values -> values.get(0))
            .orElse(null);
    }

}
