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

package com.logicmonitor.auth;

import java.util.Base64;
import java.util.Objects;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

/**
 * Generates LogicMonitor authentication tokens.
 */
public class LMv1TokenGenerator {

    /**
     * Name of the LogicMonitor authentication token.
     */
    public static final String TOKEN_NAME = "LMv1";

    /**
     * Generates LogicMonitor authentication token.
     * @param accessId LogicMonitor access ID.
     * @param accessKey LogicMonitor access key.
     * @param httpMethod request's HTTP method.
     * @param payload reuqest's payload.
     * @param resourcePath reuqest's resource path.
     * @param timestamp reuqest's timestamp (epoch).
     * @return LogicMonitor authentication token ('LMv1 accessId:digest:timestamp').
     * @throws NullPointerException if any of the parameters is null.
     */
    public static String generate(String accessId, String accessKey, String httpMethod, String payload,
            String resourcePath, long timestamp) {

        accessId = Objects.requireNonNull(accessId, "Access ID must not be null");
        accessKey = Objects.requireNonNull(accessKey, "Access key must not be null");
        httpMethod = Objects.requireNonNull(httpMethod, "HTTP method must not be null");
        payload = Objects.requireNonNull(payload, "Payload must not be null");
        resourcePath = Objects.requireNonNull(resourcePath, "Resource path must not be null");

        // METHOD + TIMESTAMP + PAYLOAD + RESOURCE PATH
        String value = httpMethod.toUpperCase() + timestamp + payload + resourcePath;

        byte[] digest = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, accessKey)
            .hmacHex(value)
            .getBytes();

        // 'LMv1' ID : BASE64(DIGEST) : TIMESTAMP
        return String.format("%s %s:%s:%d", TOKEN_NAME, accessId,
                Base64.getEncoder().encodeToString(digest), timestamp);
    }

}
