package com.itzsrikanth.streamforge;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

public class App {


    public static void main(String[] args) throws InterruptedException {

        String BINANCE_WS_URL = "wss://data-stream.binance.vision/ws/btcusdt@trade";
        CountDownLatch socketClosed = new CountDownLatch(1);
        WebSocket.Listener listener = new WebSocket.Listener() {
            @Override
            public void onOpen(WebSocket webSocket) {
                webSocket.request(1);
            }

            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                System.out.println("Received: " + data);
                webSocket.request(1);
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                System.out.println("Closed: " + statusCode + " " + reason);
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                error.printStackTrace();
            }
        };

        HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(BINANCE_WS_URL), listener)
                .join();
        System.out.println("Before Latch");
        socketClosed.await();
    }
}
