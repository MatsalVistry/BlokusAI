import java.io.Serializable;

/**
 * Store data for a game move
 */
public class Move implements Serializable
{
    // stores if the piece needs to be flipped (true - yes / false no)
    private boolean flip;
    // stores how many times the piece needs to be rotated
    private int rotation;
    // stores the index of the peice being placed
    private int pieceNumber;
    // stores the top left corner of where the piece will be placed on the game board
    private IntPoint point;

    /**
     * Creates a move with the provided data
     * @param pieceNumber - which piece is being placed
     * @param flip - if is it flipped
     * @param rotation - number of times to rotate it
     * @param point - position on the board that it will be placed
     */
    public Move(int pieceNumber, boolean flip, int rotation, IntPoint point) {
        this.pieceNumber = pieceNumber;
        this.flip = flip;
        this.rotation = rotation;
        this.point=point;
    }

    /**
     * Returns where the piece is the to be placed
     * @return - the top left corner for the piece placement
     */
    public IntPoint getPoint() {
        return point;
    }

    /**
     * Which piece is being placed
     * @return - the index of the piece
     */
    public int getPieceNumber() {
        return pieceNumber;
    }

    /**
     * Returns if the piece needs to be flipped
     * @return - true if the piece is to be flipped, false if not.
     */
    public boolean isFlip() {
        return flip;
    }

    /**
     * Returns how many times the piece needs to be rotated
     * @return - number of rotations needed
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Returns a text version of the move
     * (location/flipped/roations/piece index)
     * @return text version of the move
     */
    public String toString()
    {
        String s = "Move location: ("+point+")\n";
        s+= "flip: "+flip +"\n";
        s+= "ratation: "+rotation+"\n";
        s+= "piece: " +pieceNumber+"\n";
        return s;
    }
}
