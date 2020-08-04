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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LMv1TokenGeneratorTest {

    @ParameterizedTest
    @CsvSource({
        ",      key,    post,    payload,    /path",
        "id,    ,       post,    payload,    /path",
        "id,    key,    ,        payload,    /path",
        "id,    key,    post,    ,           /path",
        "id,    key,    post,    payload,         ",
    })
    public void testInvalidGenerateParameters(String accessId, String accessKey, String httpMethod,
            String payload, String resourcePath) {
        assertThrows(NullPointerException.class,
                () -> LMv1TokenGenerator.generate(accessId, accessKey, httpMethod, payload, resourcePath, 0));
    }

    @Test
    public void testGenerate() throws Exception {
        String token = LMv1TokenGenerator.generate("id", "key", "post", "some text", "/path", 12345);
        assertEquals(
                "LMv1 id:MjlkODUxOTBhNzljYzBkMzBhOWI4ZjNlMGZiMmViYWMzMjA2NjhkZjgyOTY1MDZjNmU3MjA2MjY1ZTE4YzI2NQ==:12345",
                token);
    }

}
