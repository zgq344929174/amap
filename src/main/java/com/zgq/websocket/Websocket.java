package com.zgq.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.sockjs.transport.session.WebSocketServerSockJsSession;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@ServerEndpoint("/websocket")
public class Websocket {
    private static Logger logger = LoggerFactory.getLogger(Websocket.class);

    private Session session;

    private static CopyOnWriteArrayList<Websocket> webSocketSet = new CopyOnWriteArrayList<>();
    @OnOpen
    public void opOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        logger.info("【webSocket消息】有新的连接，总数{}" ,webSocketSet.size());
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        logger.info("【webSocket消息】连接断开，总数{}" ,webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message){
        logger.info("【webSocket消息】收到客户端发来的消息，{}" ,message);
    }

    public void sendMessage(String message){
        for(Websocket websocket : webSocketSet){
            logger.info("【webSocket消息】广播消息，message={}" ,message);
            try {
                websocket.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
