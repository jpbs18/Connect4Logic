package academy.mindswap.Connect4;

import academy.mindswap.Connect4.Utilities.Colors;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static academy.mindswap.Connect4.Utilities.Messages.*;

/**
 * Class responsible for creating a server and accepting players to start the game.
 */
public class Server {

    private ServerSocket serverSocket;
    private ArrayList<PlayerHandler> list;

    private ExecutorService threadpool;

    /**
     * It's the main method of the class Server.
     * @param args
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    /**
     * This method starts the server creating a new server socket that receives a PORT number and a pool thread.
     * It calls the method acceptPlayer.
     */
    private void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            list = new ArrayList<>();
            threadpool = Executors.newCachedThreadPool();
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
     * Draw a logo for each player in the beginning of the game with random colors.
     * @param playerHandler
     */
    private void drawLogo(PlayerHandler playerHandler) {
        File file = new File("C:\\Users\\joaos\\Desktop\\Connect4Logic\\Connect4\\src\\academy\\mindswap\\Connect4\\Resources\\Logotype.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String draw;

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


    /**
     * It creates an array of playerHandlers.
     * The game starts if the position 1 of the array created isn't null, meaning that the array is full of players
     * and ready to start a new game.
     */
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
            threadpool.submit(game);
            Arrays.stream(array).forEach(PlayerHandler::startGame);
        }
    }
}
