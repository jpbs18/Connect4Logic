package academy.mindswap.Connect4;

import academy.mindswap.Connect4.Utilities.Colors;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import static academy.mindswap.Connect4.Utilities.Messages.*;

public class Server {

    private ServerSocket serverSocket;
    private ArrayList<PlayerHandler> list;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            list = new ArrayList<>();
            System.out.println(SERVER_ONLINE);
            acceptPlayer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Server socket accepts player's socket (blocking method).
     * Initializes "PlayerHandler" that accepts the client socket as parameter.
     * It calls the method drawLogo that prints for each player the logotype of the game.
     * It sends a message to each player asking for their name.
     * After receive the player's name, this method changes it.
     * After a player connects to the server, it's added to the ArrayList.
     * It calls the findPlayer method.
     * In the end, it's a recursive call.
     */
    private void acceptPlayer() {
        try {
            System.out.println(ACCEPTING_PLAYERS);
            Socket clientSocket = serverSocket.accept(); //blocking method
            PlayerHandler playerHandler = new PlayerHandler(clientSocket);

            drawLogo(playerHandler);
            playerHandler.sendMessage(PLAYER_NAME);

            playerHandler.setPlayerName(playerHandler.receiveMessage());

            playerHandler.sendMessage(playerHandler.getPlayerName().concat(IN_GAME));
            playerHandler.sendMessage(ONE_PLAYER_SHORT);

            list.add(playerHandler);

            findPlayer();
            acceptPlayer();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * It creates a file txt that has the logotype.
     * It creates a buffereredReader to read the txt file and then it prints the logotype with random colors for each player.
     * @param playerHandler
     */
    private void drawLogo(PlayerHandler playerHandler) {
        File file = new File("/Users/mac/IdeaProjects/Connect4Logic_final_final/Connect4/src/academy/mindswap/Connect4/Resources/Logotype.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String draw = "";

        while (true) {

            try {
                if ((draw = reader.readLine()) == null) {
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String colorTxt= Colors.values()[(int)(Math.random()* Colors.values().length)].getColor();
            playerHandler.sendMessage(colorTxt+ draw + RESET);
        }
    }


    public void findPlayer() {
        PlayerHandler[] array = new PlayerHandler[2];
        int counter = 0;

        for (PlayerHandler player : list) {
            if (!player.isOffline() && player != array[0] && !player.getIsPlaying()) {
                array[counter] = player;
                counter++;
            }
        }

        if (array[1] != null) {
            Game game = new Game(array, this);
            Thread thread = new Thread(game);
            thread.start();
            Arrays.stream(array).forEach(PlayerHandler::startGame);
        }
    }
}
