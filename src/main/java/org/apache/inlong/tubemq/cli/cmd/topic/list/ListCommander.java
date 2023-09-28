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

package org.apache.inlong.tubemq.cli.cmd.topic.list;

import org.apache.inlong.tubemq.cli.cmd.TubeMQHttpClient;
import org.apache.inlong.tubemq.cli.cmd.TubeMQRequestBuilder;
import org.apache.inlong.tubemq.cli.cmd.topic.TopicCommander;
import org.apache.inlong.tubemq.cli.model.topic.list.Datum;
import org.apache.inlong.tubemq.cli.model.topic.list.TopicInfo;
import org.apache.inlong.tubemq.cli.model.topic.list.TopicListResult;
import org.apache.inlong.tubemq.cli.utils.JsonFormatter;
import org.apache.inlong.tubemq.cli.utils.log.Logger;

import org.apache.commons.text.TextStringBuilder;
import org.jetbrains.annotations.NotNull;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Command(name = "list", description = "list topic information")
public class ListCommander implements Runnable {

    @ParentCommand
    TopicCommander ParentCommand;

    @Option(names = {"-l", "--list"}, description = "show mini details in list format")
    boolean isList;

    @Option(names = {"-d", "--detail"}, description = "show detailed details in list format")
    boolean isDetailed = false;

    @Option(names = {"-j", "--json"}, description = "print all information in json format")
    boolean isJsonFormat;

    @Option(names = {"-p", "--pretty"}, description = "make json format pretty if printing in json format")
    boolean isJsonFormatPrettier = false;

    @Option(names = {"-b", "--broker-id"}, description = "limit target broker id")
    String brokerId;

    @Option(names = {"-t", "--topic-name"}, description = "limit target topic name")
    String topicName;

    @Option(names = {"-m", "--map"}, description = "provide map params, in Key=Value model")
    List<String> params = new ArrayList<>();

    @Option(names = {"--create-user"}, description = "limit topic create user")
    String createUser;

    @Option(names = {"--modify-user"}, description = "limit topic modify user")
    String modifyUser;

    @Override
    public void run() {
        if (ParentCommand.isNotOk()) {
            return;
        }

        TubeMQRequestBuilder builder = new TubeMQRequestBuilder();
        builder.setType("op_query")
                .setMethod("admin_query_topic_info")
                .setParamsIfNotEmpty("brokerId", brokerId)
                .setParamsIfNotEmpty("topicName", topicName)
                .setParamsIfNotEmpty("createUser", createUser)
                .setParamsIfNotEmpty("modifyUser", modifyUser)
                .setParamsMap(params);
        try {
            TopicListResult result =
                    TubeMQHttpClient.getInstance().execute(builder.buildRequest(), TopicListResult.class);
            if (!result.getResult() || result.getErrCode() != 0) {
                Logger.logError("Can not fetch result from TubeMQ Server with error: " + result.getErrCode() + " , "
                        + result.getErrMsg());
                return;
            }

            if (isJsonFormat) {
                if (isJsonFormatPrettier) {
                    Logger.logInfo(JsonFormatter.getPrettyJsonString(result));
                    return;
                }
                Logger.logInfo(JsonFormatter.getJsonString(result));
                return;
            }

            if (!isList) {
                Logger.logInfo(formatTable(result));
                return;
            }

            Logger.logInfo(formatDetail(result));
        } catch (IOException e) {
            Logger.logError("Error when sending packet to TubeMQ-Server: Get Topic List");
            Logger.logError(e.toString());
        }
    }

    private String formatTableHeader() {
        TextStringBuilder textBuilder = new TextStringBuilder();
        textBuilder.appendFixedWidthPadLeft("Name", 16, ' ');
        textBuilder.appendFixedWidthPadLeft("BrokerId", 10, ' ');
        textBuilder.appendFixedWidthPadLeft("createUser", 16, ' ');
        textBuilder.appendFixedWidthPadLeft("createDate", 16, ' ');
        textBuilder.appendFixedWidthPadLeft("acceptPublish", 16, ' ');
        textBuilder.appendFixedWidthPadLeft("acceptSubscribe", 16, ' ');
        textBuilder.append('\n');
        return textBuilder.build();
    }

    private String formatTableRow(@NotNull TopicInfo info) {
        TextStringBuilder textBuilder = new TextStringBuilder();
        textBuilder.appendFixedWidthPadLeft(info.getTopicName(), 16, ' ');
        textBuilder.appendFixedWidthPadLeft(info.getBrokerId(), 10, ' ');
        textBuilder.appendFixedWidthPadLeft(info.getCreateUser(), 16, ' ');
        textBuilder.appendFixedWidthPadLeft(info.getCreateDate(), 16, ' ');
        textBuilder.appendFixedWidthPadLeft(info.getAcceptPublish(), 16, ' ');
        textBuilder.appendFixedWidthPadLeft(info.getAcceptSubscribe(), 16, ' ');
        textBuilder.append('\n');
        return textBuilder.build();
    }

    private String formatTable(@NotNull TopicListResult model) {
        TextStringBuilder textBuilder = new TextStringBuilder();
        textBuilder.appendln("Count: " + model.getCount());
        for (Datum entry : model.getData()) {
            textBuilder.append("Topic Name:  ").append(entry.getTopicName()).append('\n');
            textBuilder.appendln("topicInfo:  ");
            textBuilder.append(formatTableHeader());
            for (TopicInfo info : entry.getTopicInfo()) {
                textBuilder.append(formatTableRow(info));
            }
            textBuilder.appendln("");
        }
        return textBuilder.build();
    }

    private String formatDetail(@NotNull TopicListResult model) {
        TextStringBuilder textBuilder = new TextStringBuilder();
        textBuilder.appendln("Count: " + model.getCount());
        for (Datum entry : model.getData()) {
            textBuilder.append("Topic Name:  ").append(entry.getTopicName()).append('\n');
            textBuilder.append("      Info:  ");
            for (TopicInfo info : entry.getTopicInfo()) {
                textBuilder.append(info.getTopicName()).append(' ');
            }
            textBuilder.appendln("");
            for (TopicInfo info : entry.getTopicInfo()) {
                if (isDetailed) {
                    textBuilder.appendPadding(8, ' ').append("Child Name: ").append(info.getTopicName()).append('\n');
                    textBuilder.appendPadding(8, ' ').append("Broker Id: ").append(info.getBrokerId()).append('\n');
                    textBuilder.appendPadding(8, ' ').append("Broker Ip: ").append(info.getBrokerIp()).append('\n');
                    textBuilder.appendPadding(8, ' ').append("Broker Port: ").append(info.getBrokerPort())
                            .append('\n');
                    textBuilder.appendPadding(8, ' ').append("Delete Policy: ").append(info.getDeletePolicy())
                            .append('\n');
                    textBuilder.appendPadding(8, ' ').append("Create User: ").append(info.getCreateUser())
                            .append('\n');
                    textBuilder.appendPadding(8, ' ').append("Create Date: ").append(info.getCreateDate())
                            .append('\n');
                    textBuilder.appendPadding(8, ' ').append("Accept Publish: ")
                            .append(info.getRunInfo().getAcceptPublish()).append('\n');
                    textBuilder.appendPadding(8, ' ').append("Accept Subscribe: ")
                            .append(info.getRunInfo().getAcceptSubscribe()).append('\n');
                    textBuilder.appendPadding(8, ' ').append("Broker Manage Status: ")
                            .append(info.getRunInfo().getBrokerManageStatus()).append('\n');
                }
            }
            textBuilder.appendln(" ");
        }
        return textBuilder.build();
    }
}
