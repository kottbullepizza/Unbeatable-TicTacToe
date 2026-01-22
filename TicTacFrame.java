import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Responsible for the user interface of the game
 */
public class TicTacFrame extends JFrame {
    private static final int DIMENSION = 3; //3x3 board
    private static final int SQ_SIZE = 150;
    private static final int SQ_PADDING = 20;
    private static final Color LIGHT = Color.decode("#fcfbf4"); // for board squares
    private static final Color DARK  = Color.decode("#1b1814"); // for square borders
    private static final Color X_COLOR = Color.decode("#da4b2b"); // for X's color
    private static final Color O_COLOR = Color.decode("#3e64d6ff"); // for O's color

    public TicTacFrame(GameState gs) {
        ImageIcon icon = new ImageIcon("tic-tac-toe-icon.jpg"); // creates an ImageIcon

        this.setTitle("Tic Tac Toe");
        this.setIconImage(icon.getImage());
        this.setResizable(false);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        TicTacPanel panel = new TicTacPanel(gs, DIMENSION*SQ_SIZE, LIGHT, DARK);
        this.add(panel);
        this.pack();

        this.setVisible(true);
    }

    public class TicTacPanel extends JPanel implements MouseListener, MouseMotionListener {
        private final GameState gs;
        private final Color LIGHT;
        private final Color DARK;
        private int x, y = 0;

        public TicTacPanel(GameState gs, int panelSize, Color light, Color dark) {
            this.gs = gs;
            this.LIGHT = light;
            this.DARK = dark;
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            setPreferredSize(new java.awt.Dimension(panelSize, panelSize));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
                for (int rank = 0; rank < DIMENSION; rank++) {
                    for (int file = 0; file < DIMENSION; file++) {
                        // Draw board-squares
                        g.setColor(LIGHT);
                        g.fillRect(file * SQ_SIZE, rank * SQ_SIZE, SQ_SIZE, SQ_SIZE);
                        g.setColor(DARK);
                        g.drawRect(file * SQ_SIZE, rank * SQ_SIZE, SQ_SIZE, SQ_SIZE);
                    }
                }

                // Draw pieces
                int xs = gs.getXs();
                int os = gs.getOs();

                for (int row = 0; row < 3; row++) { // row
                    for (int col = 0; col < 3; col++) { // col
                        int mathConst = (int) Math.pow(2, col + 3*row);
                        int x = col > 0 ? col == 2 ? SQ_PADDING : SQ_PADDING + SQ_SIZE : SQ_PADDING + 2*SQ_SIZE;
                        int y = row > 0 ? row == 2 ? SQ_PADDING : SQ_PADDING + SQ_SIZE : SQ_PADDING + 2*SQ_SIZE;
                        if ((xs & mathConst) == mathConst) {
                            this.drawX(g, x, y, SQ_SIZE - 2*SQ_PADDING);
                        } else if ((os & mathConst) == mathConst) {
                            this.drawO(g, x, y, SQ_SIZE - 2*SQ_PADDING);
                        }
                    }
                }

                if (gs.isGameOver()) {
                    this.drawEndText(g);
                }
            }

        private void drawX(Graphics g, int x, int y, int pieceSize) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform old = g2d.getTransform();
            final int WIDTH = 40;
            
            g2d.setColor(X_COLOR);
            g2d.rotate(Math.toRadians(45), x + pieceSize/2, y + pieceSize/2);
            g2d.fillRect(x + (pieceSize - WIDTH)/2, y, WIDTH, pieceSize);
            g2d.fillRect(x , y + (pieceSize - WIDTH)/2, pieceSize, WIDTH);

            g2d.setTransform(old); // rotates back
        }

        private void drawO(Graphics g, int x, int y, int pieceSize) {
            final int HOLE_WIDTH = 50;

            g.setColor(O_COLOR);
            g.fillOval(x, y, pieceSize, pieceSize);

            g.setColor(LIGHT);
            g.fillOval(x + (pieceSize - HOLE_WIDTH)/2, y + (pieceSize - HOLE_WIDTH)/2, HOLE_WIDTH, HOLE_WIDTH);
        }

        private void drawEndText(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform old = g2d.getTransform();

            String endString = "";
            int whoWon = gs.whoWon();
            int pos = gs.isThreeInARow();
            int y = 0;

            if (whoWon == 1) { // X's won
                g.setColor(X_COLOR);
                endString = "X's won";
            } else if (whoWon == 2) { // O's won
                g.setColor(O_COLOR);
                endString = "O's won";
            } else { // Draw
                g.setColor(Color.GRAY);
                endString = "Draw";
            }

            if (pos == 7 || pos == 56 || pos == 448) { // a row
                int n = pos > 56 ? 0 : pos == 56 ? 1 : 2;
                y = SQ_SIZE/2 - 2*SQ_PADDING + SQ_SIZE * n;
            } else if (pos == 73 || pos == 146 || pos == 292) { // col
                int n = pos > 146 ? 2 : pos == 146 ? 1 : 0;
                y = SQ_SIZE/2 - 2*SQ_PADDING + SQ_SIZE * n;

                g2d.rotate(Math.toRadians(90), getWidth()/2, getWidth()/2);
            } else if (pos == 84 || pos == 273) { // diagonal
                y = SQ_SIZE/2 - 2*SQ_PADDING + SQ_SIZE;
                if (pos == 84) {
                    g2d.rotate(Math.toRadians(315), getWidth()/2, getWidth()/2);
                } else {
                    g2d.rotate(Math.toRadians(45), getWidth()/2, getWidth()/2);
                }
            } else { y = SQ_SIZE/2 - 2*SQ_PADDING + SQ_SIZE; }

            g.fillRect(-SQ_SIZE, y, 5*SQ_SIZE, 4*SQ_PADDING);
            g.setColor(LIGHT);
            g.setFont(new Font("Serif", Font.BOLD, 60));
            int strWidth = 50 + (endString.length()-1) * 25;
            g.drawString(endString, (3*SQ_SIZE - strWidth)/2, y + 60);

            g2d.setTransform(old); // rotates back if it was rotated
        }

        @Override
        public void mousePressed(MouseEvent e) {
            boolean humanToMove = (gs.getTurn() && !Main.Xbot) || (!gs.getTurn() && !Main.Obot);
            if (!humanToMove || gs.isGameOver()) return;

            x = e.getX();
            y = e.getY();
            System.out.println("X: " + x + " Y: " + y);

            if (x > 0 && x < SQ_SIZE*DIMENSION && y > 0 && y < SQ_SIZE*DIMENSION) {
                int newMove = x < SQ_SIZE && y < SQ_SIZE ? 256 :
                x < 2*SQ_SIZE && y < SQ_SIZE ? 128 :
                x < 3*SQ_SIZE && y < SQ_SIZE ? 64 :
                x < SQ_SIZE && y < 2*SQ_SIZE ? 32 :
                x < 2*SQ_SIZE && y < 2*SQ_SIZE ? 16 :
                x < 3*SQ_SIZE && y < 2*SQ_SIZE ? 8 :
                x < SQ_SIZE && y < 3*SQ_SIZE ? 4 :
                x < 2*SQ_SIZE && y < 3*SQ_SIZE ? 2 :
                1;

                int pre = gs.getXs() | gs.getOs();
                gs.makeMove(newMove);

                repaint();

                if ((gs.getXs() | gs.getOs()) != pre) {
                    checkAndPerformBotMove();
                }
            }
        }

        public void checkAndPerformBotMove() {
            if (gs.isGameOver()) return;

            boolean isXTurn = gs.getTurn();
            if ((isXTurn && Main.Xbot) || (!isXTurn && Main.Obot)) {
                TicTacBot.botMove(gs);
                repaint();
                // a timer to make bot playing itself look better
                Timer timer = new Timer(500, (ActionEvent) -> {
                // Recursively call to see if the next player is also a bot
                    checkAndPerformBotMove();
                });
                timer.setRepeats(false);
                timer.start();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {}

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }
}