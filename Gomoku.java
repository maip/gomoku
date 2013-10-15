import javax.swing.JFrame;

/**
 * The Gomoku class is the driver for the GUI game. Gomoku is a game played on
 * a 15x15 grid with two players. The goal is to get 5 pieces in a row in any
 * direction.
 *
 * @author    Melissa Ip
 * @version   1.0
 */
public class Gomoku {

    /**
     * Desired width and height of the unresizeable window excluding the frame.
     */
    private static final int WINDOW_SIZE = Display.END_MARGIN + Display.MARGIN;

    public static void main(String args[]) {
        JFrame f = new JFrame();
        Display d = new Display();

        f.add(d);
        f.setSize(WINDOW_SIZE + 6, WINDOW_SIZE + 28);  // accounts for frame
        f.setResizable(false);
        f.setTitle("Gomoku");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}