package cn.teamstack.logger.websocket;

import cn.teamstack.logger.model.QueryCmd;
import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhouli
 * @date 2017/9/18
 */
public class WebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(TextWebSocketHandler.class);

    @Value("${ssh.server.log.ip}")
    private String sshIP;
    @Value("${ssh.server.log.port}")
    private String sshPort;
    @Value("${ssh.server.log.username}")
    private String sshUsername;
    @Value("${ssh.server.log.password}")
    private String sshPassword;

    @Override
    public void handleTextMessage(WebSocketSession webSocketSession, TextMessage textMessage) {
        String message = textMessage.getPayload();
        QueryCmd cmd = JSON.parseObject(message, QueryCmd.class);

        JSch jsch = new JSch();
        try {
            Session session;
            if (cmd.ssh != null && !StringUtils.isEmpty(cmd.ssh.ip)) {
                //可以优先使用配置的ssh配置信息
                session = jsch.getSession(cmd.ssh.username, cmd.ssh.ip, Integer.parseInt(cmd.ssh.port));
                session.setPassword(cmd.ssh.password);
            } else {
                //使用代理程序默认配置的信息
                logger.info(sshIP);
                logger.info(sshPort);
                session = jsch.getSession(sshUsername, sshIP, Integer.parseInt(sshPort));
                session.setPassword(sshPassword);
            }

            UserInfo ui = new AbstractMyUserInfo() {
                @Override
                public void showMessage(String message) {
                    JOptionPane.showMessageDialog(null, message);
                }

                @Override
                public boolean promptYesNo(String message) {
                    return true;
                }
            };

            session.setUserInfo(ui);
            // making a connection with timeout.
            session.connect(30000);
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cmd.content);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream inputStream = channel.getInputStream();
            channel.connect();

            ProcessThread thread = new ProcessThread(inputStream, webSocketSession, session, channel);
            thread.start();

        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.debug("Opened new session in instance " + this);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception)
            throws Exception {
        session.close(CloseStatus.SERVER_ERROR);
    }

    public static abstract class AbstractMyUserInfo
            implements UserInfo, UIKeyboardInteractive {
        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public boolean promptYesNo(String str) {
            return false;
        }

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return false;
        }

        @Override
        public boolean promptPassword(String message) {
            return false;
        }

        @Override
        public void showMessage(String message) {
        }

        @Override
        public String[] promptKeyboardInteractive(String destination,
                                                  String name,
                                                  String instruction,
                                                  String[] prompt,
                                                  boolean[] echo) {
            return null;
        }
    }
}
