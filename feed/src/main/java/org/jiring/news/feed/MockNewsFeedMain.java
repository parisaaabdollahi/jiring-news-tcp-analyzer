package org.jiring.news.feed;

import org.jiring.news.common.config.AddressConfig;
import org.jiring.news.common.dto.NewsItemDto;
import org.jiring.news.common.utility.JsonLinesMessageCodec;
import org.jiring.news.feed.config.FeedConfigLoader;
import org.jiring.news.feed.core.RandomNewsItemGenerator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Random;

public class MockNewsFeedMain {


    private static final Random RANDOM = new Random();


    public static void main(String[] args) {

        AddressConfig config = new FeedConfigLoader().load
                (System.getProperties());
        System.out.println(config);


        try (Socket socket = new Socket(config.host, config.port);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            while (true) {
                NewsItemDto newsItem = RandomNewsItemGenerator.generate(RANDOM);
                String jsonLine = JsonLinesMessageCodec.encode(newsItem);
                writer.write(jsonLine);
                writer.flush();
                System.out.println("Sent: " + newsItem);

                Thread.sleep(config.intervalMs);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}