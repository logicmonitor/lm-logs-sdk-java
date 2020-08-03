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

import java.io.IOException;
import java.util.Objects;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Filter adding LMv1 authentication to the requests.
 */
@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class LMv1AuthenticationFilter implements ClientRequestFilter {

    /**
     * Base path of the service.
     */
    protected final String basePath;
    /**
     * LogicMonitor access ID.
     */
    protected final String accessId;
    /**
     * LogicMonitor access key.
     */
    protected final String accessKey;

    /**
     * Initializes LMv1AuthenticationFilter instance.
     * @param basePath base path of the service.
     * @param accessId LogicMonitor access ID.
     * @param accessKey LogicMonitor access key.
     * @throws NullPointerException if any of the parameters is null.
     */
    public LMv1AuthenticationFilter(String basePath, String accessId, String accessKey) {
        this.basePath = Objects.requireNonNull(basePath, "Base path must not be null");
        this.accessId = Objects.requireNonNull(accessId, "Access id must not be null");
        this.accessKey = Objects.requireNonNull(accessKey, "Access key must not be null");
    }

    /**
     * Adds 'Authorization' header with the LMv1 token to the request.
     * @param requestContext request context.
     * @throws IOException if an I/O exception occurs.
     */
    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        String requestPath = requestContext.getUri().getPath();
        if (!requestPath.startsWith(basePath)) {
            throw new IOException("Invalid request path '" + requestPath + "'");
        }

        String payload = getEntityAsString(requestContext.getConfiguration(), requestContext.getEntity());
        String lmToken = LMv1TokenGenerator.generate(
                accessId,
                accessKey,
                requestContext.getMethod(),
                payload,
                requestPath.substring(basePath.length()),
                System.currentTimeMillis());
        requestContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, lmToken);
    }

    /**
     * Converts the request entity to string using object mapper from the configuration.
     * @param config request configuration.
     * @param entity request entity.
     * @return The entity serialized to String.
     * @throws IOException if object mapper can't be found in the configuration.
     * @throws JsonProcessingException (unchecked) if the entity is invalid.
     */
    protected static String getEntityAsString(Configuration config, Object entity)
            throws IOException, JsonProcessingException {

        @SuppressWarnings("unchecked")
        ObjectMapper mapper = config.getInstances().stream()
            .filter(ContextResolver.class::isInstance)
            .map(ContextResolver.class::cast)
            .map(resolver -> resolver.getContext(entity.getClass()))
            .filter(ObjectMapper.class::isInstance)
            .findAny()
            .map(ObjectMapper.class::cast)
            .orElseThrow(() -> new IOException(
                    "No ObjectMapper found for type " + entity.getClass().getName()));
        return mapper.writeValueAsString(entity);
    }

}
