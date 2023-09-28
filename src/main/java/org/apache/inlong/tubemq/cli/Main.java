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

package org.apache.inlong.tubemq.cli;

import org.apache.inlong.tubemq.cli.cmd.consumer.ConsumerCommander;
import org.apache.inlong.tubemq.cli.cmd.help.HelpCommander;
import org.apache.inlong.tubemq.cli.cmd.producer.ProducerCommander;
import org.apache.inlong.tubemq.cli.cmd.topic.TopicCommander;
import org.apache.inlong.tubemq.cli.utils.log.LogType;
import org.apache.inlong.tubemq.cli.utils.log.Logger;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Command(name = "tubectl", header = "%nTubeCtl provides cmd prompt for TubeMQ%n", mixinStandardHelpOptions = true, version = "@|yellow TubeCtl 1.0 Build 10001-beta (c) 2023|@", subcommands = {
        HelpCommander.class,
        TopicCommander.class,
        ProducerCommander.class,
        ConsumerCommander.class,
})
public class Main implements Runnable {

    private static CommandLine mainCommand;
    public static void main(String[] args) {
        mainCommand = new CommandLine(new Main());
        int exitCode = mainCommand.execute(args);
        System.exit(exitCode);
    }

    @Option(names = {"-d", "--debug"}, description = "print debug info when carrying command.")
    boolean isDebug = false;

    @Option(names = {"-i", "--interactive"}, description = "enter interactive mode")
    boolean isInteractive;

    boolean inInteractiveMode = false;

    @Override
    public void run() {
        if (inInteractiveMode) {
            return;
        }

        // Configure Main module

        if (isDebug) {
            Logger.setLogLevel(LogType.DEBUG);
        }

        if (isInteractive) {
            interactiveMode();
        }
    }

    private void interactiveMode() {
        Logger.logInfo("Entering interactive mode, will auto ignore first passed action, " +
                "please enter now. Use `quit` or `exit` to quit tubectl interactive mode");
        Logger.logBlank();
        inInteractiveMode = true;
        Scanner cin = new Scanner(System.in);
        while (true) {
            Logger.logCommonInput();
            String input = cin.nextLine();

            if (input.isEmpty()) {
                continue;
            }

            if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) {
                System.exit(0);
            } else {
                mainCommand.execute(splitString(input));
            }
        }
    }

    private static String[] splitString(String input) {
        List<String> resultList = new ArrayList<>();

        Pattern pattern = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                resultList.add(matcher.group(1));
            } else {
                resultList.add(matcher.group());
            }
        }

        return resultList.toArray(new String[0]);
    }
}