package cn.teamstack.logger.config;

import cn.teamstack.logger.websocket.WebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket配置
 * @author zhouli
 * @date 2017/8/14
 */
@Configuration
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/websocket").setAllowedOrigins("*");
    }

    @Bean
    public org.springframework.web.socket.WebSocketHandler webSocketHandler() {
        return new WebSocketHandler();
    }


}
