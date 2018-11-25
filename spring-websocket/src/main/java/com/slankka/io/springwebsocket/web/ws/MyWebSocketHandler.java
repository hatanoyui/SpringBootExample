package com.slankka.io.springwebsocket.web.ws;

import com.slankka.io.springwebsocket.redis.RedisConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> stringValueOperations;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    private static Map<String, WebSocketSession> webSocketSessionMap = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("WebSocket has receive a message {} from sessionId={}", message.getPayload(), session.getId());
        redisTemplate.convertAndSend(RedisConstant.WS_NEW_MSG_CHANNEL, message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Incoming WebSocket from sessionId={}", session.getId());
        webSocketSessionMap.put(session.getId(), session);
        stringValueOperations.set(RedisConstant.WS_CONNECTION_SESSION_PREFIX + session.getId(), session.getId());
        redisTemplate.convertAndSend(RedisConstant.WS_NEW_MSG_CHANNEL, session.getId()+" join us.");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        redisTemplate.convertAndSend(RedisConstant.WS_NEW_MSG_CHANNEL, session.getId()+" has disconnected");
        webSocketSessionMap.remove(session.getId());
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        logger.info("WebSocket has receive a PONG message {} from sessionId={}", message.getPayload(), session.getId());
    }

    public void sendAllMessage(Message message) throws IOException {

        for (Map.Entry<String, WebSocketSession> entry : webSocketSessionMap.entrySet()) {
            WebSocketSession value = entry.getValue();
            if (value != null && value.isOpen()) {
                value.sendMessage(new TextMessage(message.getBody()));
            }
        }

    }
}
