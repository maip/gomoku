import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The Display class handles the graphics, draws the grid and pieces, and
 * updates after every move by getting input from the mouse and keyboard.
 */
public class Display extends JPanel implements KeyListener, MouseListener {

    /**
     * Constants for determining grid and piece size.
     */
    public static final int MARGIN = 35;
    public static final int GRID_ROWS = 15;
    public static final int PIECE_DIAMETER = 30;
    public static final int END_MARGIN = MARGIN * GRID_ROWS;

    /**
     * Instance variables that keep track of game play.
     */
    private Point currentLocation;
    private ArrayList<Point> playerOneMoves;
    private ArrayList<Point> playerTwoMoves;
    private boolean isPlayerOneTurn;
    private boolean hasPlayerOneWon;
    private boolean hasPlayerTwoWon;

    /**
     * Class constructor initalizes this to receive input from mouse click and
     * Enter key.
     */
    public Display() {
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setFocusable(true);
        currentLocation = new Point();
        playerOneMoves = new ArrayList<Point>();
        playerTwoMoves = new ArrayList<Point>();
        isPlayerOneTurn = true;
        hasPlayerOneWon = false;
        hasPlayerTwoWon = false;
    }

    /**
     * Update the display every turn or after receiving input.
     *
     * @params g  Graphics object to draw
     */
    public void paint(Graphics g) {
        super.paint(g);
        drawGrid(g);
        drawAllCircles(g);
    }

    /**
     * Draw a 15x15 black grid using class constants.
     *
     * @params g  Graphics object to draw lines
     */
    private void drawGrid(Graphics g) {
        g.setColor(Color.black);
        for (int space = 0; space < GRID_ROWS * MARGIN; space += MARGIN) {
            g.drawLine(MARGIN + space, MARGIN,
                       MARGIN + space, GRID_ROWS*MARGIN);
            g.drawLine(MARGIN, MARGIN + space,
                       GRID_ROWS*MARGIN, MARGIN + space);
        }
    }

    /**
     * Draw all pieces for both players.
     * 
     * Player 1 is white and moves first. Player 2 is black and moves second.
     *
     * @params g  Graphics object to draw circles
     */
    private void drawAllCircles(Graphics g) {
        drawCircle(g, playerOneMoves, Color.WHITE);
        drawCircle(g, playerTwoMoves, Color.BLACK);
    }

    /**
     * Draw pieces at specified locations given by a list of Points.
     *
     * @params g      Graphics object to draw circles
     * @params moves  list of Points containing locations where pieces should be
     * @params col    color of pieces
     */
    private static void drawCircle(Graphics g, ArrayList<Point> moves, Color col) {
        for (Point piece: moves) {
            g.setColor(col);
            g.fillOval(piece.getX() - PIECE_DIAMETER/2,
                       piece.getY() - PIECE_DIAMETER/2,
                       PIECE_DIAMETER,
                       PIECE_DIAMETER);
            if (col != Color.BLACK) {
                g.setColor(Color.BLACK);  // black borders on white pieces
                g.drawOval(piece.getX() - PIECE_DIAMETER/2,
                           piece.getY() - PIECE_DIAMETER/2,
                           PIECE_DIAMETER,
                           PIECE_DIAMETER);
            }
        }
    }

    /**
     * Check for a win, 5 pieces in a row in any direction.
     */
    private void checkWin() {
        ArrayList<Point> moves = getlastPlayerMoves();

        // begin with all valid adjacent locations
        ArrayList<Point> adjacentPoints = currentLocation.getAdjacentAll();
        for (Point adjPoint : adjacentPoints) {
            if (moves.contains(adjPoint)) {
                // look for more pieces in this direction
                int i = adjacentPoints.indexOf(adjPoint);
                int direction = Point.DIRECTIONS[i];
                boolean isGameOver = checkInRow(moves, adjPoint, direction, 3);
                if (isGameOver) {
                    setWinner();
                }
            }
        }
    }

    /**
     * Set hasPlayerOneWon or hasPlayerTwoWon to true. Assume game is over.
     */
    private void setWinner() {
        if (!isPlayerOneTurn) {
            hasPlayerOneWon = true;
        } else {
            hasPlayerTwoWon = true;
        }
    }

    /**
     * Check for additional consecutive pieces and report win.
     *
     * @params moves  list of Points containing locations where pieces should be
     * @params loc    starting location to check adjacent Points
     * @params dir    direction in which to look for adjacent pieces
     * @params num    number of times to check in a row
     */
    private static boolean checkInRow(ArrayList<Point> moves, Point loc, int dir,
            int num) {
        Point nextLoc = loc.getAdjacent(dir);
        while (moves.contains(nextLoc) && num > 0) {
            nextLoc = nextLoc.getAdjacent(dir);
            num--;
        }
        if (num == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get list of moves of the player that went last.
     *
     * @return  list of moves for the last player
     */
    private ArrayList<Point> getlastPlayerMoves() {
        if (!isPlayerOneTurn) {
            return playerOneMoves;
        } else {
            return playerTwoMoves;
        }
    }

    /**
     * Set location from mouse click and make call to update display.
     * Freeze the display if the game is over.
     * 
     * @params e  mouse click input
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!hasPlayerOneWon && !hasPlayerTwoWon) {
            Point newLocation = Point.convertFromPixel(e.getX(), e.getY());
            if (isPlayerOneTurn) {
                // if not previous move and not overlapping with player two
                if (!playerOneMoves.contains(newLocation) &&
                        !playerTwoMoves.contains(newLocation)) {
                    playerOneMoves.add(newLocation);
                    isPlayerOneTurn = false;
                }
            } else {  // player two
                // if not previous move and not overlapping with player one
                if (!playerTwoMoves.contains(newLocation) &&
                        !playerOneMoves.contains(newLocation)) {
                    playerTwoMoves.add(newLocation);
                    isPlayerOneTurn = true;
                }
            }
            currentLocation = newLocation;
            checkWin();
        }
        this.repaint();
        showWinDialog();
    }

    /**
     * Display a dialog saying which player won the game and reset game after
     * clicking OK.
     */
    private void showWinDialog() {
        String winner = null;
        if (hasPlayerOneWon) {
            winner = "Player 1 won.";
        } else if (hasPlayerTwoWon) {
            winner = "Player 2 won.";
        }

        if (hasPlayerOneWon || hasPlayerTwoWon) {
            JOptionPane.showMessageDialog(null, winner, "Gomoku",
                                          JOptionPane.PLAIN_MESSAGE);
            resetGame();
            this.repaint();
        }
    }

    /**
     * Clear the board for a new game if Enter key is pressed.
     *
     * @params e  key input
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            resetGame();
            this.repaint();
        }
    }

    /**
     * Reset all variables to default values.
     */
    private void resetGame() {
        currentLocation.setAll(0, 0);
        playerOneMoves.clear();
        playerTwoMoves.clear();
        isPlayerOneTurn = true;
        hasPlayerOneWon = false;
        hasPlayerTwoWon = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void keyReleased(KeyEvent arg0) {}

    @Override
    public void keyTyped(KeyEvent arg0) {}
}