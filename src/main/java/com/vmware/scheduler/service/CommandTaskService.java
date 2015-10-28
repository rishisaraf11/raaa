package com.vmware.scheduler.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * @author rkasha
 */
@Component
public class CommandTaskService {
    static final int PORT = 22;

    public String execute(Map<String, Object> payload) {
        String hostIP = payload.get("hostIP").toString();
        String username = payload.get("username").toString();
        String password = payload.get("password").toString();
        String command = payload.get("command").toString();
        int exitStatus = run(hostIP, PORT, username, password, command);
        System.out.println("Successfully executed linux command");
        return String.valueOf(exitStatus);
    }

    private int run(String hostIP, int port, String username, String password, String command) {
        int exitStatus = -100;
        Session session = getSSHSession(hostIP, port, username, password);
        if (session != null) {
            Channel channel = executeCommand(command, session);
            if (channel != null) {
                exitStatus = getExitStatus(channel);
                session.disconnect();
                channel.disconnect();
                System.out.println("DONE");
            }
        } else {
            //TODO: throw exception:unable to establish a ssh session
        }

        return exitStatus;
    }

    private Session getSSHSession(String hostIP, int port, String username, String password) {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession(username, hostIP, port);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            return session;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Channel executeCommand(String command, Session session) {
        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            channel.setOutputStream(System.out);
            InputStream in = channel.getInputStream();
            channel.connect();
            printCommandOutput(channel, in);
            return channel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void printCommandOutput(Channel channel, InputStream inputStream) {
        byte[] tmp = new byte[1024];
        try {
            while (true) {
                while (inputStream.available() > 0) {
                    int i = inputStream.read(tmp, 0, 1024);
                    if (i < 0)
                        break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getExitStatus(Channel channel) {
        while (channel.getExitStatus() == -1) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return channel.getExitStatus();
    }
}
