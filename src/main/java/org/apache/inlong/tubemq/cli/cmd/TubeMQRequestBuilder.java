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

import okhttp3.HttpUrl;
import okhttp3.Request;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TubeMQRequestBuilder {

    private final HttpUrl.Builder httpUrl;

    private final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();

    public TubeMQRequestBuilder() {
        if (TubeMQHttpClient.getInstance().getHostAddr().startsWith("http")) {
            httpUrl =
                    Objects.requireNonNull(HttpUrl.parse(TubeMQHttpClient.getInstance().getHostAddr() + "/webapi.htm"))
                            .newBuilder();
        } else {
            httpUrl = Objects
                    .requireNonNull(
                            HttpUrl.parse("http://" + TubeMQHttpClient.getInstance().getHostAddr() + "/webapi.htm"))
                    .newBuilder();
        }
    }

    public Request buildRequest() {
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            httpUrl.addQueryParameter(entry.getKey(), entry.getValue());
        }
        return new Request.Builder().url(Objects.requireNonNull(httpUrl.build().url())).build();
    }

    public TubeMQRequestBuilder setType(String type) {
        parameters.put("type", type);
        return this;
    }

    public TubeMQRequestBuilder setMethod(String method) {
        parameters.put("method", method);
        return this;
    }

    public TubeMQRequestBuilder setKey() {
        parameters.put("confModAuthToken", TubeMQHttpClient.getInstance().getKey());
        return this;
    }

    public TubeMQRequestBuilder setParamsIfNotEmpty(String key, String value) {
        if (!StringUtils.isBlank(value)) {
            parameters.put(key, value);
        }
        return this;
    }

    public TubeMQRequestBuilder setParamsMap(List<String> paramsMap) {
        for (String entry : paramsMap) {
            String[] pair = entry.split("=");
            if (pair.length != 2) {
                continue;
            }
            parameters.put(pair[0], pair[1]);
        }
        return this;
    }
}
