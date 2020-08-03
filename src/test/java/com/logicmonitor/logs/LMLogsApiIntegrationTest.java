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

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;
import com.logicmonitor.auth.LMv1TokenGenerator;
import com.logicmonitor.logs.invoker.ApiException;
import com.logicmonitor.logs.invoker.ApiResponse;
import com.logicmonitor.logs.invoker.ServerConfiguration;
import com.logicmonitor.logs.model.LogEntry;
import com.logicmonitor.logs.model.LogResponse;

public class LMLogsApiIntegrationTest extends JerseyTest {

    protected static final String TEST_ID = "testId";
    protected static final String TEST_KEY = "testKey";
    protected static final Pattern AUTH_PATTERN = Pattern.compile("\\w+\\s\\w+:[^:]+:(\\d+)");
    protected static final String TEST_REQUEST_ID = "testRequestId";

    @Path("/rest")
    public static class LogIngestResource {
        @Path("/log/ingest")
        @POST
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public Response doPost(
                @HeaderParam("X-Version") String version,
                @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization,
                String payload) {

            if (version == null) {
                return error(Status.BAD_REQUEST, "Missing version");
            }
            if (!LMLogsApi.API_VERSION.toString().equals(version)) {
                return error(Status.BAD_REQUEST, "Invalid version");
            }

            if (authorization == null) {
                return error(Status.UNAUTHORIZED, "Missing authorization");
            }
            Matcher matcher = AUTH_PATTERN.matcher(authorization);
            if (!matcher.matches()) {
                return error(Status.UNAUTHORIZED, "Invalid authorization format");
            }
            String token = LMv1TokenGenerator.generate(TEST_ID, TEST_KEY, "POST", payload,
                    "/log/ingest", Long.parseLong(matcher.group(1)));
            if (!token.equals(authorization)) {
                return error(Status.UNAUTHORIZED, "Invalid authorization");
            }

            return Response
                .status(Status.ACCEPTED)
                .entity(new LogResponse())
                .header(LMLogsApi.REQUEST_ID_HEADER, TEST_REQUEST_ID)
                .build();
        }
    }

    protected static Response error(Response.Status status, String message) {
        return Response
            .status(status)
            .entity(new LogResponse()
                .success(false)
                .message(message))
            .build();
    }

    private LMLogsApi api = new LMLogsApi("testCompany", TEST_ID, TEST_KEY);

    @Override
    protected Application configure() {
        forceSet(TestProperties.CONTAINER_PORT, "0");
        return new ResourceConfig(LogIngestResource.class);
    }

    @Before
    public void overrideClientBaseUrl() {
        URI testBaseUrl = getBaseUri().resolve(
                URI.create(api.getApiClient().getBasePath()).getPath());
        api.getApiClient().setServers(List.of(
                new ServerConfiguration(testBaseUrl.toString(), null, Map.of())));
    }

    @Test
    public void testApiCall() throws ApiException {
        assertDoesNotThrow(() -> api.logIngestPostWithHttpInfo(List.of(new LogEntry())));
        ApiResponse<LogResponse> response = api.logIngestPostWithHttpInfo(List.of(new LogEntry()));
        assertAll(
            () -> assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatusCode()),
            () -> assertEquals(List.of(TEST_REQUEST_ID),
                    response.getHeaders().get(LMLogsApi.REQUEST_ID_HEADER)),
            () -> assertNotNull(response.getData())
        );
    }

    @Test
    public void testNullEntries() throws ApiException {
        assertThrows(ApiException.class, () -> api.logIngestPostWithHttpInfo(null));
        try {
            api.logIngestPostWithHttpInfo(null);
        } catch (ApiException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getCode());
        }
    }

}
