import java.util.ArrayList;

public class TicTacBot {
    public static void botMove(GameState gs) {
        int move = findBestMove(gs);
        if (move != -1) gs.makeMove(move);
    }

    public static void randomMove(GameState gs) {
        gs.makeMove(findRandomMove(gs));
    }

    private static int findRandomMove(GameState gs) {
        ArrayList<Integer> moves = getLegalMoves(gs);
        return moves.get((int) (Math.random() * 1000) % moves.size());
    }

    private static int findBestMove(GameState gs) {
        ArrayList<Integer> moves = getLegalMoves(gs);
        boolean botIsX = gs.getTurn(); // Is the bot currently X?
        
        int bestVal = botIsX ? -1000 : 1000;
        int bestMove = -1;

        for (int move : moves) {
            gs.makeMove(move);
            int moveVal = minimax(gs, 0, !botIsX, -1000, 1000);
            gs.undoMove(move);

            int rand = (int) (Math.random() * 1000) % 2;

            if (botIsX) {
                if (moveVal > bestVal) {
                    bestVal = moveVal;
                    bestMove = move;
                }
            } else {
                if (moveVal < bestVal) {
                    bestVal = moveVal;
                    bestMove = move;
                }
            }
        }
        return bestMove;
    }

    private static int minimax(GameState gs, int depth, boolean isMax, int alpha, int beta) {
        int score = evaluate(gs, depth);

        // If Game Over, return the score
        if (gs.isGameOver()) return score;

        ArrayList<Integer> moves = getLegalMoves(gs);

        if (isMax) {
            int best = -1000;
            for (int move : moves) {
                gs.makeMove(move);
                best = Math.max(best, minimax(gs, depth + 1, !isMax, alpha, beta));
                gs.undoMove(move);
                alpha = Math.max(alpha, best);
                if (beta <= alpha) break;
            }
            return best;
        } else {
            int best = 1000;
            for (int move : moves) {
                gs.makeMove(move);
                best = Math.min(best, minimax(gs, depth + 1, !isMax, alpha, beta));
                gs.undoMove(move);
                beta = Math.min(beta, best);
                if (beta <= alpha) break;
            }
            return best;
        }
    }

    private static int evaluate(GameState gs, int depth) {
    int winner = gs.isThreeInARow();
        if (winner != 0) {
            if (gs.getTurn()) { 
                // X's turn, so O won
                return -10 + depth;
            } else {
                // O's turn, so X won
                return 10 - depth;
            }
        }
        return 0; // Draw or game ongoing
    }

    private static ArrayList<Integer> getLegalMoves(GameState gs) {
        ArrayList<Integer> moves = new ArrayList<>();
        int occupied = gs.getXs() | gs.getOs();
        for (int i = 0; i < 9; i++) {
            int move = (int) Math.pow(2, i);
            if ((occupied & move) == 0) moves.add(move);
        }
        return moves;
    }
}