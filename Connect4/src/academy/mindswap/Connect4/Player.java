package academy.mindswap.Connect4;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import static academy.mindswap.Connect4.Utilities.Messages.*;

/**
 * This class is responsible for creating players for the game.
 */
public class Player {

    private Socket socket;
    private BufferedReader serverReader;
    private BufferedReader consoleReader;
    private BufferedWriter serverWriter;
    private final Semaphore semaphore = new Semaphore(1);


    /**
     * This is the main method of the class Player where are created the players.
     * It calls the handleServer method.
     * @param args an array of Strings.
     */
    public static void main(String[] args) {
        Player player = new Player();
        player.handleServer();
    }

    /**
     * It creates the socket that receives the hostName and Port.
     */
    private void setServer() {
        String host = HOST;
        int port = PORT;
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            System.out.println(SERVER_PROBLEMS);
            setServer();
        }
    }


    /**
     * It's the method responsible for writing messages to the server.
     */
    private void serverWrite() {
        try {
            semaphore.acquire();
            System.in.read(new byte[System.in.available()]);
            serverWriter.write(consoleReader.readLine());
            serverWriter.newLine();
            serverWriter.flush();
            serverWrite();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is responsible for starting the communications.
     */
    private void startBuffers() {
        try {
            serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is responsible for calling all the previous methods and creating a serverListener object.
     * It starts a new thread  with the serverListener.
     */
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

    /**
     * This inner class that implements Runnable Interface is used for creating serverListener objects.
     * Since Runnable is a functional Interface(only 1 method), only the method run has to be overridden.
     */
    private class ServerListener implements Runnable {

        /**
         * This method is responsible for listening the messages from the server.
         */
        private void listenServer() {
            try {
                String message = serverReader.readLine();
                if (message == null) {
                    serverWriter.close();
                    return;
                }
                System.out.println(message);
                if(message.equals(YOUR_TURN) || message.equals(WANT_TO_PLAY) || message.equals(COLUMN_FULL)
                        || message.equals(INPUT_NOT_VALID) || message.equals(VALID_MOVE)){
                    semaphore.release();
                }
                listenServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * As soon as the thread starts, the code inside this method is executed.
         */
        @Override
        public void run() {
            listenServer();
        }
    }
}
