package academy.mindswap.Connect4;

import java.io.*;
import java.net.Socket;

public class Player {

    private Socket socket;
    private BufferedReader serverReader;
    private BufferedReader consoleReader;
    private BufferedWriter serverWriter;

    public static void main(String[] args) {
        Player player = new Player();
        player.handleServer();
    }

    private void setServer() {
        String host = "localhost";
        int port = 8080;
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            System.out.println("Server is with problems. Try again later.");
            setServer();
        }
    }

    private void serverWrite() {
        try {
            serverWriter.write(consoleReader.readLine());
            serverWriter.newLine();
            serverWriter.flush();
            serverWrite();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startBuffers() {
        try {
            serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleServer() {
        setServer();
        startBuffers();
        ServerListener serverListener = new ServerListener();
        Thread thread = new Thread(serverListener);
        thread.start();
        serverWrite();
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class ServerListener implements Runnable {

        private void listenServer() {
            try {
                String message = serverReader.readLine();
                if (message == null) {
                    serverWriter.close();
                    return;
                }
                System.out.println(message);
                listenServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            listenServer();
        }
    }
}
