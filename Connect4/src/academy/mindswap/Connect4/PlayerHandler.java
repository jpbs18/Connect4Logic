package academy.mindswap.Connect4;

import academy.mindswap.Connect4.Utilities.Colors;

import java.io.*;
import java.net.Socket;

public class PlayerHandler {

    private Socket playerSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean isPlaying;
    private String playerName;
    private char playerChar;
    private static int counter = 0;
    private final String colorChar;

    public PlayerHandler(Socket clientSocket) {
        playerSocket = clientSocket;
        startBuffers();
        colorChar = counter % 2 == 0 ? Colors.RED.getColor()
                : Colors.YELLOW.getColor();
        counter++;

    }

    public boolean isOffline(){
        if (playerSocket == null){
            return true;
        }
        return playerSocket.isClosed();
    }

    private void startBuffers(){
        try {
            reader = new BufferedReader( new InputStreamReader(playerSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String receiveMessage(){
        String message;
        try {
            message = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

    public void startGame(){
        isPlaying = true;
    }
    public void endGame(){
        isPlaying = false;
    }

    public void closeSocket() {
        try {
            playerSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public char getPlayerChar() {
        return playerChar;
    }

    public void setPlayerChar(char playerChar) {
        this.playerChar = playerChar;
    }

    public String getPlayerName() {
        return playerName.toUpperCase();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getColorChar() {
        return colorChar;
    }
}