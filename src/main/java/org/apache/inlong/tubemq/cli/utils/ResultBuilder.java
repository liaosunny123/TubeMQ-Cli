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

package org.apache.inlong.tubemq.cli.utils;

import org.apache.inlong.tubemq.cli.cmd.TubeMQHttpClient;
import org.apache.inlong.tubemq.cli.cmd.TubeMQRequestBuilder;
import org.apache.inlong.tubemq.cli.model.topic.common.Datum;
import org.apache.inlong.tubemq.cli.model.topic.common.TopicCommonResult;
import org.apache.inlong.tubemq.cli.utils.log.Logger;

import java.io.IOException;

public class ResultBuilder {

    public static TopicCommonResult getTopicCommonResult(TubeMQRequestBuilder builder) throws IOException {
        TopicCommonResult result =
                TubeMQHttpClient.getInstance().execute(builder.buildRequest(), TopicCommonResult.class);
        boolean isOk = true;
        StringBuilder childErrMsg = new StringBuilder();
        for (Datum entry : result.getData()) {
            if (entry.getErrCode() != 0 && entry.getErrCode() != 200) {
                isOk = false;
                childErrMsg.append("BrokerId: ")
                        .append(entry.getBrokerId())
                        .append(" happens error: ")
                        .append(entry.getErrInfo())
                        .append('\n');
            }
        }
        if (!result.getResult() || result.getErrCode() != 0 || !isOk) {
            Logger.logError("Can not fetch result from TubeMQ Server with error: " + result.getErrCode() + " , "
                    + result.getErrMsg());
            if (!isOk) {
                Logger.logError("Child error:");
                Logger.logError(childErrMsg.toString());
            }
            return null;
        }
        return result;
    }
}
