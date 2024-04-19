import java.util.*;

class AIplayer{
    List<PointsAndScores> rootsChildrenScores;

    public AIplayer() {
    }

    // Method to return the best move based on the minimax algorithm
    public Point returnBestMove() {
        int MAX = -100000;
        int best = -1;

        // Iterate through all possible moves and choose the one with the highest score
        for (int i = 0; i < rootsChildrenScores.size(); ++i) {
            if (MAX < rootsChildrenScores.get(i).score) {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
            }
        }
        return rootsChildrenScores.get(best).point;
    }

    // Wrapper method to call the minimax algorithm
    public void callMinimax(int depth, int turn, Board b){
        rootsChildrenScores = new ArrayList<>();
        minimax(depth, turn, b, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    // The minimax algorithm with alpha-beta pruning
    // Evaluates all possible moves and returns the best score for the current player
    public int minimax(int depth, int turn, Board b, int alpha, int beta) {
        if (b.hasXWon()) return +1000; // AI player wins
        if (b.hasOWon()) return -1000; // Human player wins
        List<Point> pointsAvailable = b.getAvailablePoints();
        if (pointsAvailable.isEmpty()) return 0; // Game is a draw

        // Heuristic evaluation for depth cutoff
        if (depth == 7) {
            return evaluateBoard(b); // Evaluate board when the depth limit is reached
        }

        if (turn == 1) { // Maximizing player (AI)
            int maxEval = Integer.MIN_VALUE;
            for (Point point : pointsAvailable) {
                b.placeAMove(point, 1); // Try move
                int eval = minimax(depth + 1, 2, b, alpha, beta); // Recursive minimax call
                if (depth == 0) rootsChildrenScores.add(new PointsAndScores(eval, point)); // Store move and score for root depth
                maxEval = Math.max(maxEval, eval); // Update maxEval if eval is larger
                alpha = Math.max(alpha, eval); // Update alpha value for alpha-beta pruning
                b.placeAMove(point, 0); // Undo move
                if (beta <= alpha) break; // Alpha-beta pruning cutoff
            }
            return maxEval;
        } else { // Minimizing player (Human)
            int minEval = Integer.MAX_VALUE;
            for (Point point : pointsAvailable) {
                b.placeAMove(point, 2); // Try move
                int eval = minimax(depth + 1, 1, b, alpha, beta); // Recursive minimax call
                minEval = Math.min(minEval, eval); // Update minEval if eval is smaller
                beta = Math.min(beta, eval); // Update beta value for alpha-beta pruning
                b.placeAMove(point, 0); // Undo move
                if (beta <= alpha) break; // Alpha-beta pruning cutoff
            }
            return minEval;
        }
    }

    // Heuristic evaluation function for the board
    // Scores the board based on the number of consecutive X's or O's in rows, columns, and diagonals
    public static int evaluateLine(int c1, int c2, int c3, int c4, int c5) {
        int score = 0;
        int[] line = {c1, c2, c3, c4, c5};
        int countX = 0;
        int countO = 0;

        for (int cell : line) {
            if (cell == 1) { // AI player 'X'
                countX++;
            } else if (cell == 2) { // Human player 'O'
                countO++;
            }
        }

        if (countX == 5) {
            score = (int) Math.pow(10, 5); // Win condition
        } else if (countX == 0 && countO > 0) {
            score = -(int) Math.pow(10, countO); // O's advantage
        } else if (countO == 0 && countX > 0) {
            score = (int) Math.pow(10, countX); // X's advantage
        }

        return score;
    }

    // Evaluates the entire board by summing up the scores from evaluateLine method for all rows, columns, and diagonals
    public static int evaluateBoard(Board b) {
        int score = 0;

        // Check all rows and columns
        for (int i = 0; i < 5; i++) {
            score += evaluateLine(b.board[i][0], b.board[i][1], b.board[i][2], b.board[i][3], b.board[i][4]);
            score += evaluateLine(b.board[0][i], b.board[1][i], b.board[2][i], b.board[3][i], b.board[4][i]);
        }

        // Check the two diagonals
        score += evaluateLine(b.board[0][0], b.board[1][1], b.board[2][2], b.board[3][3], b.board[4][4]);
        score += evaluateLine(b.board[0][4], b.board[1][3], b.board[2][2], b.board[3][1], b.board[4][0]);

        return score;
    }
}
