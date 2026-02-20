package org.jiring.news.feed;

import org.jiring.news.feed.config.FeedConfig;
import org.jiring.news.feed.config.FeedConfigLoader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MockNewsFeedMain {

    private static final List<String> HEADLINE_WORDS = Arrays.asList(
            "up", "down", "rise", "fall", "good", "bad", "success", "failure", "high", "low"
    );

    private static final Random RANDOM = new Random();


    public static void main(String[] args) {
        FeedConfig config = new FeedConfigLoader().load(System.getProperties());
        System.out.println(config);


        try (Socket socket = new Socket(config.host, config.port);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            while (true) {
//todo
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}