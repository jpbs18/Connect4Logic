package academy.mindswap.Connect4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {

        private ServerSocket serverSocket;
        private ArrayList<PlayerHandler> list;

        private void start() {
            try {
                serverSocket = new ServerSocket(8080);
                list = new ArrayList<>();
                System.out.println("Server is online.");
                acceptPlayer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void acceptPlayer() {
            try {
                System.out.println("Accepting players...");
                Socket clientSocket = serverSocket.accept(); //blocking method
                PlayerHandler playerHandler = new PlayerHandler(clientSocket);

                playerHandler.sendMessage("What's your name?");

                playerHandler.setPlayerName(playerHandler.receiveMessage());

                playerHandler.sendMessage(playerHandler.getPlayerName().concat(" you're now in the game!"));
                playerHandler.sendMessage("We need one more player to start the game, please wait.");

                list.add(playerHandler);

                findPlayer();
                acceptPlayer();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        void findPlayer() {
            PlayerHandler[] array = new PlayerHandler[2];
            int counter = 0;

            for(PlayerHandler player : list) {
                if(!player.isOffline() && player != array[0] && !player.getIsPlaying()){
                    array[counter] = player;
                    counter++;
                }
            }

            if(array[1] != null) {
                Game game = new Game(array, this);
                Thread thread = new Thread(game);
                thread.start();
                Arrays.stream(array).forEach(PlayerHandler::startGame);
            }
        }


        public static void main(String[] args) {
            Server server = new Server();
            server.start();
        }
}
