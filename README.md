# Unbeatable Tic Tac Toe Engine & Simulator

A bitboard based Tic-Tac-Toe engine written in Java. This project features a Swing-based GUI, an unbeatable game-playing bot powered by a Minimax search algorithm, and a simulator for large-scale bot vs. bot testing.

## Key Features:

### Bit-board representation

Optimized game logic representing the 3x3 grid as 9-bit integers, allowing for fast move validation and win-state detection via bitwise operations. The board state is stored in two primary integers, xs and os. Each bit corresponds to a square on the board:

```text
    000
    000
    000
```

To manipulate a single bit, the number corresponding to the square is simply added to the xs or os:

```text
    256 | 128 | 64
    ---------------
     32 |  16 |  8
    ---------------
      4 |   2 |  1
```

This allows the engine to check for "Three in a Row" using predefined bit-masks (e.g., 7 for the bottom row, 273 for the main diagonal).

### Unbeatable Tic-Tac-Toe bot

A computer opponent using a recursive Minimax search. It evaluates game trees to ensure it never loses when playing optimally and makes sure to win whenever there is the opportunity.


This supports Human vs. Human, Human vs. Bot and Bot vs. Bot play.

### Simulator

A dedicated tool to run thousands of games between bots to compare win/loss/draw percentages and performance metrics between their game strategies.

## Simulation Results

The included Simulator class provides a breakdown of performance across different agent configurations. Running the default suite (1,000 games each) typically yields:

```text
    Configuration	    X Win %	 Draw %	 O Win %
    AI vs. AI	        0%	     100%	   0%
    AI vs. Random	    99%	     1%	       0%
    Random vs. AI	    0%	     19%	   81%
    Random vs. Random	59%	     13%	   28%
```

### How to Use
You can toggle players between human and bot in Main.java:

    public static boolean Xbot = false; // Set to true for X to be a bot
    public static boolean Obot = true;  // Set to true for O to be a bot

Run the Project by

Compiling the Java files.

Run Main.main().

The GUI will launch for the configured game mode, and the terminal will output simulation results for 4,000 automated games. To disable the initial simulation, comment out the Simulator code in Main.java.
