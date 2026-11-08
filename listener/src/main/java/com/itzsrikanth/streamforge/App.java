package com.itzsrikanth.streamforge;

import com.itzsrikanth.streamforge.model.ITickHandler;
import com.itzsrikanth.streamforge.persist.Postgres;
import com.itzsrikanth.streamforge.source.BinanceSocket;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CountDownLatch;

import static com.itzsrikanth.streamforge.source.BinanceSocket.BINANCE_WS_URL;

public class App {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch socketClosed = new CountDownLatch(1);
        BinanceSocket socketListener = new BinanceSocket();
        Postgres conn = new Postgres();
        WebSocket.Listener socket = socketListener.getSocket(socketClosed, conn::persist);

        HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(BINANCE_WS_URL), socket)
                .join();
        System.out.println("Before Latch");
        socketClosed.await();
    }
}
