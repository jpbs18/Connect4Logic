package academy.mindswap.Connect4;

import academy.mindswap.Connect4.Utilities.Messages;

import java.io.IOException;
import java.net.Socket;

public class Testes{

    private Game game;
    PlayerHandler[] array ;
    Server server;
    private Socket playerSocket;

    private int[][] h = new int[6][7];


    public static void main(String[] args) {


    }
    public boolean testBoardSize() {

        try {
            playerSocket= new Socket(Messages.HOST, Messages.PORT);// refuse connection
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        PlayerHandler player = new PlayerHandler(playerSocket);

        PlayerHandler player1=new PlayerHandler(playerSocket);;



        this.array[0]=player;
        this.array[1]=player1;

        game = new Game(array,server);
        if (game.createBoard().length == h.length|| game.createBoard()[0].length==h[0].length) {
            return true;
        }
        return false;
    }

    private int checkLastMove(){
        // verify last move;

        return 0;
    }
 /*
    private void testSocket(){
        // test socket it is open
    }
*/
}

