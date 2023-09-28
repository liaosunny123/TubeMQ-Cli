/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.inlong.tubemq.cli.cmd;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TubeMQHttpClient {

    private final String _key;

    private final String _hostAddr;

    private final OkHttpClient client = new OkHttpClient();

    private final Gson gson = new Gson();

    @lombok.Getter
    private static boolean isRegisterInformation = false;

    private TubeMQHttpClient(String key, String addr) {
        _key = key;
        _hostAddr = addr;
        isRegisterInformation = true;
    }

    @lombok.Getter
    private static TubeMQHttpClient instance = null;

    public static synchronized TubeMQHttpClient getInstance(String key, String addr) {
        if (instance == null) {
            instance = new TubeMQHttpClient(key, addr);
        }

        return instance;
    }

    public String getHostAddr() {
        return _hostAddr;
    }

    public String getKey() {
        return _key;
    }

    public <T> T execute(Request request, Class<T> clazz) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            String responseBody;
            if (response.body() != null) {
                responseBody = response.body().string();
                return gson.fromJson(responseBody, clazz);
            }
            return null;
        }
    }
}
