/*
 * Copyright (C) 2018 Drake, Inc. https://github.com/liangjingkanji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.engine.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;


public final class ShellUtils {

    private static final String LINE_SEP = System.getProperty("line.separator");

    private ShellUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Execute the command.
     *
     * @param command The command.
     * @param isRoot  True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command, final boolean isRoot) {
        return execCmd(new String[]{command}, isRoot, true);
    }

    /**
     * Execute the command.
     *
     * @param commands The commands.
     * @param isRoot   True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final List<String> commands, final boolean isRoot) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}), isRoot, true);
    }

    /**
     * Execute the command.
     *
     * @param commands The commands.
     * @param isRoot   True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String[] commands, final boolean isRoot) {
        return execCmd(commands, isRoot, true);
    }

    /**
     * Execute the command.
     *
     * @param command         The command.
     * @param isRoot          True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command,
                                        final boolean isRoot,
                                        final boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, isRoot, isNeedResultMsg);
    }

    /**
     * Execute the command.
     *
     * @param commands        The commands.
     * @param isRoot          True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final List<String> commands,
                                        final boolean isRoot,
                                        final boolean isNeedResultMsg) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}),
                isRoot,
                isNeedResultMsg);
    }

    /**
     * Execute the command.
     *
     * @param commands        The commands.
     * @param isRoot          True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String[] commands,
                                        final boolean isRoot,
                                        final boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        Process process = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        try {

            BufferedReader successResult;
            BufferedReader errorResult;
            DataOutputStream os;

            process = Runtime.getRuntime()
                    .exec(isRoot ? "su" : "sh");
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }
                os.write(command.getBytes());
                os.writeBytes(LINE_SEP);
                os.flush();
            }
            os.writeBytes("exit" + LINE_SEP);
            os.flush();
            result = process.waitFor();
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream(),
                        StandardCharsets.UTF_8));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream(),
                        StandardCharsets.UTF_8));
                String line;
                if ((line = successResult.readLine()) != null) {
                    successMsg.append(line);
                    while ((line = successResult.readLine()) != null) {
                        successMsg.append(LINE_SEP)
                                .append(line);
                    }
                }
                if ((line = errorResult.readLine()) != null) {
                    errorMsg.append(line);
                    while ((line = errorResult.readLine()) != null) {
                        errorMsg.append(LINE_SEP)
                                .append(line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {


            if (process != null) {
                process.destroy();
            }
        }

        return new CommandResult(
                result,
                successMsg == null ? null : successMsg.toString(),
                errorMsg == null ? null : errorMsg.toString()
        );
    }

    /**
     * The result of command.
     */
    public static class CommandResult {

        public int result;
        public String successMsg;
        public String errorMsg;

        public CommandResult(final int result, final String successMsg, final String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
}
