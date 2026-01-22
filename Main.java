public class Main {
    public static boolean Xbot = false; // set to true for X to be bot, false to be human
    public static boolean Obot = true; // set to true for O to be bot, false to be human
    /**
     * Responsible for starting the game
     */
    public static void main(String[] args) {
        GameState gs = new GameState();
        gs.play();
        
        // Simulates games between bots and prints the results
        Simulator sim = new Simulator();
        sim.botVsbot(false, false, 1000);
        sim.botVsbot(true, false, 1000);
        sim.botVsbot(false, true, 1000);
        sim.botVsbot(true, true, 1000);
    }
}