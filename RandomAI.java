import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * A random moving AI
 */
public class RandomAI extends Player
{
    /**
     * Contructs a random AI given a name and color
     * @param color - color the player is playing as
     * @param name - name of the player
     */
    public RandomAI(int color,String name)
    {
        super(color, name);
    }

    /**
     * Returns a valid move.
     * @param board - the board that a move should be made on
     * @return a valid move, null if non can be found
     */
    public Move getMove(BlokusBoard board)
    {
        //System.out.println("my color is "+getColor() + " the turn is "+board.getTurn());
        ArrayList<IntPoint> avaiableMoves = board.moveLocations(getColor());
        Collections.shuffle(avaiableMoves);
        //System.out.println("available move locations "+avaiableMoves);
        ArrayList<Integer> usableShapePositions = new ArrayList<>();
        boolean[] used = (getColor()==BlokusBoard.ORANGE)?board.getOrangeUsedShapes():board.getPurpleUsedShapes();
        for(int x=0; x<used.length; x++)
            if(!used[x])
                usableShapePositions.add(x);
        //System.out.println("usable pieces "+ Arrays.toString(used));
        Collections.shuffle(usableShapePositions);
        if(usableShapePositions.isEmpty() ||avaiableMoves.isEmpty())
            return null;
        else
        {
            //System.out.println("hi");
            Move move = null;
                    for(IntPoint movLoc: avaiableMoves)
                        for(Integer position: usableShapePositions)
                        {
                            for(int i=0; i<8;i++) {
                                boolean flip = i > 3;
                                int rotation = i % 4;
                                boolean[][] shape = board.getShapes().get(position).manipulatedShape(flip, rotation);
                                for (int r = -shape.length+1; r <shape.length;  r++)
                                    for (int c = -shape[0].length+1; c < shape[0].length; c++)
                                    {
                                        IntPoint topLeft = new IntPoint(movLoc.getX()+c,movLoc.getY()+r);
                                        Move test = new Move(position,flip,rotation,topLeft);
                                        if(board.isValidMove(test,getColor()))
                                            return test;
                                    }
                            }
                }
            return null;
        }
    }

    /**
     * Returns a clone of the player
     * @return a clone of this player
     */
    public Player freshCopy()
    {
        return new RandomAI(getColor(),getName());
    }
}
