public class Simulator {
    private boolean XAI;
    private boolean OAI;
    
    private int n; // number of games played
    private int Xwins; // number of games X won
    private int Owins; // number of games O won
    private int Draws; // number of games ended in a draw

    /**
     * Constructor for a new Simulator
     */
    public Simulator() {
        this.XAI = false;
        this.OAI = false;
        this.n = 0;
        this.Xwins = 0;
        this.Owins = 0;
        this.Draws = 0;
    }

    /**
     * Simulates a number of games between bots and stores the results, and prints the results
     * @param xAi set to true for X-player to be AI, false for random
     * @param oAi set to true for O-player to be AI, false for random
     * @param m number of games to simulate
     */
    public void botVsbot(boolean xAi, boolean oAi, int m) {
        this.XAI = xAi;
        this.OAI = oAi;
        this.reset();

        // Main.Xbot = true;
        // Main.Obot = true;

        GameState gsBots = new GameState();

        while (m > 0) {
            if (gsBots.isGameOver()) {
                if (gsBots.whoWon() == 1) this.Xwins++;
                else if (gsBots.whoWon() == 2) this.Owins++;
                else this.Draws++;

                gsBots.reset();
                m--;
                this.n++;
            }
            if (gsBots.getTurn()) {
                if (this.XAI) { TicTacBot.botMove(gsBots); }
                else TicTacBot.randomMove(gsBots);
            } else {
                if (this.OAI) { TicTacBot.botMove(gsBots); }
                else TicTacBot.randomMove(gsBots);
            }
        }

        System.out.println(this.toString());
    }

    /**
     * resets the Simulator by clearing (setting to zero) all it's stored data
     */
    public void reset() {
        this.n = 0;
        this.Xwins = 0;
        this.Owins = 0;
        this.Draws = 0;
    }

    public String toString() {
        String str = this.XAI ? this.OAI ? "AI vs. AI\n" : "AI vs. Random\n" : this.OAI ? "Random vs. AI\n" : "Random vs. Random\n";
        str += "Number of games: " + Integer.toString(this.n) + "\n";
        str += "X wins: " + Integer.toString(this.Xwins) + " " + Double.toString(((double) this.Xwins / this.n) * 100) + "%" + "\n";
        str += "Draws:  " + Integer.toString(this.Draws) + " " + Double.toString(((double) this.Draws / this.n) * 100) + "%" + "\n";
        str += "O wins: " + Integer.toString(this.Owins) + " " + Double.toString(((double) this.Owins / this.n) * 100) + "%" + "\n";
        return str;
    }
}