/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.code4people.echoserver;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EchoServer {

    public static final int PORT = 5000;

    public static void main(String[] args) throws IOException, InterruptedException {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + PORT);
            System.exit(1);
        }

        System.out.println("Waiting for connection.....");

        ExecutorService executor = Executors.newCachedThreadPool();

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                System.out.println("Incoming connection");

                Future future = executor.submit(() -> {
                    try {
                        runReading(clientSocket);
                    }
                    catch (Exception ex) {

                    }
                });
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);

                break;
            }
        }
    }

    private static void runReading(Socket clientSocket) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();

        System.out.println("Connection successful");
        System.out.println("Waiting for input.....");

        OutputStream os = clientSocket.getOutputStream();
        InputStream is = clientSocket.getInputStream();

        System.out.println("Creating buffer");

// byte[] buffer = new byte[1024 1024 10];
// for (int i = 0; i < buffer.length; i++) {
// buffer[i] = (byte) (i % 256 - 126);
// }
// 
// System.out.println("Writing buffer");
// 
// //for (int i = 0; i < 1024 * 10; i++) {
// os.write(buffer);
// //}
// 
// os.flush();

        System.out.println("--------------");
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while((bytesRead = is.read(buffer)) != -1) {
            System.out.println("incomming bytes: " + bytesRead);
            for (int i = 0; i < bytesRead; i++) {
                System.out.print(buffer[i] + "-");
            }
            os.write(buffer, 0, bytesRead);
        }
        System.out.println("--------------");
        System.out.println("Finished");
        long endTime = System.currentTimeMillis();

        System.out.println(endTime - startTime);
//Thread.sleep(1000);

        clientSocket.shutdownInput();
        clientSocket.shutdownOutput();

        Thread.sleep(1000);

        os.close();
        is.close();

        clientSocket.close();
    }
}