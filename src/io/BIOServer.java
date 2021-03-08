package io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 传统IO
 *
 * @author William Lang
 * @since 2021年3月9日
 */

public class BIOServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(8848);
        // 接收到客户端连接请求之后为每个客户端创建一个新的线程进行链路处理
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // 阻塞方法获取新的连接
                    Socket socket = serverSocket.accept();
                    // 每一个新的连接都创建一个线程，负责读取数据
                    new Thread(() -> {
                        try(InputStream inputStream = socket.getInputStream()) {
                            int len;
                            byte[] data = new byte[1024];
                            // 按字节流方式读取数据
                            while ((len = inputStream.read(data)) != -1) {
                                System.out.println(new String(data, 0, len));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.join();
        thread.start();
    }
}