import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * A random moving AI
 */
public class NitinAI extends Player
{
    /**
     * Contructs a random AI given a name and color
     * @param color - color the player is playing as
     * @param name - name of the player
     */
    public NitinAI(int color,String name)
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
        ArrayList<Move> vm = new ArrayList<>();
        ArrayList<Integer> am = new ArrayList<>();

        ArrayList<IntPoint> avaiableMoves = board.moveLocations(getColor());
        //System.out.println("available move locations "+avaiableMoves);
        ArrayList<Integer> usableShapePositions = new ArrayList<>();
        boolean[] used = (getColor()==BlokusBoard.ORANGE)?board.getOrangeUsedShapes():board.getPurpleUsedShapes();
        for(int x=0; x<used.length; x++)
            if(!used[x])
                usableShapePositions.add(x);
        //System.out.println("usable pieces "+ Arrays.toString(used));

        if(usableShapePositions.isEmpty() ||avaiableMoves.isEmpty())
            return null;
        else {
            //System.out.println("hi");
            Move move = null;
            for (IntPoint movLoc : avaiableMoves)
                for (Integer position : usableShapePositions) {
                    for (int i = 0; i < 8; i++) {
                        boolean flip = i > 3;
                        int rotation = i % 4;
                        boolean[][] shape = board.getShapes().get(position).manipulatedShape(flip, rotation);
                        for (int r = -shape.length + 1; r < shape.length; r++)
                            for (int c = -shape[0].length + 1; c < shape[0].length; c++) {
                                IntPoint topLeft = new IntPoint(movLoc.getX() + c, movLoc.getY() + r);
                                Move test = new Move(position, flip, rotation, topLeft);

                                if (board.isValidMove(test, getColor())) {
                                    vm.add(test);
                                }
                            }
                    }
                }
            for (int x = 0; x < vm.size() - 1; x++)
            {
                int opponentColor = 0;
                if (getColor() == 4)
                    opponentColor = 5;
                else
                    opponentColor = 4;

                BlokusBoard clone = new BlokusBoard(board);

                clone.makeMove(vm.get(x), getColor());
                int opponentsMoves2 = clone.moveLocations(opponentColor).size();
                am.add(opponentsMoves2);
//
            }
            int lv = 1000000;
            int loc = 0;
            for(int x=0;x<am.size()-1;x++)
            {
                if(am.get(x)<lv)
                {
                    lv = am.get(x);
                    loc = x;
                }
            }
            if(getColor()==5) {
                if (loc == 0 && vm.size() >= 150)
                    loc = vm.size() - 150;
            }
            int l = 122;
            if(getColor()==4) {
                if (loc == 0 && vm.size() >= l)
                    loc = vm.size() - l;
            }
                if (vm.size() > loc)
                    return vm.get(loc);

            return null;


        }

    }

    /**
     * Returns a clone of the player
     * @return a clone of this player
     */
    public Player freshCopy()
    {
        return new NitinAI(getColor(),getName());
    }
}
