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

import org.apache.inlong.tubemq.cli.model.topic.common.TopicCommonResult;

import okhttp3.Request;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

public class TubeMQRequestBuilderTest {

    private TubeMQRequestBuilder builder;

    @Before
    public void setUp() {
        TubeMQHttpClient.getInstance("abc", "127.0.0.1:8080");
        builder = new TubeMQRequestBuilder();
    }

    @Test
    public void TubeMQRequestTest() throws IOException {
        builder.setType("sampleType")
                .setMethod("sampleMethod")
                .setKey()
                .setParamsIfNotEmpty("param1", "value1")
                .setParamsMap(Collections.singletonList("param2=value2"));

        Request request = builder.buildRequest();

        Assert.assertEquals(
                "http://127.0.0.1:8080/webapi.htm?type=sampleType&method=sampleMethod&confModAuthToken=abc&param1=value1&param2=value2",
                request.url().toString());
        TopicCommonResult result = TubeMQHttpClient.getInstance().execute(request, TopicCommonResult.class);
        Assert.assertNotEquals(result.getErrCode().toString(), "0");
    }
}
