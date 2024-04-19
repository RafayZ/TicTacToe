import java.util.*;

class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + (x + 1) + ", " + (y + 1) + "]";
    }
}

class PointsAndScores { // Class to store points and the scores
    int score;
    Point point;

    PointsAndScores(int score, Point point) {
        this.score = score;
        this.point = point;
    }
}

class Board { // Base class
    List<Point> availablePoints;
    int[][] board = new int[5][5];

    public Board() {
    }

    public boolean isGameOver() {
        return (hasXWon() || hasOWon() || getAvailablePoints().isEmpty());
    }

    public boolean hasXWon() { // Method to check if the AI player has won
        return hasPlayerWon(1); // 1 represents X which is the AI player
    }

    public boolean hasOWon() { // Method to check if the human player has won
        return hasPlayerWon(2); // 2 represents O which is the human player
    }

    private boolean hasPlayerWon(int player) { // Method to iterate through colums, rows and diagonals
        // Check rows and columns
        for (int i = 0; i < 5; i++) {
            if (checkFiveInARow(player, i, 0, 0, 1) || checkFiveInARow(player, 0, i, 1, 0))
                return true;
        }

        // Check diagonals
        return checkFiveInARow(player, 0, 0, 1, 1) || checkFiveInARow(player, 0, 4, 1, -1);
    }

    private boolean checkFiveInARow(int player, int x, int y, int dx, int dy) { // Method to check if any player got a 5 in a row
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (board[x + i * dx][y + i * dy] == player) {
                count++;
                if (count == 5) return true;
            } else {
                count = 0;
            }
        }
        return false;
    }

    public List<Point> getAvailablePoints() { // Get all the points in the game board
        availablePoints = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Point(i, j));
                }
            }
        }
        return availablePoints;
    }

    public boolean isMoveValid(Point point) { // Check if the point is within the board bounds
        if (point.x >= 0 && point.x < 5 && point.y >= 0 && point.y < 5) {
            return board[point.x][point.y] == 0; // Check if the cell is empty (0 indicates an empty cell)
        }
        return false; // The move is invalid if its already occupied
    }

    public void placeAMove(Point point, int player) { // Method to place a move
        board[point.x][point.y] = player;
    }

    public void displayBoard() { // Method to display the board
        System.out.println();

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                if (board[i][j] == 1)
                    System.out.print("X ");
                else if (board[i][j] == 2)
                    System.out.print("O ");
                else
                    System.out.print(". ");
            }
            System.out.println();
        }
    }

    public void clearBoard() { // Method to clear the board to restart the game
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = 0; // Set all positions to 0 (empty)
            }
        }
    }

    public void resetBoard() { // Reset everything
        clearBoard(); // Clear the board
        availablePoints = null; // Reset the available points list
    }

}