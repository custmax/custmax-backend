// SshExecutor.java
package com.custmax.officialsite.util;

import com.jcraft.jsch.*;
import java.io.ByteArrayOutputStream;

public class SshExecutor {
    public static String exec(String host, int port, String user, String password, String cmd) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(cmd);
        channel.setInputStream(null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        channel.setOutputStream(outputStream);
        channel.setErrStream(outputStream);
        channel.connect();

        while (!channel.isClosed()) {
            Thread.sleep(100);
        }
        String result = outputStream.toString();
        channel.disconnect();
        session.disconnect();
        return result;
    }
}