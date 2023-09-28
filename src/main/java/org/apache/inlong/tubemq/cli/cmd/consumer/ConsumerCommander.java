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

package org.apache.inlong.tubemq.cli.cmd.consumer;

import org.apache.inlong.tubemq.cli.Main;
import org.apache.inlong.tubemq.cli.cmd.TubeMQSingleFactory;
import org.apache.inlong.tubemq.cli.model.ConsumerType;
import org.apache.inlong.tubemq.cli.utils.log.Logger;
import org.apache.inlong.tubemq.client.consumer.ConsumerResult;
import org.apache.inlong.tubemq.client.consumer.PullMessageConsumer;
import org.apache.inlong.tubemq.client.exception.TubeClientException;
import org.apache.inlong.tubemq.corebase.Message;
import org.apache.inlong.tubemq.corebase.utils.ThreadUtils;

import org.apache.commons.text.TextStringBuilder;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Command(name = "consume", description = "provide consumer action")
public class ConsumerCommander implements Runnable {

    @ParentCommand
    private Main ParentCommand;

    @Option(names = {"-c", "--connect"}, description = "connect target master")
    String masterAddr = "";

    @Option(names = {"-f",
            "--format"}, description = "use the target format to print the message, with type: ${COMPLETION-CANDIDATES}.", defaultValue = "bytes")
    ConsumerType formatType;

    @Option(names = {"-a", "--all"}, description = "whether to show the all tag information.")
    boolean isAllPrint;

    @Option(names = {"-A", "--attribute"}, description = "whether to show the attribute")
    boolean isAttributePrint;

    @Option(names = {"-i", "--information"}, description = "whether to show the msg information")
    boolean isInformationPrint;

    @Option(names = {"--flag"}, description = "whether to show the msg flag")
    boolean isFlagPrint;

    @Option(names = {"-g", "--group"}, description = "the group of the topic")
    String groupTopic = "";

    @Option(names = {"-s",
            "--skip-consume"}, description = "mark the message received un consumed", defaultValue = "false")
    boolean isNotConsumed = false;

    @Option(names = {"-k",
            "--keep"}, description = "keep the consumer", defaultValue = "false")
    boolean isKeep;

    @Option(names = {"-n",
            "--number"}, description = "the number trying receives the message, default value is 1", defaultValue = "1")
    int number;

    private PullMessageConsumer consumer = null;

    String storedGroupTopic = "";

    @Parameters(index = "0", description = "topic name", defaultValue = "")
    String topic;

    @Option(names = {"-F", "--filter"}, description = "filter conds")
    List<String> filter = new ArrayList<>();

    @Override
    public void run() {
        ParentCommand.run();

        try {
            if (consumer == null) {
                if (!masterAddr.isEmpty())
                    TubeMQSingleFactory.getInstance(masterAddr);

                if (!groupTopic.isEmpty())
                    storedGroupTopic = groupTopic;

                if (storedGroupTopic.isEmpty()) {
                    Logger.logInfo(
                            "Please provide master address and topic group, using \"-c [masterAddr] -g [topic-group]\"");
                    return;
                }

                consumer = TubeMQSingleFactory.getInstance().createPullConsumer(storedGroupTopic);
            }
        } catch (Exception e) {
            Logger.logError("Error when init the consumer, please check your group and host.");
            Logger.logError("Error host: " + TubeMQSingleFactory.getInstance().getMasterAddr()
                    + ", group: " + storedGroupTopic);
            Logger.logError(e.toString());
            return;
        }

        try {
            if (!topic.isEmpty()) {
                consumer.subscribe(topic, getFilterConds());
                consumer.completeSubscribe();
            } else {
                if (consumer != null) {
                    Logger.logInfo("Using last register topic...");
                } else {
                    Logger.logInfo(
                            "You should provide at least one topic, or leave empty unless you have kept consumer");
                    return;
                }
            }
        } catch (TubeClientException e) {
            Logger.logError("Error when subscribing the topic.");
            Logger.logError(e.toString());
            return;
        }

        while (!consumer.isPartitionsReady(100)) {
            ThreadUtils.sleep(100);
        }

        Logger.logInfo("Connected successfully! Waiting for message...");
        int count = 1;
        while (count <= number) {
            ConsumerResult result;
            try {
                result = consumer.getMessage();
            } catch (TubeClientException e) {
                Logger.logError("Error when getting the message.");
                Logger.logError(e.toString());
                return;
            }

            if (result != null && result.isSuccess()) {
                List<Message> messageList = result.getMessageList();
                if (messageList.isEmpty())
                    continue;
                for (Message message : messageList) {
                    Logger.logInfo("Received message: ");
                    printMessage(message);
                }

                try {
                    consumer.confirmConsume(result.getConfirmContext(), !isNotConsumed);
                } catch (TubeClientException e) {
                    Logger.logError("Error when consuming the message.");
                    Logger.logError(e.toString());
                    return;
                }
            } else {
                if (result == null) {
                    Logger.logError("Error about the message received, got a null result");
                } else {
                    Logger.logError(
                            "Error about the message received: " + result.getErrCode()
                                    + ", with" + result.getErrMsg());
                }
            }
            count++;
        }
        try {
            if (!isKeep) {
                consumer.shutdown();
                consumer = null;
            }
        } catch (Throwable e) {
            Logger.logError("Error when shutdown consumer, maybe leaking memory...");
        }
    }

    private TreeSet<String> getFilterConds() {
        if (filter.isEmpty()) {
            return null;
        }
        return new TreeSet<>(filter);
    }

    private void printMessage(Message message) {
        TextStringBuilder textStringBuilder = new TextStringBuilder();
        textStringBuilder.appendPadding(4, ' ').append("Topic Name:").append(message.getTopic()).append('\n');
        textStringBuilder.appendPadding(4, ' ').append("Topic Index:").append(message.getIndexId()).append('\n');
        textStringBuilder.appendPadding(4, ' ');
        switch (formatType) {
            case bytes:
                textStringBuilder.append("Message Data: ");
                for (byte sp : message.getData()) {
                    textStringBuilder.append(sp).append(' ');
                }
                textStringBuilder.append('\n');
                break;
            case string:
                textStringBuilder.append("Message String: ")
                        .append(new String(message.getData(), StandardCharsets.UTF_8))
                        .append('\n');
                break;
        }

        if ((isAllPrint || isAttributePrint) && message.getAttribute() != null) {
            textStringBuilder.appendPadding(4, ' ')
                    .append("Attribute: ").appendln(message.getAttribute());
        }

        if ((isAllPrint || isInformationPrint) && message.getMsgType() != null) {
            textStringBuilder.appendPadding(4, ' ')
                    .append("Message Type: ").appendln(message.getMsgType());
        }

        if ((isAllPrint || isInformationPrint) && message.getAttribute() != null) {
            textStringBuilder.appendPadding(4, ' ')
                    .append("Message Time: ").appendln(message.getMsgTime());
        }

        if ((isAllPrint || isFlagPrint) && message.getAttribute() != null) {
            textStringBuilder.appendPadding(4, ' ')
                    .append("Flag: ").appendln(message.getFlag());
        }

        Logger.logInfo(textStringBuilder.build());
    }
}
