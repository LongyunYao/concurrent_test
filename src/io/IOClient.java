package io;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * IO客户端
 *
 * @author William Lang
 * @since 2021年3月9日
 */

public class IOClient {
    public static void main(String[] args) {
        Runnable sender = () -> {
            Scanner scanner = new Scanner(System.in);
            try (Socket socket = new Socket("127.0.0.1", 8848)) {
                while (!Thread.currentThread().isInterrupted() && scanner.hasNext()) {
                    System.out.println("IOClient send a msg, socket's info -- Client:" + socket.getLocalSocketAddress());
                    socket.getOutputStream().write(scanner.next().getBytes(StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        new Thread(sender).start();
    }
}