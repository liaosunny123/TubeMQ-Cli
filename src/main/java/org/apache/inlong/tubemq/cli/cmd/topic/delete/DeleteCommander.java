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

package org.apache.inlong.tubemq.cli.cmd.topic.delete;

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

@Command(name = "delete", description = "delete target topic, softly default")
public class DeleteCommander implements Runnable {

    @ParentCommand
    TopicCommander ParentCommand;

    @Parameters(index = "0", description = "the id of the broker")
    String brokerId;

    @Parameters(index = "1", description = "the name of the topic")
    String topicName;

    @Option(names = {"--modify-user"}, description = "topic modify user", defaultValue = "tubectl")
    String modifyUser;

    @Option(names = {"--modify-date"}, description = "the date of modify", defaultValue = "default")
    String modifyDate;

    @Option(names = {"-H", "--hard"}, description = "use hard remove mode", defaultValue = "false")
    boolean isHard;

    @Option(names = {"-f",
            "--force"}, description = "delete topic using force mod, which will auto close the subscribe and publish state", defaultValue = "false")
    boolean isForce;

    @Override
    public void run() {
        if (ParentCommand.isNotOk()) {
            return;
        }

        if ("default".equals(modifyDate)) {
            modifyDate = DateProvider.getCurrentDate();
        }

        TubeMQRequestBuilder builder = new TubeMQRequestBuilder();
        if (isForce) {
            TubeMQRequestBuilder forceBuilder = new TubeMQRequestBuilder();
            forceBuilder.setType("op_modify")
                    .setMethod("admin_modify_topic_info")
                    .setParamsIfNotEmpty("brokerId", brokerId)
                    .setParamsIfNotEmpty("topicName", topicName)
                    .setParamsIfNotEmpty("modifyUser", "tubectl")
                    .setParamsIfNotEmpty("modifyDate", modifyDate)
                    .setParamsIfNotEmpty("acceptPublish", "false")
                    .setParamsIfNotEmpty("acceptSubscribe", "false")
                    .setKey();
            try {
                TopicCommonResult result = ResultBuilder.getTopicCommonResult(forceBuilder);
                if (result == null) {
                    return;
                }
            } catch (IOException e) {
                Logger.logError("Error when sending packet to TubeMQ-Server: Remove Topic with Force mode");
                Logger.logError(e.toString());
            }
        }

        if (isHard) {
            builder.setType("op_modify")
                    .setMethod("admin_remove_topic_info")
                    .setParamsIfNotEmpty("brokerId", brokerId)
                    .setParamsIfNotEmpty("topicName", topicName)
                    .setParamsIfNotEmpty("modifyUser", modifyUser)
                    .setParamsIfNotEmpty("modifyDate", modifyDate)
                    .setKey();
        } else {
            builder.setType("op_modify")
                    .setMethod("admin_delete_topic_info")
                    .setParamsIfNotEmpty("brokerId", brokerId)
                    .setParamsIfNotEmpty("topicName", topicName)
                    .setParamsIfNotEmpty("modifyUser", modifyUser)
                    .setParamsIfNotEmpty("modifyDate", modifyDate)
                    .setKey();
        }

        try {
            TopicCommonResult result = ResultBuilder.getTopicCommonResult(builder);
            if (result == null) {
                return;
            }

            Logger.logInfo("Remove topic success! with " + (isHard ? "hard" : "soft") + " mode."
                    + (isForce ? "(Forced)" : ""));
            Logger.logBlank();
            for (Datum entry : result.getData()) {
                Logger.logInfo(
                        "Remove topic name \"" + entry.getTopicName() + "\" in brokerId: " + entry.getBrokerId());
            }
        } catch (IOException e) {
            Logger.logError("Error when sending packet to TubeMQ-Server: Remove Topic");
            Logger.logError(e.toString());
        }
    }
}
