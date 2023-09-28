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

package org.apache.inlong.tubemq.cli.cmd.producer;

import org.apache.inlong.tubemq.cli.Main;
import org.apache.inlong.tubemq.cli.cmd.TubeMQSingleFactory;
import org.apache.inlong.tubemq.cli.model.producer.ActionType;
import org.apache.inlong.tubemq.cli.utils.log.Logger;
import org.apache.inlong.tubemq.client.exception.TubeClientException;
import org.apache.inlong.tubemq.client.producer.MessageProducer;
import org.apache.inlong.tubemq.client.producer.MessageSentResult;
import org.apache.inlong.tubemq.corebase.Message;

import org.apache.commons.codec.binary.StringUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import java.util.ArrayList;
import java.util.List;

@Command(name = "produce", description = "provide producer action")
public class ProducerCommander implements Runnable {

    @ParentCommand
    private Main ParentCommand;

    @Parameters(index = "0", description = "the name of the topic: publish, send")
    ActionType action;

    @Parameters(index = "1", description = "the name of the topic")
    String topic;

    @Parameters(index = "2", description = "the content of the topic", defaultValue = "")
    String content;

    @Option(names = {"-c", "--connect"}, description = "connect target master")
    String masterAddr = "";

    @Option(names = {"-a", "--attribute"}, description = "tag attribute with the messages")
    List<String> attributes = new ArrayList<>();

    @Option(names = {"-H", "--header"}, description = "tag header with the messages")
    List<String> headers = new ArrayList<>();

    @Option(names = {"-p",
            "--publish"}, description = "send with publishing a new topic, if published, do not use this option")
    boolean isPublish;

    private MessageProducer producer = null;

    @Override
    public void run() {
        ParentCommand.run();

        try {
            if (producer == null) {
                if (!masterAddr.isEmpty())
                    TubeMQSingleFactory.getInstance(masterAddr);

                if (masterAddr.isEmpty() && !TubeMQSingleFactory.getInstance().isRegistered()) {
                    Logger.logInfo("You should provide master address, use \"-c [address]\"");
                    return;
                }

                producer = TubeMQSingleFactory.getInstance().createProducer();
                Logger.logInfo("Using master address: " + TubeMQSingleFactory.getInstance().getMasterAddr());
            }
        } catch (TubeClientException e) {
            Logger.logError("Error when creating TubeSingleSessionFactory, please check your address.");
            Logger.logError(e.toString());
            return;
        }

        if (producer == null) {
            return;
        }

        switch (action) {
            case publish:
                try {
                    producer.publish(topic);
                    Logger.logInfo("Published the topic!");
                } catch (TubeClientException e) {
                    Logger.logError("Error when publishing topic.");
                    Logger.logError(e.toString());
                    return;
                }
                break;
            case send:
                byte[] bodyData = StringUtils.getBytesUtf8(content);
                Message message = new Message(topic, bodyData);
                tagAttributes(message);
                tagHeaders(message);
                MessageSentResult result;
                try {
                    if (isPublish) {
                        producer.publish(topic);
                    }
                    result = producer.sendMessage(message);
                } catch (Exception e) {
                    Logger.logError("Error when sending message");
                    Logger.logError(e.toString());
                    return;
                }

                if (result != null && result.isSuccess()) {
                    Logger.logInfo("Send message success!");
                } else {
                    Logger.logInfo("Send message failed!");
                }
        }
    }

    private void tagAttributes(Message message) {
        for (String entry : attributes) {
            String[] pair = entry.split("=");
            if (pair.length != 2) {
                continue;
            }
            message.setAttrKeyVal(pair[0], pair[1]);
        }
    }

    private void tagHeaders(Message message) {
        for (String entry : headers) {
            String[] pair = entry.split("=");
            if (pair.length != 2) {
                continue;
            }
            message.putSystemHeader(pair[0], pair[1]);
        }
    }
}
