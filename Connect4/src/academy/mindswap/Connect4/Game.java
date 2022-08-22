package academy.mindswap.Connect4;

import academy.mindswap.Connect4.Utilities.Colors;

import java.io.*;

import static academy.mindswap.Connect4.Utilities.Messages.*;

public class Game implements Runnable{

    private final PlayerHandler player1;
    private final PlayerHandler player2;
    private final Server server;
    private char[][] boardGame;


    public Game(PlayerHandler[] array, Server server) {
        this.server = server;
        player1 = array[0];
        player2 = array[1];
        boardGame = createBoard();
    }
    private void gameProcess(){

        player1.setPlayerChar(R);
        player2.setPlayerChar(Y);


        while(true){

            printBoard(player1);
            player1.sendMessage(YOUR_TURN);
            player2.sendMessage(OPPONENT_TURN);

            receiveMove(player1);
            if(checkWinner(player1.getPlayerChar(),player1)){
                printBoard(player1);
                checkIfWantsToPlay(player1);
                return;
            }

            printBoard(player2);
            player2.sendMessage(YOUR_TURN);
            player1.sendMessage(OPPONENT_TURN);

            receiveMove(player2);
            if(checkWinner(player2.getPlayerChar(),player2)){
                printBoard(player2);
                checkIfWantsToPlay(player2);
                return;
            }
        }
    }

    private void checkIfWantsToPlay(PlayerHandler player) {
        broadCast(WANT_TO_PLAY);
        playAgain(player1);
        playAgain(player2);
    }

    private void playAgain(PlayerHandler player) {

        String response = player.receiveMessage().toLowerCase();

        switch (response){
            case YES:
                player.sendMessage(NEED_A_PLAYER);
                player.endGame();
                server.findPlayer();
                return;
            case NO:
                player.sendMessage(GOODBYE);
                player.closeSocket();
                return;
            default:
                player.sendMessage(INPUT_NOT_VALID);
                playAgain(player);
        }
    }

    private void broadCast(String message) {
        player1.sendMessage(message);
        player2.sendMessage(message);
    }

    private char[][] createBoard() {

        char[][] array = new char[6][7];

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = ' ';
            }
        }
        return array;
    }

    private void printBoard(PlayerHandler player) {

        for (int i = 0; i < boardGame.length; i++) {

            StringBuilder line = new StringBuilder(Colors.BLUE.getColor()+ "|");

            for (int j = 0; j < boardGame[0].length; j++) {
                line.append(player.getColorChar()).append(boardGame[i][j]).append(RESET).
                        append(Colors.BLUE.getColor()).append("|");
            }
            broadCast(line.toString());
        }
        broadCast(BOARD_LIMITATION+ RESET);
    }

    private void receiveMove(PlayerHandler player){

        String move = player.receiveMessage();

        if(move.matches("^[0-6]$")){
            if(!playValidation(Integer.parseInt(move),player)){
                receiveMove(player);
            }
            play(Integer.parseInt(move), player.getPlayerChar());
            return;
        }
        player.sendMessage(VALID_MOVE);
        receiveMove(player);
    }

    private boolean playValidation(int move, PlayerHandler player) {
        if (boardGame[0][move] != ' ') {
            player.sendMessage(COLUMN_FULL);
            return false;
        }
        return true;
    }

    private void play(int move, char playerChar) {

        for (int i = 5; i >= 0; i--) {
            if (boardGame[i][move] == ' ') {
                boardGame[i][move] = playerChar;
                break;
            }
        }
    }

    private boolean checkWinner(char player, PlayerHandler playerHandler) {

        //check row
        for (char[] value : boardGame) {
            for (int j = 0; j < boardGame[0].length - 3; j++) {
                if (value[j] == player && value[j + 1] == player &&
                        value[j + 2] == player && value[j + 3] == player) {
                    broadCast(playerHandler.getPlayerName() + WIN);
                    return true;
                }
            }
        }

        //check column
        for (int i = 0; i < boardGame.length - 3; i++) {
            for (int j = 0; j < boardGame[0].length; j++) {
                if (boardGame[i][j] == player && boardGame[i + 1][j] == player &&
                        boardGame[i + 2][j] == player && boardGame[i + 3][j] == player) {
                    broadCast(playerHandler.getPlayerName() + WIN);
                    return true;
                }
            }
        }

        // diagonal down - up
        for (int i = 3; i < boardGame.length; i++) {
            for (int j = 0; j < boardGame[0].length - 3; j++) {
                if (boardGame[i][j] == player && boardGame[i - 1][j + 1] == player &&
                        boardGame[i - 2][j + 2] == player && boardGame[i - 3][j + 3] == player) {
                    broadCast(playerHandler.getPlayerName() + WIN);
                    return true;
                }
            }
        }

        // diagonal up - down
        for (int i = 0; i < boardGame.length - 3; i++) {
            for (int j = 0; j < boardGame[0].length - 3; j++) {
                if (boardGame[i][j] == player && boardGame[i + 1][j + 1] == player &&
                        boardGame[i + 2][j + 2] == player && boardGame[i + 3][j + 3] == player) {
                    broadCast(playerHandler.getPlayerName() + WIN);
                    return true;
                }
            }
        }

        for (char[] chars : boardGame) {
            for (int j = 0; j < boardGame[0].length; j++) {
                if (chars[j] == ' ') {
                    return false;
                }
            }
        }
        broadCast(DRAW);
        return true;
    }

    private void welcome() {
        broadCast(START_GAME);
    }



    @Override
    public void run() {
        welcome();
        gameProcess();
    }
}
