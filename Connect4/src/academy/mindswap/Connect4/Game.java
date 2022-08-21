package academy.mindswap.Connect4;

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

        player1.setPlayerChar('R');
        player2.setPlayerChar('Y');

        while(true){


            printBoard();
            player1.sendMessage("it's your turn, please inset move from 0 to 6");
            player2.sendMessage("It's your opponent turn.");

            receiveMove(player1);
            if(checkWinner(player1.getPlayerChar(),player1)){
                printBoard();
                checkIfWantsToPlay(player1);
                return;
            }

            printBoard();
            player2.sendMessage("it's your turn, please inset move from 0 to 6");
            player1.sendMessage("It's your opponent turn.");

            receiveMove(player2);
            if(checkWinner(player2.getPlayerChar(),player2)){
                printBoard();
                checkIfWantsToPlay(player2);
                return;
            }
        }
    }

    private void checkIfWantsToPlay(PlayerHandler player) {
        broadCast("Want to play another game?");
        playAgain(player1);
        playAgain(player2);
    }

    private void playAgain(PlayerHandler player) {

        String response = player.receiveMessage().toLowerCase();

        switch (response){
            case "yes": player.sendMessage("We need one more player, please wait.");
                player.endGame();
                server.findPlayer();
                return;
            case "no": player.sendMessage("See you tomorrow!");
                player.closeSocket();
                return;
            default: player.sendMessage("Say yes or no please...");
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

    private void printBoard() {

        for (int i = 0; i < boardGame.length; i++) {

            String line = "|";

            for (int j = 0; j < boardGame[0].length; j++) {
                line += boardGame[i][j] + "|";
            }
            broadCast(line);
        }
        broadCast("---------------\n 0 1 2 3 4 5 6\n");
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
        player.sendMessage("Please insert a valid move.");
        receiveMove(player);
    }

    private boolean playValidation(int move, PlayerHandler player) {
        if (boardGame[0][move] != ' ') {
            player.sendMessage("That column is full, pick another one.");
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
        for (int i = 0; i < boardGame.length; i++) {
            for (int j = 0; j < boardGame[0].length - 3; j++) {
                if (boardGame[i][j] == player && boardGame[i][j + 1] == player &&
                        boardGame[i][j + 2] == player && boardGame[i][j + 3] == player) {
                    broadCast(playerHandler.getPlayerName() + " won this game!");
                    return true;
                }
            }
        }

        //check column
        for (int i = 0; i < boardGame.length - 3; i++) {
            for (int j = 0; j < boardGame[0].length; j++) {
                if (boardGame[i][j] == player && boardGame[i + 1][j] == player &&
                        boardGame[i + 2][j] == player && boardGame[i + 3][j] == player) {
                    broadCast(playerHandler.getPlayerName() + " won this game!");
                    return true;
                }
            }
        }

        // diagonal down - up
        for (int i = 3; i < boardGame.length; i++) {
            for (int j = 0; j < boardGame[0].length - 3; j++) {
                if (boardGame[i][j] == player && boardGame[i - 1][j + 1] == player &&
                        boardGame[i - 2][j + 2] == player && boardGame[i - 3][j + 3] == player) {
                    broadCast(playerHandler.getPlayerName() + " won this game!");
                    return true;
                }
            }
        }

        // diagonal up - down
        for (int i = 0; i < boardGame.length - 3; i++) {
            for (int j = 0; j < boardGame[0].length - 3; j++) {
                if (boardGame[i][j] == player && boardGame[i + 1][j + 1] == player &&
                        boardGame[i + 2][j + 2] == player && boardGame[i + 3][j + 3] == player) {
                    broadCast(playerHandler.getPlayerName() + " won this game!");
                    return true;
                }
            }
        }

        for(int i = 0; i < boardGame.length;i++){
            for(int j = 0; j < boardGame[0].length; j++){
                if(boardGame[i][j] == ' '){
                    return false;
                }
                else{
                    broadCast("It's a draw guys!");
                    return true;
                }
            }
        }
        return false;
    }

    private void welcome() {
        player1.sendMessage("Let's start the game!\n");
        player2.sendMessage("Let's start the game!\n");
    }

    @Override
    public void run() {
        welcome();
        gameProcess();
    }
}
