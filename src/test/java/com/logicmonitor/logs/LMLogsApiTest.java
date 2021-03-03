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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LMLogsApiTest {

    @ParameterizedTest
    @CsvSource({
        ",      key",
        "id,       ",
    })
    public void testBuilderInvalidParameters(String accessId, String accessKey) {
        assertThrows(NullPointerException.class, () -> new LMLogsApi.Builder()
            .withAccessId(accessId)
            .withAccessKey(accessKey)
            .build());
    }

    @ParameterizedTest
    @CsvSource({
        ",           0,           1,           true ,       Agent/0.1",
        "company,    ,            99999999,    false,       Agent/0.1",
        "company,    1,           ,            true ,                ",
        "company,    99999999,    0,                ,                ",
    })
    public void testBuilder(String company, Integer connectTimeout, Integer readTimeout,
            Boolean debugging, String userAgentHeader) {
        LMLogsApi api = new LMLogsApi.Builder()
            .withAccessId("id")
            .withAccessKey("key")
            .withCompany(company)
            .withConnectTimeout(connectTimeout)
            .withReadTimeout(readTimeout)
            .withDebugging(debugging)
            .withUserAgentHeader(userAgentHeader)
            .build();
        LMLogsClient client = api.getApiClient();

        assertAll(
            () -> assertEquals(company, client.getCompany()),
            () -> assertEquals(connectTimeout != null ? connectTimeout : LMLogsClient.DEFAULT_TIMEOUT,
                    client.getConnectTimeout()),
            () -> assertEquals(readTimeout != null ? readTimeout : LMLogsClient.DEFAULT_TIMEOUT,
                    client.getReadTimeout()),
            () -> assertEquals(debugging != null ? debugging : false, client.isDebugging()),
            () -> assertEquals(userAgentHeader != null ? userAgentHeader : "lm-logs-sdk-java/1.2", client.getUserAgent())
        );
    }

}
