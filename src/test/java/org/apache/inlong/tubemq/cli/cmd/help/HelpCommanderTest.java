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

import org.apache.inlong.tubemq.cli.Main;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpCommanderTest {

    private CommandLine mainCommand;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUp() {
        mainCommand = new CommandLine(new Main());
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void helpTest() {
        mainCommand.execute(splitString("help help"));
        mainCommand.execute(splitString("help topic"));
        mainCommand.execute(splitString("help topic add"));
        mainCommand.execute(splitString("help topic delete"));
        mainCommand.execute(splitString("help topic list"));
        mainCommand.execute(splitString("help topic modify"));
        mainCommand.execute(splitString("help consume"));
        mainCommand.execute(splitString("help produce"));
        String allOutput = outContent + errContent.toString();
        Assert.assertFalse(allOutput.contains("error") || allOutput.contains("Unknown"));
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
