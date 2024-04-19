import java.util.Scanner;

public class TicTacToe {
    private final Board board;
    private final AIplayer aiPlayer;
    private final Scanner scanner;
    private static final int PLAYER_X = 1; // Assuming player 'X' is Computer
    private static final int PLAYER_O = 2; // Assuming player 'O' is human player
    private int AI_counter; // Counter to make keep track of computer scores
    private int PLAYER_counter; // Counter to make keep track of human scores

    public TicTacToe() {
        board = new Board();
        aiPlayer = new AIplayer();
        scanner = new Scanner(System.in);
    }

    private void initGame() { //Method for the Main menu screen
        System.out.println("Welcome to Tic Tac Toe Challenger!");
        board.displayBoard();
        System.out.println("Who makes the first move? Choose (1) for AI player and (2) for User: ");
        int choice = scanner.nextInt();
        if (choice == 1) {
            aiMove();
        }
    }

    private void playerMove() { // Method to check if human players move is valid and then place it
        boolean validMove = false;
        while (!validMove) {
            System.out.println("Current board Score: " + AIplayer.evaluateBoard(board));
            System.out.println("Your move: line and column numbers (1-5) ");
            int x = scanner.nextInt() - 1;
            int y = scanner.nextInt() - 1;
            Point move = new Point(x, y);

            if (board.isMoveValid(move)) {
                board.placeAMove(move, PLAYER_O);
                validMove = true;
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
        board.displayBoard();
    }

    private void aiMove() { // Method to call minimax and place the AI players move
        aiPlayer.callMinimax(0, PLAYER_X, board);
        board.placeAMove(aiPlayer.returnBestMove(), PLAYER_X);
        board.displayBoard();
    }

    private void checkGameOver() { // Method to access isGameOver method in Board class to check if any player has won or draw
        if (board.isGameOver()) {
            displayScore(); // Display the current score
            System.out.println("Do you want a rematch? (1) Yes (2) No");
            int choice = scanner.nextInt();
            if (choice == 1) {
                board.resetBoard(); // Reset the board for a new game
                startGame(); // Restart the game
            } else {
                System.out.println("Thank you for playing!");
                scanner.close();
                System.exit(0);
            }
        }
    }

    private void displayScore() { // Method to display the both players scores
        if (board.hasXWon()) {
            AI_counter++;
            System.out.println("AI wins this round!");
        } else if (board.hasOWon()) {
            PLAYER_counter++;
            System.out.println("You win this round!");
        } else {
            System.out.println("It's a draw!");
        }
        System.out.println("Current Score - AI: " + AI_counter + ", You: " + PLAYER_counter); // Display game scores
    }

    public void startGame() { // Method to start the game
        board.clearBoard(); // Ensure the board is clear at the start of the game
        initGame(); // Start the game
        while (!board.isGameOver()) {
            playerMove();
            if (board.isGameOver()) {
                checkGameOver();
                break;
            }
            aiMove();
            checkGameOver();
        }
    }

    public static void main(String[] args) {
        new TicTacToe().startGame();
    }
}
