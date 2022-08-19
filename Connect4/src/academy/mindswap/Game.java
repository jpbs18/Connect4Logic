package academy.mindswap;

import java.util.Scanner;

public class Game {

    public static String[][] createBoard() {

        String[][] board = new String[6][15];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (j % 2 == 0) {
                    board[i][j] = "|";
                }
                else {
                    board[i][j] = " ";
                }
            }
        }
        return board;
    }

    public static void printBoard(String[][] board){

        System.out.println(" 1 2 3 4 5 6 7");

        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        System.out.println("_______________");
    }

    public static void playYellow(String[][] board){

        System.out.println("You can play your move from 1 to 7.");
        Scanner scanner = new Scanner(System.in);

        int move = 2 * scanner.nextInt() - 1;

        if(move <= 0 || move > 13 || !board[0][move].equals(" ")){
            System.out.println("Play a valid move please.");
            move = 0;
            playYellow(board);
        }

        for(int i = 5; i >=0; i--){
            if(board[i][move].equals(" ")){
                board[i][move] = "Y";
                break;
            }
        }
    }

    public static void playRed(String[][] board){

        System.out.println("You can play your move from 1 to 7.");
        Scanner scanner = new Scanner(System.in);

        int move = 2 * scanner.nextInt() - 1;

        if(move <= 0 || move > 13 || !board[0][move].equals(" ")){
            System.out.println("Play a valid move please.");
            move = 0;
            playRed(board);
        }

        for(int i = 5; i >=0; i--){
            if(board[i][move].equals(" ")){
                board[i][move] = "R";
                break;
            }
        }
    }

    public static void play(String[][] array){
        printBoard(array);

        for(int i = 0; i < 100; i++){

            if(i % 2 == 0){
                playRed(array);
            }
            else{
                playYellow(array);
            }
            printBoard(array);
        }
    }

    public static String checkWinner(String[][] board){

        // rows win

        for(int i = 0; i < board.length; i++){
            for(int j = 1; j < board[i].length; j+=2){

                if((!board[i][j].equals(" ")) && (!board[i][j+2].equals(" "))
                    && (!board[i][j+4].equals(" ")) && (!board[i][j+6].equals(" "))
                    && (board[i][j].equals(board[i][j+2])) && (board[i][j+2].equals(board[i][j+4]))
                        && (board[i][j+4].equals(board[i][j+6]))){
                    return board[i][j];
                }
            }
        }

       // columns win

        for(int i = 1; i < 15; i+=2){
            for(int j = 0; j < 3; j++){

                if((!board[j][i].equals(" ")) && (!board[j+1][i].equals(" "))
                    && (!board[j+2][i].equals(" ")) && (!board[j+3][i].equals(" "))
                    && board[j][i].equals(board[j+1][i]) && board[j+1][i].equals(board[j+2][i])
                    && board[j+2][i].equals(board[j+3][i])){
                    return board[j][i];
                }
            }
        }

        // diagonal up-down left-right


        for(int i = 0; i < 3; i++){
            for(int j = 1; j < 15; j+=2){
                if()
            }
        }

//http://www.javaproblems.com/2013/01/creating-connect-four-game-in-java.html
        return null;
    }

    public static void main(String[] args) {
        String[][] array = createBoard();

        printBoard(array);

        int count = 0;
        while(true){
            if(count % 2 == 0){
                playYellow(array);
            }
            else{
                playRed(array);
            }
            count++;
            printBoard(array);

            if(checkWinner(array) != null){
                if(checkWinner(array).equals("R")){
                    System.out.println("red wins");
                    break;
                }
                else if(checkWinner(array).equals("Y")){
                    System.out.println("yellow wins");
                    break;
                }
            }
        }

    }
}
