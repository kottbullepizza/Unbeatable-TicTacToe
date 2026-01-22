/**
 * Resonsible for game mechanics
 * by creating GameState-Objects and manipulating them
 */
public final class GameState {
    private int xs; // all X's on the board (bit-board)
    private int os; // all O's on the board (bit-board)
    private boolean xToMove; // who's turn it is

    private final int FILLED_BOARD = 511; // 111 111 111 in binary

    /**
     * Constructor of a new GameState
     * sets x-positions and o-positions to 0
     * and xToMove to true
     */
    public GameState() {
        this.xs = 0;
        this.os = 0;
        this.xToMove = true;
    }

    /**
     * Copy-constructor of a new GameState
     * sets x-positions and o-positions and xToMove to same as gs
     * @param gs any GameState object
     */
    public GameState(GameState gs) {
        this.xs = gs.getXs();
        this.os = gs.getOs();
        this.xToMove = gs.getTurn();
    }

    /**
     * Creates a new TicTacFrame (a JFrame)
     */
    public void play() {
        TicTacFrame frame = new TicTacFrame(this);
        
        ((TicTacFrame.TicTacPanel)frame.getContentPane().getComponent(0)).checkAndPerformBotMove();
    }

    /**
     * Makes a move on the board if gameOver isn't true and the move is legal,
     * and inverts who's turn it is next
     * If the move isn't legal or game is over, it instead prints the reason move isn't played
     * @param move within board perimeters and doesn't collide with another piece
     */
    public void makeMove(int move) {
        int board = getXs() | getOs();
        String error = "Error in makeMove: ";
        
        if (isGameOver()) System.out.println(error + "can't make move as game is over");
        else if (move > 256 || move < 1) {
            System.out.println(error + "attempted move is outside board perimeters");
        } else {
            boolean onlyOne = false;
            for (int i = 0; i < 9; i++) {
                if (move == (int) Math.pow(2, i)) {
                    onlyOne = true;
                    break;
                }
            }
            if (!onlyOne) System.out.println(error + "attempted multiple moves at once");
            else if ((board ^ move) < board) {
                System.out.println(error + "attempted move collides with another piece");
            } else {
                this.xs = getTurn() ? getXs() | move : this.xs;
                this.os = !getTurn() ? getOs() | move : this.os;
                this.xToMove = !this.xToMove;
            }
        }
    }

    /**
     * Undoes a move on the board, and inverts who's turn it is next (if the square wasn't empty)
     * @param move within board perimeters
     */
    public void undoMove(int move) {
        if (((this.xs | this.os) & move) != 0) { // check if the move actually exists in either X or O
            this.xs &= ~move; // clear bit in X
            this.os &= ~move; // clear bit in O
            this.xToMove = !this.xToMove; // flip turn back
        }
    }

    /**
     * Checks whether the last move led to gameOver
     * @return true 1) if there is 'three in a row' of same kind (X's or O's exclusively): 
     * on a row, column or diagonally
     * 2) if the board is filled but neither player has three in a row
     * otherwise, returns false
     */
    public boolean isGameOver() {
        if (this.isThreeInARow() == 0) return (getXs() | getOs()) == FILLED_BOARD; // returns whether the board is filled or not
        return true;
    }

    /**
     * Checks if there is three-in-a-row and returns on what row/column/diagonal
     * @return 0 if no three-in-a-row else the positions (of the pieces (bit-wise)
     * creating the three-in-a-row are located) are added together:
     * 7 = bottom row, 56 = middle row, 448 = top row,
     * 73 = right col, 146 = middle col, 292 = left col,
     * 84 = bottom left to top right diagonal, 273 = top left to bottom right diagonal
     */
    public int isThreeInARow() {
        int ps = getTurn() ? getOs() : getXs();
        for (int i = 0; i < 3; i++) {
            int row = 7 * (int) Math.pow(8, i);
            int col = 73 * (int) Math.pow(2, i);
            if ((ps & row) == row) return row;
            else if ((ps & col) == col) return col;
        }
        if ((ps & 84) == 84) return 84;
        else if ((ps & 273) == 273) return 273;
        return 0; // no three-in-a-row
    }

    /**
     * Checks who won (or draw) if game is over, otherwise prints "Error in whoWon: game isn't over" and returns -1
     * @return an int with either -1 = no one (yet), 0 = draw, 1 = X, 2 = O
     */
    public int whoWon() {
        if (!isGameOver()) {
            System.out.println("Error in whoWon: game isn't over");
            return -1;
        } else {
            if ((getXs() | getOs()) == FILLED_BOARD && this.isThreeInARow() == 0) return 0; // draw
            int whoWon = getTurn() ? 2 : 1;
            return whoWon;
        }
    }

    /**
     * Resets a GameState by clearing the X and O positions, and setting X to move
     */
    public void reset() {
        this.xs = 0;
        this.os = 0;
        this.xToMove = true;
    }

    /**
     * Retrieves on what positions there is an X in the GameState
     * Additionally 'cleans' the bits outside of the board with bitwise AND with 111111111
     * @return an int which represents the bits of the positions where there is an X
     */
    public int getXs() {
        return this.xs & FILLED_BOARD;
    }
    
    /**
     * Retrieves on what positions there is an O in the GameState
     * Additionally 'cleans' the bits outside of the board with bitwise AND with 111111111
     * @return an int which represents the bits of the positions where there is an O
     */
    public int getOs() {
        return this.os & FILLED_BOARD;
    }

    /**
     * Retrieves who's turn it is to move
     * @return a boolean: true if X is to move, false if O is to move
     */
    public boolean getTurn() {
        return this.xToMove;
    }
}