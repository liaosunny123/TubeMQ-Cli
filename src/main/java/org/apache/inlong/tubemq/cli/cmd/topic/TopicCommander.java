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

package org.apache.inlong.tubemq.cli.cmd.topic;

import org.apache.inlong.tubemq.cli.Main;
import org.apache.inlong.tubemq.cli.cmd.TubeMQHttpClient;
import org.apache.inlong.tubemq.cli.cmd.topic.add.AddCommander;
import org.apache.inlong.tubemq.cli.cmd.topic.delete.DeleteCommander;
import org.apache.inlong.tubemq.cli.cmd.topic.list.ListCommander;
import org.apache.inlong.tubemq.cli.cmd.topic.modify.ModifyCommander;
import org.apache.inlong.tubemq.cli.utils.log.Logger;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@CommandLine.Command(name = "topic", description = "provide topic related action", subcommands = {
        ListCommander.class,
        ModifyCommander.class,
        DeleteCommander.class,
        AddCommander.class,
})
public class TopicCommander implements Runnable {

    @ParentCommand
    private Main ParentCommand;

    @Option(names = {"-c",
            "--connect"}, description = "connect targeted host, with 127.0.0.1:8080 default", defaultValue = "127.0.0.1:8080")
    String hostAddr = "";

    @Option(names = {"-k", "--key"}, arity = "0..1", interactive = true)
    String key = "";

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }

    public boolean isNotOk() {
        ParentCommand.run();

        if (!TubeMQHttpClient.isRegisterInformation() && (hostAddr.isEmpty() || key.isEmpty())) {
            Logger.logInfo(
                    "Should provide `key` and `hostAddr`, using \"-c [hostAddr] -k [key]\", or do not provide" +
                            " key field and input in password-type.");
            return true;
        }

        TubeMQHttpClient.getInstance(key, hostAddr);
        return false;
    }
}
