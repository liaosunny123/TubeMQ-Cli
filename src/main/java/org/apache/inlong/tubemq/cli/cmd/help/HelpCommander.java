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

package org.apache.inlong.tubemq.cli.cmd.help;

import org.apache.inlong.tubemq.cli.cmd.consumer.ConsumerCommander;
import org.apache.inlong.tubemq.cli.cmd.producer.ProducerCommander;
import org.apache.inlong.tubemq.cli.cmd.topic.TopicCommander;
import org.apache.inlong.tubemq.cli.cmd.topic.add.AddCommander;
import org.apache.inlong.tubemq.cli.cmd.topic.delete.DeleteCommander;
import org.apache.inlong.tubemq.cli.cmd.topic.list.ListCommander;
import org.apache.inlong.tubemq.cli.cmd.topic.modify.ModifyCommander;
import org.apache.inlong.tubemq.cli.model.CMDType;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "help", description = "provide help information")
public class HelpCommander implements Runnable {

    @Parameters(index = "0", description = "cmdType: ${COMPLETION-CANDIDATES}.")
    CMDType cmdType;

    @Parameters(description = "action name, with the parent command if not provided.", defaultValue = "")
    String action;

    @Override
    public void run() {
        switch (cmdType) {
            case topic:
                switch (action) {
                    case "list":
                        CommandLine.usage(new ListCommander(), System.out);
                        break;
                    case "add":
                        CommandLine.usage(new AddCommander(), System.out);
                        break;
                    case "delete":
                        CommandLine.usage(new DeleteCommander(), System.out);
                        break;
                    case "modify":
                        CommandLine.usage(new ModifyCommander(), System.out);
                        break;
                    default:
                        CommandLine.usage(new TopicCommander(), System.out);
                        break;
                }
                break;
            case help:
                CommandLine.usage(this, System.out);
                break;
            case consume:
                CommandLine.usage(new ConsumerCommander(), System.out);
                break;
            case produce:
                CommandLine.usage(new ProducerCommander(), System.out);
                break;
        }
    }
}
