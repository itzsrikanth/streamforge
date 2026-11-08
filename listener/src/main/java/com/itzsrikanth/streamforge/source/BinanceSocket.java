package com.itzsrikanth.streamforge.source;

import com.itzsrikanth.streamforge.model.ITickHandler;
import com.itzsrikanth.streamforge.model.TickParser;
import com.itzsrikanth.streamforge.model.TradeWire;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

public class BinanceSocket {

    public static final String BINANCE_WS_URL = "wss://data-stream.binance.vision/ws/btcusdt@trade";
    private static final ObjectMapper mapper = new ObjectMapper();

    public WebSocket.Listener getSocket(CountDownLatch socketClosed, ITickHandler handler) {
        WebSocket.Listener socket = new WebSocket.Listener() {

            private int textCallbacks = 0;

            @Override
            public void onOpen(WebSocket webSocket) {
                webSocket.request(1);
            }

            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                if (textCallbacks < 10) {
                    webSocket.request(1);
                    try {
                        String json = data.toString();
                        TradeWire wired = mapper.readValue(json, TradeWire.class);
                        TickParser.toTick(wired).ifPresentOrElse(handler::onTick, () -> {
                            System.err.println("bad data dropped: " + data);
                        });
                    } catch (JacksonException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Received 10 callbacks");
                    webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "");
                }
                textCallbacks++;
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                System.out.println("Closed: " + statusCode + " " + reason);
                socketClosed.countDown();
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                error.printStackTrace();
            }
        };

        return socket;

    }
}
