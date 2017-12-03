package cn.teamstack.logger.websocket;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author zhouli
 * @date 2017/8/14
 */
public class ProcessThread extends Thread {
    private BufferedReader reader;
    private WebSocketSession webSocketSession;
    private Channel channel;
    private Session session;

    ProcessThread(InputStream in, WebSocketSession webSocketSession, Session session, Channel channel) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.webSocketSession = webSocketSession;
        this.session = session;
        this.channel = channel;
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                webSocketSession.sendMessage(new TextMessage(line.getBytes()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            channel.disconnect();
            session.disconnect();
        }
    }
}
