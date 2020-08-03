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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import java.io.IOException;
import java.net.URI;
import java.util.Set;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import com.logicmonitor.logs.invoker.JSON;
import com.logicmonitor.logs.model.LogEntry;

public class LMv1AuthenticationFilterTest {

    @ParameterizedTest
    @CsvSource({
        ",         id,    key",
        "/path,    ,      key",
        "/path,    id,       ",
    })
    public void testInvalidConstructorParameters(String basePath, String accessId, String accessKey) {
        assertThrows(NullPointerException.class,
                () -> new LMv1AuthenticationFilter(basePath, accessId, accessKey));
    }

    @ParameterizedTest
    @CsvSource({
        "/foo,        http://test.com/foo",
        "/foo,        http://test.com/foo/bar",
        "/,           http://test.com/",
        "/,           http://test.com/foo/bar",
        "'',          http://test.com",
        "'',          http://test.com/foo/bar",
    })
    public void testAddAuthorizationHeader(String basePath, URI requestUri) throws IOException {
        LMv1AuthenticationFilter filter = new LMv1AuthenticationFilter(basePath, "id", "key");
        ClientRequestContext request = getRequestContext(requestUri);
        filter.filter(request);
        assertNotNull(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
    }

    @ParameterizedTest
    @CsvSource({
        "/foo,        http://test.com/",
        "/foo,        http://test.com/bar/foo",
        "/,           http://test.com",
        "/foo/bar,    http://test.com/foo",
    })
    public void testInvalidRequestPath(String basePath, URI requestUri) {
        LMv1AuthenticationFilter filter = new LMv1AuthenticationFilter(basePath, "id", "key");
        ClientRequestContext request = getRequestContext(requestUri);
        assertThrows(IOException.class, () -> filter.filter(request));
    }

    @Test
    public void testGetEntityAsString() throws IOException {
        MockConfiguration config = spy(MockConfiguration.class);
        config.setInstances(Set.of(new LoggingFeature(), new Object(), new JSON()));
        LogEntry entity = new LogEntry();
        String entityString = LMv1AuthenticationFilter.getEntityAsString(config, entity);
        assertEquals(new JSON().getContext(entity.getClass()).writeValueAsString(entity),
                entityString);
    }

    @Test
    public void testMissingObjectMapper() {
        MockConfiguration config = spy(MockConfiguration.class);
        config.setInstances(Set.of(new LoggingFeature(), new Object()));
        assertThrows(IOException.class,
                () -> LMv1AuthenticationFilter.getEntityAsString(config, new LogEntry()));
    }

    protected static ClientRequestContext getRequestContext(URI requestUri) {
        MockConfiguration config = spy(MockConfiguration.class);
        config.setInstances(Set.of(new JSON()));

        MockClientRequestContext request = spy(MockClientRequestContext.class);
        request.setUri(requestUri);
        request.setMethod("POST");
        request.setHeaders(new MultivaluedHashMap<>());
        request.setConfiguration(config);
        request.setEntity(new LogEntry());
        return request;
    }

}
