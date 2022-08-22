package academy.mindswap.Connect4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import static academy.mindswap.Connect4.Utilities.Messages.*;

public class Server {

        private ServerSocket serverSocket;
        private ArrayList<PlayerHandler> list;

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

        private void acceptPlayer() {
            try {
                System.out.println(ACCEPTING_PLAYERS);
                Socket clientSocket = serverSocket.accept(); //blocking method
                PlayerHandler playerHandler = new PlayerHandler(clientSocket);

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
