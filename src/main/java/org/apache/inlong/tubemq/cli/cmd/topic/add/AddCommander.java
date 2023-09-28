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

package org.apache.inlong.tubemq.cli.cmd.topic.add;

import org.apache.inlong.tubemq.cli.cmd.TubeMQRequestBuilder;
import org.apache.inlong.tubemq.cli.cmd.topic.TopicCommander;
import org.apache.inlong.tubemq.cli.model.topic.common.Datum;
import org.apache.inlong.tubemq.cli.model.topic.common.TopicCommonResult;
import org.apache.inlong.tubemq.cli.utils.DateProvider;
import org.apache.inlong.tubemq.cli.utils.ResultBuilder;
import org.apache.inlong.tubemq.cli.utils.log.Logger;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Command(name = "add", description = "add new topic")
public class AddCommander implements Runnable {

    @ParentCommand
    TopicCommander ParentCommand;

    @Parameters(index = "0", description = "the id of the broker")
    String brokerId;

    @Parameters(index = "1", description = "the name of topic added")
    String topicName;

    @Option(names = {"-m", "--map"}, description = "provide map params, in Key=Value model")
    List<String> params = new ArrayList<>();

    @Option(names = {"--create-user"}, description = "topic create user", defaultValue = "tubectl")
    String createUser;

    @Option(names = {"--create-date"}, description = "topic create date", defaultValue = "default")
    String createDate;

    @Override
    public void run() {
        if (ParentCommand.isNotOk()) {
            return;
        }

        if ("default".equals(createDate)) {
            createDate = DateProvider.getCurrentDate();
        }

        TubeMQRequestBuilder builder = new TubeMQRequestBuilder();
        builder.setType("op_modify")
                .setMethod("admin_add_new_topic_record")
                .setParamsIfNotEmpty("brokerId", brokerId)
                .setParamsIfNotEmpty("topicName", topicName)
                .setParamsIfNotEmpty("createUser", createUser)
                .setParamsIfNotEmpty("createDate", createDate)
                .setKey()
                .setParamsMap(params);

        try {
            TopicCommonResult result = ResultBuilder.getTopicCommonResult(builder);
            if (result == null) {
                return;
            }

            Logger.logInfo("Add topic success!");
            Logger.logBlank();
            for (Datum entry : result.getData()) {
                Logger.logInfo("Added topic name \"" + entry.getTopicName() + "\" in brokerId: " + entry.getBrokerId());
            }
        } catch (IOException e) {
            Logger.logError("Error when sending packet to TubeMQ-Server: Add Topic");
            Logger.logError(e.toString());
        }
    }
}
