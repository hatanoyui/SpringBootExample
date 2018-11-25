# SpringBootExample
Some useful project example which are created with SpringBoot 2.1.x and some relevant technologies.

## spring-websocket

a simple chat application project with SpringBoot 2.1.x which contains Thymeleaf, Websocket, SpringData-Redis, and ReconnectingWebSocket js lib.

### Feature
1. Any message will be send to all websocket client(browser).
2. The chat server subscribes to Redis at channel ws_msg_channel. It also can publish the message to other subscribers by redis.

### Usage
1. Launch it by running SpringWebsocketApplication using IntelliJ IDEA
2. It is aussmued that the Redis is installed at 127.0.0.1:6379
3. Open http://127.0.0.1:8080/websocket in your browser.
4. The page will establish a websocket connection to backend ws://127.0.0.1/ws
5. Now, you can send message to chat server.
