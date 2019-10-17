/**
 * Abstract class of what a player is
 */
public abstract class Player
{
    // Stores the player's Color (Values come from CheckerBoard)
    private int color;

    // Stores the player's Name
    private String name;

    /**
     * Contructs a player given a name and color
     * @param color - color the player is playing as
     * @param name - name of the player
     */
    public Player(int color, String name) {
        this.color = color;
        this.name = name;
    }

    /**
     * Returns the player's color
     * @return player color
     */
    public int getColor() {
        return color;
    }

    /**
     * Returns the other player's color
     * @return the color of the other player
     */
    public int getOtherColor() {

        return (color==BlokusBoard.ORANGE)?BlokusBoard.PURPLE:BlokusBoard.ORANGE;
    }

    /**
     * REturns the player's name
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * returns a text version of the player
     * (name/color)
     * @return text version of the player
     */
    @Override
    public String toString()
    {
        return getName();
    }

    /**
     * Determines a good valid move and returns it
     * @param board - the board that a move should be made on
     * @return - a valid or null if the player wants to skip their turn
     */
    public abstract Move getMove(BlokusBoard board);

    /**
     * Returns a clone of the player
     * @return a clone of this player
     */
    public abstract Player freshCopy();
}


