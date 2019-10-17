import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.BufferedImage;

/**
 * Srceen that dipslays how the game is playing out
 */
public class DisplayScreen extends JFrame implements Runnable, KeyListener
{
    // The board of the game
    public BlokusBoard board = null;
    // image used for double buffeting
    BufferedImage img;

    /**
     * Constructs a display screen that will display what is going on the provided board
     * @param board
     */
    public DisplayScreen(BlokusBoard board)
    {
        super();
        this.board = board;
        setSize(700,420);
        addKeyListener(this);
        img = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_ARGB);
        setUndecorated(true);

        setVisible(true);
    }

    /**
     * paints the game tot he frame's graphics
     * @param g - graphics of the frame
     */
    public void paint(Graphics g)
    {
        board.draw(img.getGraphics());

        g.drawImage(img,0,0,null);
    }

    /**
     * Starts the painting thread
     */
    public void addNotify()
    {
        super.addNotify();
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * Run method that will continually repaint the screen
     */
    public void run()
    {

        while(true)
        {
            try
            {
                repaint();

                Thread.sleep(100);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Not used
     * @param e - the key event
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * closes the program when escape is pressed
     * @param e - the key even
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key==e.VK_ESCAPE)
        {
            System.exit(0);
        }
    }

    /**
     * Not used
     * @param e - the key event
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }


}