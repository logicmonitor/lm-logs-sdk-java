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

import java.net.URI;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.client.ClientRequestFilter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import com.logicmonitor.auth.LMv1AuthenticationFilter;
import com.logicmonitor.logs.invoker.ApiClient;

/**
 * Implementation of LogicMonitor Logs client.
 */
public class LMLogsClient extends ApiClient {

    /**
     * Default connect and read timeout (10 seconds).
     */
    public static final int DEFAULT_TIMEOUT = (int) TimeUnit.SECONDS.toMillis(10);
    /**
     * Name of the company variable.
     */
    public static final String COMPANY_VARIABLE = "company";

    /**
     * Filter adding LMv1 authentication to the requests.
     */
    protected final ClientRequestFilter authFilter;
    /**
     * Company name.
     */
    protected String company;

    /**
     * Initializes LMLogsClient instance.
     * @param accessId LogicMonitor access ID.
     * @param accessKey LogicMonitor access key.
     * @throws NullPointerException if any of the parameters is null.
     */
    public LMLogsClient(String accessId, String accessKey) {
        authFilter = new LMv1AuthenticationFilter(URI.create(basePath).getPath(), accessId, accessKey);
        setConnectTimeout(DEFAULT_TIMEOUT);
        setReadTimeout(DEFAULT_TIMEOUT);
        httpClient = buildHttpClient(debugging);
    }

    /**
     * Sets the company in the target URL '{company}.logicmonitor.com'.
     * @param company company name.
     * @return itself.
     */
    public LMLogsClient setCompany(String company) {
        this.company = company;
        if (company == null) {
            setServerVariables(null);
        } else {
            setServerVariables(Collections.singletonMap(COMPANY_VARIABLE, company));
        }
        return this;
    }

    /**
     * Gets the company from the target URL '{company}.logicmonitor.com'.
     * @return company name.
     */
    public String getCompany() {
        return company;
    }

    /**
     * Performs additional configuration before HTTP client is built.
     */
    @Override
    protected void performAdditionalClientConfiguration(ClientConfig clientConfig) {
        clientConfig.property(ClientProperties.CONNECT_TIMEOUT, getConnectTimeout());
        clientConfig.property(ClientProperties.READ_TIMEOUT, getReadTimeout());
        if (authFilter != null) {
            clientConfig.register(authFilter);
        }
    }

    @Override
    public String toString() {
        return "LMLogsClient [basePath=" + getBasePath() + ", debugging=" + isDebugging()
                + ", connectTimeout=" + getConnectTimeout() + ", readTimeout=" + getReadTimeout() + "]";
    }

}
