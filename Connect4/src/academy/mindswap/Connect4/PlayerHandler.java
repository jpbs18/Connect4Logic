package academy.mindswap.Connect4;

import java.io.*;
import java.net.Socket;

/**
 * This class establish the connection between server and players.
 *
 */
public class PlayerHandler {

    private Socket playerSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean isPlaying;
    private String playerName;
    private char playerChar;

    /**
     * This is a constructor method that receives one parameter and starts the Buffers.
     * @param clientSocket
     */
    public PlayerHandler(Socket clientSocket) {
        playerSocket = clientSocket;
        startBuffers();
    }

    /**
     * This method checks if the Player it is connected;
     * @return boolean
     */
    public boolean isOffline(){
        if (playerSocket == null){
            return true;
        }
        return playerSocket.isClosed();
    }

    /**
     * It is used to create the objects BufferReader and BufferWriter that allows communication .
     */
    private void startBuffers(){
        try {
            reader = new BufferedReader( new InputStreamReader(playerSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * It is in charge of sending messages to the players.
     * @param   message String
     */
    public void sendMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method reads messages from the Players in put.
     * @return
     */
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

    /**
     * It is used to change the state of the isPlaying when the game starts to true.
     */
    public void startGame(){
        isPlaying = true;
    }
    /**
     * It is used to change the state of the isPlaying when the game ends to false.
     */
    public void endGame(){
        isPlaying = false;
    }

    /**
     * This method is used to close the playerSocket.
     */
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
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}