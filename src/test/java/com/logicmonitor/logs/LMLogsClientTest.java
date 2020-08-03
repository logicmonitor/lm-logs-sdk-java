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
import javax.ws.rs.core.Configuration;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@TestMethodOrder(OrderAnnotation.class)
public class LMLogsClientTest {

    private LMLogsClient client = new LMLogsClient("id", "key");

    @Test
    @Order(1)
    public void testConstructor() {
        assertAll(
            () -> assertNotNull(client.getBasePath()),
            () -> assertFalse(client.getServers().isEmpty()),
            () -> assertNotNull(client.getHttpClient()),
            () -> assertNotNull(client.getJSON()),
            () -> assertFalse(client.isDebugging()),
            () -> assertNotNull(client.authFilter),
            () -> assertEquals(LMLogsClient.DEFAULT_TIMEOUT, client.getConnectTimeout()),
            () -> assertEquals(LMLogsClient.DEFAULT_TIMEOUT, client.getReadTimeout()),
            () -> assertNull(client.getCompany())
        );
    }

    @ParameterizedTest
    @CsvSource({
        ",      key",
        "id,       ",
    })
    public void testInvalidConstructorParameters(String accessId, String accessKey) {
        assertThrows(NullPointerException.class, () -> new LMLogsClient(accessId, accessKey));
    }

    @ParameterizedTest
    @ValueSource(strings = {"company"})
    @NullAndEmptySource
    public void testSetCompany(String companyName) {
        client.setCompany(companyName);
        assertEquals(companyName, client.getCompany());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, Integer.MAX_VALUE})
    public void testSetConnectTimeout(int timeout) {
        client.setConnectTimeout(timeout);
        assertEquals(timeout, client.getConnectTimeout());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, Integer.MAX_VALUE})
    public void testSetReadTimeout(int timeout) {
        client.setReadTimeout(timeout);
        assertEquals(timeout, client.getReadTimeout());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void testSetDebugging(boolean debugging) {
        client.setDebugging(debugging);
        assertEquals(debugging, client.isDebugging());
    }

    @AfterEach
    protected void testConfigConsistency() {
        Configuration config = client.getHttpClient().getConfiguration();
        assertNotNull(config);
        assertAll(
            () -> assertTrue(config.isRegistered(client.getJSON())),
            () -> assertTrue(config.isRegistered(client.authFilter)),
            () -> assertEquals(client.isDebugging(), config.isRegistered(LoggingFeature.class)),
            () -> assertEquals(client.getConnectTimeout(), config.getProperty(ClientProperties.CONNECT_TIMEOUT)),
            () -> assertEquals(client.getReadTimeout(), config.getProperty(ClientProperties.READ_TIMEOUT)),
            () -> {
                if (client.getCompany() == null) {
                    assertNull(client.getServerVariables());
                } else {
                    assertNotNull(client.getServerVariables());
                    assertEquals(client.getCompany(),
                            client.getServerVariables().get(LMLogsClient.COMPANY_VARIABLE));
                }
            }
        );
    }

}
