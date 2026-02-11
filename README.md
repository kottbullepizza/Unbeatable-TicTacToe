# Tic Tac Toe Engine and Simulator

This repository features a Swing-based Tic-Tac-Toe GUI with game logic written in Java that uses bitboards for efficient state evaluation. The GUI supports human-vs-human, human-vs-engine and engine-vs-engine play. It includes a Tic-Tac-Toe engine implementing a recursive minimax algorithm for move comparison, along with a Simulator class for large-scale engine-vs-engine testing.

<img width="332" height="356" alt="tic-tac-toe-preview" src="https://github.com/user-attachments/assets/08a668dc-9554-4b1e-bf98-8c296a231004" />
<img width="158" height="356" alt="Skärmbild 2026-02-10 121910" src="https://github.com/user-attachments/assets/350e35c1-d60a-4eda-9a32-f4f54947b4fe" />


## How to Use
You can toggle players between human and engine in Main.java:

    public static boolean Xbot = false; // Set to true for X to be a bot
    public static boolean Obot = true;  // Set to true for O to be a bot

Run the Project by compiling the Java files and run Main.java.

The GUI will launch for the configured game mode, and the terminal will output simulation results for 4,000 automated games. To disable the initial simulation, comment out the Simulator code in Main.java.

## Key Features:

### Bit-board representation

Optimizing the game logic by representing the 3x3 grid as 9-bit integers allows fast move validation and win-state detection via bitwise operations. The board state is stored in two 9-bit integers, xs and os. Where xs stores the positions of X’s, while os stores the positions os O's. Each bit corresponds to a square on the board:

```text
    000
    000
    000
```

To manipulate a single bit, the integer corresponding to the square is simply added to xs or os, setting that bit to one:

```text
    256 | 128 | 64
    ---------------
     32 |  16 |  8
    ---------------
      4 |   2 |  1
```

This, in turn, allows the engine to check for three-in-a-row using predefined bit-masks (e.g., 7 for the bottom row (4 + 2 + 1), 273 for the main diagonal (256 + 16 + 1)), which is more efficient than accessing array values.

### Tic-Tac-Toe engine

The engine uses a recursive minimax search to evaluate game trees for determining the best move. Its evaluation method prioritizes moves that lead to quicker wins. For example, if the engine finds itself in a situation where two moves eventually lead to three-in-a-row, thus the engine winning, it will choose the win that requires the fewest moves, assuming best play from both sides.

### Simulator

A dedicated tool to run thousands of games between bots to compare win/loss/draw percentages and performance metrics between different game strategies.

<img width="291" height="145" alt="image" src="https://github.com/user-attachments/assets/39442ad0-0cae-4b85-a328-35df30b372fe" />


### Simulation Results

The included Simulator class provides a breakdown of performance across different agent configurations. Running the default suite (1,000 games each) typically yields:

```text
    Configuration	    X Win %	 Draw %	 O Win %
    AI vs. AI	        0%	     100%	   0%
    AI vs. Random	    99%	     1%	       0%
    Random vs. AI	    0%	     19%	   81%
    Random vs. Random	59%	     13%	   28%
```

The 'Random' engine makes a completely random move based on the available squares on the board, while the 'AI' engine uses the implemented Tic-Tac-Toe engine logic to find the best move. As the data suggests, the 'AI' engine is unbeatable.
